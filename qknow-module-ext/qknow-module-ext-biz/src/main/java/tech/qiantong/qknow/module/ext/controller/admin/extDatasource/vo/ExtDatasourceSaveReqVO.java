package tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 数据源 创建/修改 Request VO ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "数据源 Response VO")
@Data
public class ExtDatasourceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据库连接名称", example = "")
    @NotBlank(message = "数据库连接名称不能为空")
    @Size(max = 256, message = "数据库连接名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据库类型", example = "")
    @NotNull(message = "数据库类型不能为空")
    private Integer type;

    @Schema(description = "数据库地址", example = "")
    @NotBlank(message = "数据库地址不能为空")
    @Size(max = 256, message = "数据库地址长度不能超过256个字符")
    private String host;

    @Schema(description = "端口号", example = "")
    @NotNull(message = "端口号不能为空")
    private Long port;

    @Schema(description = "用户名", example = "")
    @Size(max = 256, message = "用户名长度不能超过256个字符")
    private String username;

    @Schema(description = "数据库名称", example = "")
    @Size(max = 32, message = "数据库名称长度不能超过32个字符")
    private String databaseName;

    @Schema(description = "密码", example = "")
    @Size(max = 256, message = "密码长度不能超过256个字符")
    private String password;

    @Schema(description = "连接状态", example = "")
//    @NotNull(message = "连接状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
