package tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 告警配置 创建/修改 Request VO dm_alarm_config
 *
 * @author qknow
 * @date 2025-02-19
 */
@Schema(description = "告警配置 Response VO")
@Data
public class DmAlarmConfigSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
//    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "告警名称", example = "")
    @NotBlank(message = "告警名称不能为空")
    @Size(max = 256, message = "告警名称长度不能超过256个字符")
    private String name;

    @Schema(description = "工况编码", example = "")
    @Size(max = 256, message = "工况编码长度不能超过256个字符")
    private String condCode;

    @Schema(description = "工况", example = "")
    @NotBlank(message = "工况不能为空")
    @Size(max = 256, message = "工况长度不能超过256个字符")
    private String operateCond;

    @Schema(description = "告警类型", example = "")
    @NotNull(message = "告警类型不能为空")
    private Integer type;

    @Schema(description = "阈值上限", example = "")
    private BigDecimal upper;

    @Schema(description = "阈值下限", example = "")
    private BigDecimal  lower;

    @Schema(description = "告警内容", example = "")
    @Size(max = 256, message = "告警内容长度不能超过256个字符")
    private String content;

    @Schema(description = "预案", example = "")
    @Size(max = 256, message = "预案长度不能超过256个字符")
    private String plan;

    @Schema(description = "预案简称", example = "")
    @Size(max = 256, message = "预案简称长度不能超过256个字符")
    private String planShort;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
