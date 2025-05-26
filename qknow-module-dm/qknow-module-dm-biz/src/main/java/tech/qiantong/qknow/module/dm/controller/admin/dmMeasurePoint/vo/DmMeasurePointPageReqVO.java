package tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 物联网测点 Request VO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Schema(description = "物联网测点 Request VO")
@Data
public class DmMeasurePointPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "测点名称", example = "")
    private String name;

    @Schema(description = "测点号", example = "")
    private String code;

    @Schema(description = "设备名称", example = "")
    private String deviceName;

    @Schema(description = "测点类型", example = "")
    private Integer type;

    @Schema(description = "设备key", example = "")
    private String deviceKey;

    @Schema(description = "前缀", example = "")
    private String prefix;

    @Schema(description = "是否实时获取", example = "")
    private Integer realtimeFlag;

    @Schema(description = "同步频率", example = "")
    private Long frequency;

    @Schema(description = "单位", example = "")
    private String unit;

    @Schema(description = "是否为故障诊断", example = "")
    private Integer failureFlag;




}
