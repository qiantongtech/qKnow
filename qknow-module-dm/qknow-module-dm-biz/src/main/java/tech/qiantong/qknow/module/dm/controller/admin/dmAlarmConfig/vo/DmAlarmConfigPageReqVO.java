package tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.math.BigDecimal;

/**
 * 告警配置 Request VO 对象 dm_alarm_config
 *
 * @author qknow
 * @date 2025-02-19
 */
@Schema(description = "告警配置 Request VO")
@Data
public class DmAlarmConfigPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "告警名称", example = "")
    private String name;

    @Schema(description = "工况编码", example = "")
    private String condCode;

    @Schema(description = "工况", example = "")
    private String operateCond;

    @Schema(description = "告警类型", example = "")
    private Integer type;

    @Schema(description = "阈值上限", example = "")
    private BigDecimal upper;

    @Schema(description = "阈值下限", example = "")
    private BigDecimal  lower;

    @Schema(description = "告警内容", example = "")
    private String content;

    @Schema(description = "预案", example = "")
    private String plan;

    @Schema(description = "预案简称", example = "")
    private String planShort;




}
