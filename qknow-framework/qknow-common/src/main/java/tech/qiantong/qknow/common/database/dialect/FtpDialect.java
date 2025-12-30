package tech.qiantong.qknow.common.database.dialect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
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
public class FtpDialect extends AbstractDbDialect implements FileDialect {

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

    private FTPClient newClient(DbQueryProperty dbQueryProperty) throws Exception {
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding(StandardCharsets.UTF_8.name());
        ftp.connect(dbQueryProperty.getHost(), dbQueryProperty.getPort());
        ftp.enterLocalPassiveMode();  // Essential for modern networks
        ftp.setFileType(FTP.BINARY_FILE_TYPE);  // Prevent corruption
        return ftp;
    }

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        FTPClient ftp = null;
        try {
            ftp = newClient(dbQueryProperty);
            return ftp.login(dbQueryProperty.getUsername(), dbQueryProperty.getPassword());
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            clean(ftp);
        }
    }

    private void clean(FTPClient ftp) {
        if (ftp == null) {
            return;
        }
        // 登出并断开连接
        try {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();  // Try proper logout first
                } catch (Exception e) {
                    log.debug("FTP logout failed (normal in some cases): {}", e.getMessage());
                }
                ftp.disconnect(); // 确保断开连接，即使在发生异常时也能执行
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public List<FileInfo> getFiles(DbQueryProperty dbQueryProperty, String p) {
        String path = p == null ? "/" : p;
        FTPClient ftp = null;
        try {
            ftp = newClient(dbQueryProperty);
            boolean login = ftp.login(dbQueryProperty.getUsername(), dbQueryProperty.getPassword());
            if (!login) {
                throw new ServiceException("FTP登录异常");
            }
            FTPFile[] ftpFiles = ftp.listFiles(path);
            return Arrays.stream(ftpFiles).map(ftpFile -> {
                        FileInfo assetFile = new FileInfo();
                        assetFile.setName(ftpFile.getName());
                        assetFile.setPath(resolvePath(path, ftpFile.getName()));
                        assetFile.setDirectory(ftpFile.isDirectory());
                        assetFile.setSize(ftpFile.getSize());
                        Calendar calendar = ftpFile.getTimestamp();
                        calendar.add(Calendar.HOUR_OF_DAY, 8);
                        assetFile.setLastModified(calendar.getTime());
                        assetFile.fillType();
                        return assetFile;
                    }
            ).collect(Collectors.toList());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("查询FTP文件异常");
        } finally {
            clean(ftp);
        }
    }

    @Override
    public void uploadFile(DbQueryProperty dbQueryProperty, String path, MultipartFile file) {
        String filename = file.getOriginalFilename() == null ? System.currentTimeMillis() + "" : file.getOriginalFilename();
        path = path == null ? "" : path;
        String key = resolvePath(path, filename);
        FTPClient ftp = null;
        try {
            ftp = newClient(dbQueryProperty);
            boolean login = ftp.login(dbQueryProperty.getUsername(), dbQueryProperty.getPassword());
            if (!login) {
                throw new ServiceException("FTP登录异常");
            }
            try (InputStream inputStream = file.getInputStream()) {
                ftp.storeFile(key, inputStream);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("上传FTP文件异常");
        } finally {
            clean(ftp);
        }
    }

    private String resolvePath(String path, String filename) {
        String str = path + "/" + filename;
        return str.replaceAll("/+", "/");
    }

}
