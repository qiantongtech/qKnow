/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.service.sync;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.config.AniviaConfig;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentSaveReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageBrowseReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageFileRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageImportReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageSelectionReqVO;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeDocumentService;

import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivilegedExceptionAction;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class KgStorageBrowserService {

    private static final Object HDFS_SECURITY_LOCK = new Object();
    private static final HashSet<String> HDFS_CLIENT_AUTH_KEYS = new HashSet<>(Arrays.asList(
            "kerberosPrincipal", "kerberosKeytabFilePath", "kerberosKrb5ConfPath"));

    private static final HashSet<String> TEXT_FILE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "txt", "pdf", "html", "xlsx", "xls", "docx", "csv", "md", "mdx", "htm", "markdown"));

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @Resource
    private IKgKnowledgeDocumentService kgKnowledgeDocumentService;

    public void testConnection(KgStorageBrowseReqVO.Connection connection) {
        String type = normalizeType(connection.getDatasourceType());
        try {
            switch (type) {
                case "HDFS" -> testHdfs(connection);
                case "FTP" -> testFtp(connection);
                case "OSS-ALIYUN", "OSS" -> testOss(connection);
                default -> throw new ServiceException("暂不支持的数据连接类型：" + type);
            }
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServiceException("连接测试失败：" + getErrorMessage(exception));
        }
    }

    public List<KgStorageFileRespVO> listDirectories(KgStorageBrowseReqVO request) {
        return listEntries(request.getConnection(), normalizePath(request.getPath())).stream()
                .filter(item -> Boolean.TRUE.equals(item.getDirectory()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> listFiles(KgStorageBrowseReqVO request) {
        List<KgStorageFileRespVO> entries = listEntries(
                request.getConnection(), normalizePath(request.getPath()));
        String keyword = StrUtil.trim(request.getFileName());
        if (StrUtil.isNotBlank(keyword)) {
            entries = entries.stream()
                    .filter(item -> StrUtil.containsIgnoreCase(item.getName(), keyword))
                    .collect(Collectors.toList());
        }

        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1
                ? 10 : Math.min(request.getPageSize(), 200);
        int total = entries.size();
        int fromIndex = Math.min((pageNum - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", entries.subList(fromIndex, toIndex));
        result.put("total", total);
        return result;
    }

    public Map<String, Object> resolveCandidates(KgStorageSelectionReqVO request) {
        CandidateFiles candidates = collectCandidates(request);
        Map<String, Object> result = new HashMap<>();
        result.put("acceptedFiles", candidates.accepted());
        result.put("rejectedFiles", candidates.rejected());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int importDocuments(
            KgStorageImportReqVO request,
            Long creatorId,
            String creatorName,
            Long workspaceId) {
        CandidateFiles candidates = collectCandidates(request);
        if (candidates.accepted().isEmpty()) {
            throw new ServiceException("所选内容中没有可导入的支持格式文件");
        }

        java.nio.file.Path importDirectory = Paths.get(
                AniviaConfig.getProfile(),
                "storage-sync",
                LocalDate.now().toString().replace("-", ""),
                UUID.randomUUID().toString().replace("-", ""));
        try {
            Files.createDirectories(importDirectory);
            List<LocalImportFile> files = new ArrayList<>();
            for (KgStorageFileRespVO remoteFile : candidates.accepted()) {
                String originalName = sanitizeDocumentName(remoteFile.getName());
                String localName = UUID.randomUUID().toString().replace("-", "") + "_" + originalName;
                java.nio.file.Path target = importDirectory.resolve(localName);
                try (OutputStream output = Files.newOutputStream(target)) {
                    writeFile(request.getConnection(), remoteFile.getPath(), output);
                }
                String relativePath = "/" + Paths.get(AniviaConfig.getProfile())
                        .relativize(target).toString().replace('\\', '/');
                files.add(new LocalImportFile(originalName, relativePath));
            }

            KgKnowledgeDocumentSaveReqVO document = buildDocumentRequest(request, files, creatorId, creatorName, workspaceId);
            return Math.toIntExact(kgKnowledgeDocumentService.createKgKnowledgeDocument(document));
        } catch (ServiceException exception) {
            deleteImportDirectory(importDirectory);
            throw exception;
        } catch (Exception exception) {
            deleteImportDirectory(importDirectory);
            throw new ServiceException("导入第三方存储文件失败：" + getErrorMessage(exception));
        }
    }

    private CandidateFiles collectCandidates(KgStorageSelectionReqVO request) {
        LinkedHashMap<String, KgStorageFileRespVO> accepted = new LinkedHashMap<>();
        LinkedHashMap<String, KgStorageFileRespVO> rejected = new LinkedHashMap<>();
        for (KgStorageSelectionReqVO.SelectedItem item : request.getSelectedItems()) {
            String path = normalizePath(item.getPath());
            if (Boolean.TRUE.equals(item.getDirectory())) {
                collectDirectoryCandidates(request.getConnection(), path, accepted, rejected);
            } else {
                addCandidate(KgStorageFileRespVO.builder()
                        .name(StrUtil.blankToDefault(item.getName(), getName(path)))
                        .path(path)
                        .directory(false)
                        .hasChildren(false)
                        .fileType(getFileType(path))
                        .build(), accepted, rejected);
            }
        }
        return new CandidateFiles(new ArrayList<>(accepted.values()), new ArrayList<>(rejected.values()));
    }

    private void collectDirectoryCandidates(
            KgStorageBrowseReqVO.Connection connection,
            String directory,
            Map<String, KgStorageFileRespVO> accepted,
            Map<String, KgStorageFileRespVO> rejected) {
        for (KgStorageFileRespVO item : listEntries(connection, directory)) {
            if (Boolean.TRUE.equals(item.getDirectory())) {
                collectDirectoryCandidates(connection, item.getPath(), accepted, rejected);
            } else {
                addCandidate(item, accepted, rejected);
            }
        }
    }

    private void addCandidate(
            KgStorageFileRespVO item,
            Map<String, KgStorageFileRespVO> accepted,
            Map<String, KgStorageFileRespVO> rejected) {
        if (isSupportedFile(item.getName())) {
            accepted.putIfAbsent(item.getPath(), item);
            rejected.remove(item.getPath());
        } else if (!accepted.containsKey(item.getPath())) {
            rejected.putIfAbsent(item.getPath(), item);
        }
    }

    private KgKnowledgeDocumentSaveReqVO buildDocumentRequest(
            KgStorageImportReqVO source,
            List<LocalImportFile> files,
            Long creatorId,
            String creatorName,
            Long workspaceId) {
        KgKnowledgeDocumentSaveReqVO target = new KgKnowledgeDocumentSaveReqVO();
        target.setCategoryId(source.getCategoryId());
        target.setCategoryName(source.getCategoryName());
        target.setDescription(source.getDescription());
        target.setRemark(source.getRemark());
        target.setName(files.stream().map(LocalImportFile::name).collect(Collectors.joining(",")));
        target.setPath(files.stream().map(LocalImportFile::path).collect(Collectors.joining(",")));
        target.setCreatorId(creatorId);
        target.setCreateBy(creatorName);
        target.setCreateTime(new Date());
        target.setWorkspaceId(workspaceId);
        return target;
    }

    private boolean isSupportedFile(String name) {
        String extension = getFileExtension(name);
        return TEXT_FILE_EXTENSIONS.contains(extension);
    }

    private String getFileExtension(String name) {
        String value = StrUtil.blankToDefault(name, "");
        int index = value.lastIndexOf('.');
        return index > -1 && index < value.length() - 1
                ? value.substring(index + 1).toLowerCase(Locale.ROOT) : "";
    }

    private String sanitizeDocumentName(String name) {
        return StrUtil.blankToDefault(name, "file")
                .replace(',', '_')
                .replace('/', '_')
                .replace('\\', '_');
    }

    private record CandidateFiles(
            List<KgStorageFileRespVO> accepted,
            List<KgStorageFileRespVO> rejected) {
    }

    private record LocalImportFile(String name, String path) {
    }

    public String preparePreview(KgStorageBrowseReqVO request) {
        String path = normalizePath(request.getPath());
        try {
            if (isDirectory(request.getConnection(), path)) {
                throw new ServiceException("文件夹无需生成预览地址");
            }
            java.nio.file.Path previewDirectory = Paths.get(AniviaConfig.getProfile(), "storage-preview");
            Files.createDirectories(previewDirectory);
            cleanupExpiredPreviews(previewDirectory);
            String extension = getExtension(path);
            String previewName = UUID.randomUUID().toString().replace("-", "") + extension;
            java.nio.file.Path target = previewDirectory.resolve(previewName);
            try (OutputStream output = Files.newOutputStream(target)) {
                writeFile(request.getConnection(), path, output);
            }
            return "/profile/storage-preview/" + previewName;
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServiceException("准备文件预览失败：" + getErrorMessage(exception));
        }
    }

    public void download(KgStorageBrowseReqVO request, HttpServletResponse response) {
        String path = normalizePath(request.getPath());
        try {
            boolean directory = isDirectory(request.getConnection(), path);
            String sourceName = "/".equals(path) ? "storage" : getName(path);
            String downloadName = directory ? sourceName + ".zip" : sourceName;
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''"
                    + URLEncoder.encode(downloadName, StandardCharsets.UTF_8).replace("+", "%20"));
            if (directory) {
                try (ZipOutputStream zip = new ZipOutputStream(response.getOutputStream())) {
                    writeDirectoryZip(request.getConnection(), path, sourceName, zip);
                    zip.finish();
                }
            } else {
                writeFile(request.getConnection(), path, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServiceException("下载文件失败：" + getErrorMessage(exception));
        }
    }

    private boolean isDirectory(KgStorageBrowseReqVO.Connection connection, String path) throws Exception {
        String type = normalizeType(connection.getDatasourceType());
        return switch (type) {
            case "HDFS" -> {
                try (FileSystem fileSystem = createHdfsFileSystem(connection)) {
                    Path target = new Path(path);
                    if (!fileSystem.exists(target)) throw new ServiceException("文件或目录不存在：" + path);
                    yield fileSystem.getFileStatus(target).isDirectory();
                }
            }
            case "FTP" -> {
                FTPClient client = connectFtp(connection);
                try {
                    if (client.changeWorkingDirectory(path)) yield true;
                    FTPFile[] files = client.listFiles(path);
                    if (files == null || files.length == 0) {
                        throw new ServiceException("文件或目录不存在：" + path);
                    }
                    yield files[0].isDirectory();
                } finally {
                    disconnectFtp(client);
                }
            }
            case "OSS-ALIYUN", "OSS" -> {
                OSS oss = createOssClient(connection);
                try {
                    String bucket = requireValue(connection.getBucket(), "OSS Bucket");
                    String key = toOssKey(path);
                    if (StrUtil.isNotBlank(key) && oss.doesObjectExist(bucket, key)) yield false;
                    ObjectListing listing = oss.listObjects(new ListObjectsRequest(bucket)
                            .withPrefix(StrUtil.isBlank(key) ? "" : key + "/")
                            .withMaxKeys(1));
                    if (listing.getObjectSummaries().isEmpty() && listing.getCommonPrefixes().isEmpty()) {
                        throw new ServiceException("文件或目录不存在：" + path);
                    }
                    yield true;
                } finally {
                    oss.shutdown();
                }
            }
            default -> throw new ServiceException("暂不支持的数据连接类型：" + type);
        };
    }

    private void writeFile(
            KgStorageBrowseReqVO.Connection connection, String path, OutputStream output) throws Exception {
        String type = normalizeType(connection.getDatasourceType());
        switch (type) {
            case "HDFS" -> {
                try (FileSystem fileSystem = createHdfsFileSystem(connection);
                     InputStream input = fileSystem.open(new Path(path))) {
                    input.transferTo(output);
                }
            }
            case "FTP" -> {
                FTPClient client = connectFtp(connection);
                try {
                    if (!client.retrieveFile(path, output)) {
                        throw new ServiceException("FTP 文件读取失败：" + client.getReplyString());
                    }
                } finally {
                    disconnectFtp(client);
                }
            }
            case "OSS-ALIYUN", "OSS" -> {
                OSS oss = createOssClient(connection);
                try {
                    String bucket = requireValue(connection.getBucket(), "OSS Bucket");
                    try (OSSObject object = oss.getObject(bucket, toOssKey(path));
                         InputStream input = object.getObjectContent()) {
                        input.transferTo(output);
                    }
                } finally {
                    oss.shutdown();
                }
            }
            default -> throw new ServiceException("暂不支持的数据连接类型：" + type);
        }
    }

    private void writeDirectoryZip(
            KgStorageBrowseReqVO.Connection connection,
            String path,
            String rootName,
            ZipOutputStream zip) throws Exception {
        String type = normalizeType(connection.getDatasourceType());
        switch (type) {
            case "HDFS" -> writeHdfsDirectoryZip(connection, path, rootName, zip);
            case "FTP" -> writeFtpDirectoryZip(connection, path, rootName, zip);
            case "OSS-ALIYUN", "OSS" -> writeOssDirectoryZip(connection, path, rootName, zip);
            default -> throw new ServiceException("暂不支持的数据连接类型：" + type);
        }
    }

    private void writeHdfsDirectoryZip(
            KgStorageBrowseReqVO.Connection connection,
            String path,
            String rootName,
            ZipOutputStream zip) throws Exception {
        try (FileSystem fileSystem = createHdfsFileSystem(connection)) {
            addHdfsEntries(fileSystem, new Path(path), sanitizeZipPath(rootName), zip);
        }
    }

    private void addHdfsEntries(
            FileSystem fileSystem, Path path, String zipPath, ZipOutputStream zip) throws Exception {
        FileStatus[] statuses = fileSystem.listStatus(path);
        if (statuses.length == 0) addEmptyDirectory(zipPath, zip);
        for (FileStatus status : statuses) {
            String childZipPath = zipPath + "/" + status.getPath().getName();
            if (status.isDirectory()) {
                addHdfsEntries(fileSystem, status.getPath(), childZipPath, zip);
            } else {
                zip.putNextEntry(new ZipEntry(sanitizeZipPath(childZipPath)));
                try (InputStream input = fileSystem.open(status.getPath())) {
                    input.transferTo(zip);
                }
                zip.closeEntry();
            }
        }
    }

    private void writeFtpDirectoryZip(
            KgStorageBrowseReqVO.Connection connection,
            String path,
            String rootName,
            ZipOutputStream zip) throws Exception {
        FTPClient client = connectFtp(connection);
        try {
            addFtpEntries(client, path, sanitizeZipPath(rootName), zip);
        } finally {
            disconnectFtp(client);
        }
    }

    private void addFtpEntries(
            FTPClient client, String path, String zipPath, ZipOutputStream zip) throws Exception {
        FTPFile[] files = client.listFiles(path);
        if (files == null || files.length == 0) addEmptyDirectory(zipPath, zip);
        if (files == null) return;
        for (FTPFile file : files) {
            if (".".equals(file.getName()) || "..".equals(file.getName())) continue;
            String childPath = joinPath(path, file.getName());
            String childZipPath = zipPath + "/" + file.getName();
            if (file.isDirectory()) {
                addFtpEntries(client, childPath, childZipPath, zip);
            } else {
                zip.putNextEntry(new ZipEntry(sanitizeZipPath(childZipPath)));
                if (!client.retrieveFile(childPath, zip)) {
                    throw new ServiceException("FTP 文件读取失败：" + childPath);
                }
                zip.closeEntry();
            }
        }
    }

    private void writeOssDirectoryZip(
            KgStorageBrowseReqVO.Connection connection,
            String path,
            String rootName,
            ZipOutputStream zip) throws Exception {
        OSS oss = createOssClient(connection);
        try {
            String bucket = requireValue(connection.getBucket(), "OSS Bucket");
            String prefix = toOssPrefix(path);
            String marker = null;
            boolean hasFile = false;
            do {
                ListObjectsRequest request = new ListObjectsRequest(bucket)
                        .withPrefix(prefix)
                        .withMaxKeys(1000);
                request.setMarker(marker);
                ObjectListing listing = oss.listObjects(request);
                for (OSSObjectSummary summary : listing.getObjectSummaries()) {
                    String key = summary.getKey();
                    if (StrUtil.isBlank(key) || key.equals(prefix) || key.endsWith("/")) continue;
                    hasFile = true;
                    String relativePath = key.substring(prefix.length());
                    zip.putNextEntry(new ZipEntry(sanitizeZipPath(rootName + "/" + relativePath)));
                    try (OSSObject object = oss.getObject(bucket, key);
                         InputStream input = object.getObjectContent()) {
                        input.transferTo(zip);
                    }
                    zip.closeEntry();
                }
                marker = listing.isTruncated() ? listing.getNextMarker() : null;
            } while (marker != null);
            if (!hasFile) addEmptyDirectory(rootName, zip);
        } finally {
            oss.shutdown();
        }
    }

    private void addEmptyDirectory(String path, ZipOutputStream zip) throws Exception {
        zip.putNextEntry(new ZipEntry(sanitizeZipPath(path) + "/"));
        zip.closeEntry();
    }

    private String sanitizeZipPath(String path) {
        String value = StrUtil.blankToDefault(path, "storage").replace('\\', '/');
        while (value.startsWith("/")) value = value.substring(1);
        return value.replace("../", "");
    }

    private String toOssKey(String path) {
        String normalized = normalizePath(path);
        return "/".equals(normalized) ? "" : normalized.substring(1);
    }

    private String getExtension(String path) {
        String name = getName(path);
        int index = name == null ? -1 : name.lastIndexOf('.');
        return index > -1 ? name.substring(index) : "";
    }

    private void cleanupExpiredPreviews(java.nio.file.Path directory) {
        long expiresAt = System.currentTimeMillis() - 6 * 60 * 60 * 1000L;
        try (java.util.stream.Stream<java.nio.file.Path> paths = Files.list(directory)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    if (Files.getLastModifiedTime(path).toMillis() < expiresAt) Files.deleteIfExists(path);
                } catch (Exception ignored) {
                    // 预览缓存清理失败不影响本次预览。
                }
            });
        } catch (Exception ignored) {
            // 预览缓存清理失败不影响本次预览。
        }
    }

    private void deleteImportDirectory(java.nio.file.Path directory) {
        if (directory == null || !Files.exists(directory)) return;
        try (java.util.stream.Stream<java.nio.file.Path> paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (Exception ignored) {
                    // 导入失败后的文件清理不覆盖原始异常。
                }
            });
        } catch (Exception ignored) {
            // 导入失败后的文件清理不覆盖原始异常。
        }
    }

    private List<KgStorageFileRespVO> listEntries(
            KgStorageBrowseReqVO.Connection connection, String path) {
        String type = normalizeType(connection.getDatasourceType());
        try {
            List<KgStorageFileRespVO> entries = switch (type) {
                case "HDFS" -> listHdfs(connection, path);
                case "FTP" -> listFtp(connection, path);
                case "OSS-ALIYUN", "OSS" -> listOss(connection, path);
                default -> throw new ServiceException("暂不支持的数据连接类型：" + type);
            };
            entries.sort(Comparator
                    .comparing(KgStorageFileRespVO::getDirectory, Comparator.reverseOrder())
                    .thenComparing(KgStorageFileRespVO::getName, String.CASE_INSENSITIVE_ORDER));
            return entries;
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServiceException("读取目录失败：" + getErrorMessage(exception));
        }
    }

    private void testHdfs(KgStorageBrowseReqVO.Connection connection) throws Exception {
        try (FileSystem fileSystem = createHdfsFileSystem(connection)) {
            if (!fileSystem.exists(new Path("/"))) {
                throw new ServiceException("HDFS 根目录不可访问");
            }
        }
    }

    private List<KgStorageFileRespVO> listHdfs(
            KgStorageBrowseReqVO.Connection connection, String path) throws Exception {
        List<KgStorageFileRespVO> result = new ArrayList<>();
        try (FileSystem fileSystem = createHdfsFileSystem(connection)) {
            FileStatus[] statuses = fileSystem.listStatus(new Path(path));
            for (FileStatus status : statuses) {
                boolean directory = status.isDirectory();
                result.add(KgStorageFileRespVO.builder()
                        .name(status.getPath().getName())
                        .path(normalizePath(status.getPath().toUri().getPath()))
                        .directory(directory)
                        .hasChildren(directory)
                        .size(directory ? 0L : status.getLen())
                        .lastModified(formatTime(status.getModificationTime()))
                        .fileType(directory ? "文件夹" : getFileType(status.getPath().getName()))
                        .build());
            }
        }
        return result;
    }

    private FileSystem createHdfsFileSystem(KgStorageBrowseReqVO.Connection connection) throws Exception {
        String host = requireValue(connection.getIp(), "HDFS IP");
        int port = parsePort(connection.getPort(), "HDFS 端口");
        String endpoint;
        if (host.startsWith("hdfs://")) {
            URI hostUri = new URI(host);
            endpoint = hostUri.getPort() >= 0 ? host : host + ":" + port;
        } else {
            endpoint = "hdfs://" + host + ":" + port;
        }
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", endpoint);
        configuration.setBoolean("fs.hdfs.impl.disable.cache", true);
        JSONObject config = null;
        if (StrUtil.isNotBlank(connection.getConfig())) {
            config = JSONUtil.parseObj(connection.getConfig());
            config.forEach((key, value) -> {
                if (value != null && !HDFS_CLIENT_AUTH_KEYS.contains(key)) {
                    configuration.set(key, String.valueOf(value));
                }
            });
        }

        URI endpointUri = new URI(endpoint);
        if (!"kerberos".equalsIgnoreCase(configuration.get("hadoop.security.authentication"))) {
            return FileSystem.newInstance(endpointUri, configuration);
        }

        String principal = config == null ? null : config.getStr("kerberosPrincipal");
        String keytabFilePath = config == null ? null : config.getStr("kerberosKeytabFilePath");
        String krb5ConfPath = config == null ? null : config.getStr("kerberosKrb5ConfPath");
        principal = requireValue(principal, "Kerberos Principal");
        keytabFilePath = requireValue(keytabFilePath, "Kerberos Keytab 路径");

        if (!Files.isRegularFile(Paths.get(keytabFilePath))) {
            throw new ServiceException("Kerberos Keytab 文件不存在：" + keytabFilePath);
        }
        if (StrUtil.isNotBlank(krb5ConfPath)) {
            if (!Files.isRegularFile(Paths.get(krb5ConfPath))) {
                throw new ServiceException("Kerberos krb5.conf 文件不存在：" + krb5ConfPath);
            }
        }

        // UGI 的 Hadoop安全配置属于 JVM 全局状态，登录初始化需要串行执行，
        // FileSystem 实例则绑定当前 UGI，可在锁外继续读取和下载。
        synchronized (HDFS_SECURITY_LOCK) {
            if (StrUtil.isNotBlank(krb5ConfPath)) {
                System.setProperty("java.security.krb5.conf", krb5ConfPath);
            }
            UserGroupInformation.setConfiguration(configuration);
            UserGroupInformation ugi = UserGroupInformation
                    .loginUserFromKeytabAndReturnUGI(principal, keytabFilePath);
            return ugi.doAs((PrivilegedExceptionAction<FileSystem>)
                    () -> FileSystem.newInstance(endpointUri, configuration));
        }
    }

    private void testFtp(KgStorageBrowseReqVO.Connection connection) throws Exception {
        FTPClient client = connectFtp(connection);
        disconnectFtp(client);
    }

    private List<KgStorageFileRespVO> listFtp(
            KgStorageBrowseReqVO.Connection connection, String path) throws Exception {
        FTPClient client = connectFtp(connection);
        try {
            FTPFile[] files = client.listFiles(path);
            List<KgStorageFileRespVO> result = new ArrayList<>();
            if (files == null) return result;
            for (FTPFile file : files) {
                if (".".equals(file.getName()) || "..".equals(file.getName())) continue;
                boolean directory = file.isDirectory();
                result.add(KgStorageFileRespVO.builder()
                        .name(file.getName())
                        .path(joinPath(path, file.getName()))
                        .directory(directory)
                        .hasChildren(directory)
                        .size(directory ? 0L : file.getSize())
                        .lastModified(file.getTimestamp() == null
                                ? "-" : DATE_FORMATTER.format(file.getTimestamp().toInstant()))
                        .fileType(directory ? "文件夹" : getFileType(file.getName()))
                        .build());
            }
            return result;
        } finally {
            disconnectFtp(client);
        }
    }

    private FTPClient connectFtp(KgStorageBrowseReqVO.Connection connection) throws Exception {
        FTPClient client = new FTPClient();
        client.setControlEncoding("UTF-8");
        client.setConnectTimeout(10_000);
        client.setDataTimeout(15_000);
        client.connect(requireValue(connection.getIp(), "FTP IP"), parsePort(connection.getPort(), "FTP 端口"));
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            disconnectFtp(client);
            throw new ServiceException("FTP 服务拒绝连接");
        }
        if (!client.login(requireValue(connection.getUsername(), "FTP 账号"),
                requireValue(connection.getPassword(), "FTP 密码"))) {
            disconnectFtp(client);
            throw new ServiceException("FTP 账号或密码错误");
        }
        client.enterLocalPassiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
        return client;
    }

    private void disconnectFtp(FTPClient client) {
        if (client == null || !client.isConnected()) return;
        try {
            client.logout();
        } catch (Exception ignored) {
            // Ignore cleanup errors.
        }
        try {
            client.disconnect();
        } catch (Exception ignored) {
            // Ignore cleanup errors.
        }
    }

    private void testOss(KgStorageBrowseReqVO.Connection connection) {
        OSS oss = createOssClient(connection);
        try {
            String bucket = requireValue(connection.getBucket(), "OSS Bucket");
            if (!oss.doesBucketExist(bucket)) {
                throw new ServiceException("OSS Bucket 不存在或当前账号无访问权限");
            }
            oss.listObjects(new ListObjectsRequest(bucket).withMaxKeys(1));
        } finally {
            oss.shutdown();
        }
    }

    private List<KgStorageFileRespVO> listOss(
            KgStorageBrowseReqVO.Connection connection, String path) {
        OSS oss = createOssClient(connection);
        try {
            String bucket = requireValue(connection.getBucket(), "OSS Bucket");
            String prefix = toOssPrefix(path);
            ObjectListing listing = oss.listObjects(new ListObjectsRequest(bucket)
                    .withPrefix(prefix)
                    .withDelimiter("/")
                    .withMaxKeys(1000));
            List<KgStorageFileRespVO> result = new ArrayList<>();

            for (String commonPrefix : listing.getCommonPrefixes()) {
                String directoryKey = trimTrailingSlash(commonPrefix);
                result.add(KgStorageFileRespVO.builder()
                        .name(getName(directoryKey))
                        .path(normalizePath(directoryKey))
                        .directory(true)
                        .hasChildren(true)
                        .size(0L)
                        .lastModified("-")
                        .fileType("文件夹")
                        .build());
            }

            for (OSSObjectSummary summary : listing.getObjectSummaries()) {
                String key = summary.getKey();
                if (StrUtil.isBlank(key) || key.equals(prefix) || key.endsWith("/")) continue;
                result.add(KgStorageFileRespVO.builder()
                        .name(getName(key))
                        .path(normalizePath(key))
                        .directory(false)
                        .hasChildren(false)
                        .size(summary.getSize())
                        .lastModified(formatTime(summary.getLastModified()))
                        .fileType(getFileType(key))
                        .build());
            }
            return result;
        } finally {
            oss.shutdown();
        }
    }

    private OSS createOssClient(KgStorageBrowseReqVO.Connection connection) {
        String endpoint = requireValue(connection.getEndpoint(), "OSS Endpoint");
        if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
            endpoint = "https://" + endpoint;
        }
        return new OSSClientBuilder().build(
                endpoint,
                requireValue(connection.getKeyId(), "OSS KeyId"),
                requireValue(connection.getKeySecret(), "OSS KeySecret"));
    }

    private String normalizeType(String type) {
        return requireValue(type, "数据连接类型").trim().toUpperCase(Locale.ROOT);
    }

    private String normalizePath(String path) {
        String value = StrUtil.blankToDefault(path, "/").replace('\\', '/').replaceAll("/+", "/");
        if (!value.startsWith("/")) value = "/" + value;
        if (value.length() > 1 && value.endsWith("/")) value = value.substring(0, value.length() - 1);
        return value;
    }

    private String joinPath(String parent, String name) {
        String path = normalizePath(parent);
        return normalizePath("/".equals(path) ? path + name : path + "/" + name);
    }

    private String toOssPrefix(String path) {
        String normalized = normalizePath(path);
        if ("/".equals(normalized)) return "";
        return normalized.substring(1) + "/";
    }

    private String trimTrailingSlash(String value) {
        return value != null && value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String getName(String path) {
        String value = trimTrailingSlash(path);
        int index = value == null ? -1 : value.lastIndexOf('/');
        return index >= 0 ? value.substring(index + 1) : value;
    }

    private String getFileType(String name) {
        String value = getName(name);
        int index = value == null ? -1 : value.lastIndexOf('.');
        return index > -1 && index < value.length() - 1
                ? value.substring(index + 1).toUpperCase(Locale.ROOT) : "文件";
    }

    private int parsePort(String value, String label) {
        try {
            return Integer.parseInt(requireValue(value, label));
        } catch (NumberFormatException exception) {
            throw new ServiceException(label + "格式不正确");
        }
    }

    private String requireValue(String value, String label) {
        if (StrUtil.isBlank(value)) throw new ServiceException(label + "不能为空");
        return value.trim();
    }

    private String formatTime(long timestamp) {
        return DATE_FORMATTER.format(Instant.ofEpochMilli(timestamp));
    }

    private String formatTime(Date date) {
        return date == null ? "-" : formatTime(date.getTime());
    }

    private String getErrorMessage(Exception exception) {
        return StrUtil.blankToDefault(exception.getMessage(), exception.getClass().getSimpleName());
    }

}
