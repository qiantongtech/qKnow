package tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 物联网测点 创建/修改 Request VO dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Schema(description = "物联网测点 Response VO")
@Data
public class DmMeasurePointSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
//    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "测点名称", example = "")
    @NotBlank(message = "测点名称不能为空")
    @Size(max = 256, message = "测点名称长度不能超过256个字符")
    private String name;

    @Schema(description = "测点号", example = "")
    @NotBlank(message = "测点号不能为空")
    @Size(max = 256, message = "测点号长度不能超过256个字符")
    private String code;

    @Schema(description = "设备名称", example = "")
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 256, message = "设备名称长度不能超过256个字符")
    private String deviceName;

    @Schema(description = "测点类型", example = "")
    @NotNull(message = "测点类型不能为空")
    private Integer type;

    @Schema(description = "设备key", example = "")
    @NotBlank(message = "设备key不能为空")
    @Size(max = 256, message = "设备key长度不能超过256个字符")
    private String deviceKey;

    @Schema(description = "前缀", example = "")
    @Size(max = 256, message = "前缀长度不能超过256个字符")
    private String prefix;

    @Schema(description = "是否实时获取", example = "")
    private Integer realtimeFlag;

    @Schema(description = "同步频率", example = "")
    private Long frequency;

    @Schema(description = "单位", example = "")
    @Size(max = 256, message = "单位长度不能超过256个字符")
    private String unit;

    @Schema(description = "是否为故障诊断", example = "")
    @NotNull(message = "是否为故障诊断不能为空")
    private Integer failureFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
