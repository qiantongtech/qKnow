package tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 数据源 Request VO 对象 ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "数据源 Request VO")
@Data
public class ExtDatasourcePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "数据库连接名称", example = "")
    private String name;

    @Schema(description = "数据库类型", example = "")
    private Integer type;

    @Schema(description = "数据库地址", example = "")
    private String host;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "数据库名称", example = "")
    private String databaseName;

    @Schema(description = "用户名", example = "")
    private String username;

    @Schema(description = "密码", example = "")
    private String password;

    @Schema(description = "连接状态", example = "")
    private Integer status;




}
