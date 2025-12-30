package tech.qiantong.qknow.common.database.dialect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.core.FileInfo;
import tech.qiantong.qknow.common.exception.ServiceException;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
public class HdfsDialect extends AbstractDbDialect implements FileDialect {

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

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + dbQueryProperty.getHost() + ":" + dbQueryProperty.getPort());
        try (FileSystem fs = FileSystem.get(conf)) {
            Path path = new Path("/"); // 或者你想要测试的其他路径
            log.info("Hdfs / has items {}", fs.listStatus(path).length);
            return true;
        } catch (Exception e) {
            log.error("Hdfs连接失败", e);
            return false;
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public List<FileInfo> getFiles(DbQueryProperty dbQueryProperty, String pathStr) {
        pathStr = pathStr == null ? "/" : pathStr;
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + dbQueryProperty.getHost() + ":" + dbQueryProperty.getPort());
        try (FileSystem fs = FileSystem.get(conf)) {
            Path path = new Path(pathStr);
            FileStatus[] fileStatuses = fs.listStatus(path);
            return Arrays.stream(fileStatuses).map(status -> {
                FileInfo info = new FileInfo();
                info.setName(status.getPath().getName());
                info.setPath(status.getPath().toUri().getPath());
                info.setDirectory(status.isDirectory());
                info.setSize(status.getLen());
                info.setLastModified(new Date(status.getModificationTime()));
                info.fillType();
                return info;
            }).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Hdfs连接失败", e);
            throw new ServiceException("Hdfs查询失败");
        }
    }

    @Override
    public void uploadFile(DbQueryProperty dbQueryProperty, String pathStr, MultipartFile file) {
        String filename = file.getOriginalFilename() == null ? System.currentTimeMillis() + "" : file.getOriginalFilename();
        pathStr = pathStr == null ? "" : pathStr;
        pathStr = resolvePath(pathStr, filename);
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + dbQueryProperty.getHost() + ":" + dbQueryProperty.getPort());
        Path path = new Path(pathStr);
        try (FileSystem fs = FileSystem.get(conf);
             InputStream inputStream = file.getInputStream();
             FSDataOutputStream outputStream = fs.create(path)) {
            IOUtils.copy(inputStream, outputStream);
            fs.setPermission(path,
                    new FsPermission(FsAction.READ_WRITE, FsAction.READ, FsAction.READ));
        } catch (Exception e) {
            log.error("Hdfs上传操作失败", e);
            throw new ServiceException("Hdfs上传操作失败了");
        }
    }

    private String resolvePath(String path, String filename) {
        String str = path + "/" + filename;
        return str.replaceAll("/+", "/");
    }
}
