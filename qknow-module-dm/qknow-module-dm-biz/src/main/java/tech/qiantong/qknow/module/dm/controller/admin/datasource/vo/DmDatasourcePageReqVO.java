package tech.qiantong.qknow.module.dm.controller.admin.datasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 数据源 Request VO 对象 DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据源 Request VO")
@Data
public class DmDatasourcePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Schema(description = "IP", example = "")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "数据库表数", example = "")
    private Long listCount;

    @Schema(description = "同步记录数", example = "")
    private Long syncCount;

    @Schema(description = "同步数据量大小", example = "")
    private Long dataSize;

    @Schema(description = "描述", example = "")
    private String description;



}
