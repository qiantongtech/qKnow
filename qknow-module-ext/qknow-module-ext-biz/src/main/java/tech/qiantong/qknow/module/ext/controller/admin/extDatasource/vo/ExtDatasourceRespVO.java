package tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qknow.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * 数据源 Response VO 对象 ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "数据源 Response VO")
@Data
public class ExtDatasourceRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据库连接名称")
    @Schema(description = "数据库连接名称", example = "")
    private String name;

    @Excel(name = "数据库类型")
    @Schema(description = "数据库类型", example = "")
    private Integer type;

    @Excel(name = "数据库地址")
    @Schema(description = "数据库地址", example = "")
    private String host;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    @Excel(name = "用户名")
    @Schema(description = "用户名", example = "")
    private String username;

    @Excel(name = "数据库名称")
    @Schema(description = "数据库名称", example = "")
    private String databaseName;

    @Excel(name = "密码")
    @Schema(description = "密码", example = "")
    private String password;

    @Excel(name = "连接状态")
    @Schema(description = "连接状态", example = "")
    private Integer status;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
