package tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qknow.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * 物联网测点 Response VO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Schema(description = "物联网测点 Response VO")
@Data
public class DmMeasurePointRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "测点名称")
    @Schema(description = "测点名称", example = "")
    private String name;

    @Excel(name = "测点号")
    @Schema(description = "测点号", example = "")
    private String code;

    @Excel(name = "设备名称")
    @Schema(description = "设备名称", example = "")
    private String deviceName;

    @Excel(name = "测点类型")
    @Schema(description = "测点类型", example = "")
    private Integer type;

    @Excel(name = "设备key")
    @Schema(description = "设备key", example = "")
    private String deviceKey;

    @Excel(name = "前缀")
    @Schema(description = "前缀", example = "")
    private String prefix;

    @Excel(name = "是否实时获取")
    @Schema(description = "是否实时获取", example = "")
    private Integer realtimeFlag;

    @Excel(name = "同步频率", readConverterExp = "分=钟")
    @Schema(description = "同步频率", example = "")
    private Long frequency;

    @Excel(name = "单位")
    @Schema(description = "单位", example = "")
    private String unit;

    @Excel(name = "是否为故障诊断")
    @Schema(description = "是否为故障诊断", example = "")
    private Integer failureFlag;

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
