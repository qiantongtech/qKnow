package tech.qiantong.qknow.common.database.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.qiantong.qknow.common.utils.FileTypeUtil;

import java.util.Date;

/**
 * 数据资产-非结构化数据 目录或文件夹
 *
 * @author Chaos
 * @date 2025-07-16
 */
@Data
public class FileInfo {

    private String name;
    private String path;
    private boolean isDirectory;
    private long size;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastModified;
    private String type;

    public void fillType(){
        type = isDirectory ? "目录" : FileTypeUtil.getFileType(name);
    }

}
