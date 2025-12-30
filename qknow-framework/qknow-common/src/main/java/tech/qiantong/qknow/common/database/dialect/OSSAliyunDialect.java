package tech.qiantong.qknow.common.database.dialect;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.core.FileInfo;
import tech.qiantong.qknow.common.exception.ServiceException;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-20 16:44
 **/
@Slf4j
public class OSSAliyunDialect extends AbstractDbDialect implements FileDialect {

    @Override
    public RowMapper<DbTable> tableMapper() {
        return null;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return null;
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        return null;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }

    private OSS initOss(Map<String, Object> config) {
        String keyId = String.valueOf(config.get("keyId"));
        String keySecret = String.valueOf(config.get("keySecret"));
        String endpoint = String.valueOf(config.get("endpoint"));
        String domain = String.valueOf(config.get("domain"));

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return initOss(endpoint, keyId, keySecret);
    }

    @SneakyThrows
    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Map<String, Object> config = dbQueryProperty.getDatasourceConfig();
        String bucket = String.valueOf(config.get("bucket"));
        OSS ossClient = null;
        try {
            ossClient = initOss(config);
            ossClient.getBucketInfo(bucket);
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            if (ossClient != null) ossClient.shutdown();
        }
        return true;
    }

    private static OSSClient initOss(String endpoint, String accessKeyId, String accessKeySecret) {
        return new OSSClient(endpoint,
                new DefaultCredentialProvider(accessKeyId, accessKeySecret),
                new ClientConfiguration());
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public List<FileInfo> getFiles(DbQueryProperty dbQueryProperty, String p) {
        if (p != null) {
            if (p.startsWith("/")) {
                p = p.substring(1);
            }
            if (!p.endsWith("/")) {
                p += "/";
            }
        }
        String path = p;
        Map<String, Object> config = dbQueryProperty.getDatasourceConfig();
        String bucket = String.valueOf(config.get("bucket"));
        OSS ossClient = null;
        String nextMarker = path;
        ObjectListing objectListing;
        List<FileInfo> res = new ArrayList<>();
        try {
            ossClient = initOss(config);
            do {
                objectListing = ossClient.listObjects(new ListObjectsRequest(bucket).withMarker(nextMarker).withDelimiter("/").withPrefix(path));

                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                List<FileInfo> collect = sums.stream().map(summary -> {
                    FileInfo info = new FileInfo();
                    info.setName(summary.getKey().substring(summary.getKey().lastIndexOf('/') + 1));
                    info.setPath(summary.getKey());
                    info.setDirectory(false);
                    info.setSize(summary.getSize());
                    info.setLastModified(summary.getLastModified());
                    info.fillType();
                    return info;
                }).collect(Collectors.toList());
                res.addAll(collect);
                collect = objectListing.getCommonPrefixes().stream().map(dir -> {
                    FileInfo info = new FileInfo();
                    String k = dir.substring(0, dir.length() - 1);
                    info.setName(k.substring(k.lastIndexOf('/') + 1));
                    info.setPath(dir);
                    info.setDirectory(true);
                    info.setSize(0);
                    info.setLastModified(null);
                    info.fillType();
                    return info;
                }).collect(Collectors.toList());
                res.addAll(collect);
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
            return res;
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("查询oss失败");
        } finally {
            if (ossClient != null) ossClient.shutdown();
        }
    }

    @Override
    public void uploadFile(DbQueryProperty dbQueryProperty, String p, MultipartFile file) {
        String filename = file.getOriginalFilename() == null ? System.currentTimeMillis() + "" : file.getOriginalFilename();
        String k = p == null ? filename : p + "/" + filename;
        String key = k.replace("//", "/");
        Map<String, Object> config = dbQueryProperty.getDatasourceConfig();
        String bucket = String.valueOf(config.get("bucket"));
        OSS ossClient = null;
        try (InputStream inputStream = file.getInputStream()) {
            ossClient = initOss(config);
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    key,
                    inputStream);
            ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("上传oss失败");
        } finally {
            if (ossClient != null) ossClient.shutdown();
        }
    }

}

