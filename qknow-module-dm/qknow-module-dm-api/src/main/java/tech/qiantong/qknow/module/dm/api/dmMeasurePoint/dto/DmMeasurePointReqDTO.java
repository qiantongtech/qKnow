package tech.qiantong.qknow.module.dm.api.dmMeasurePoint.dto;

import lombok.Data;

/**
 * 物联网测点 DTO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Data
public class DmMeasurePointReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 测点名称 */
    private String name;

    /** 测点号 */
    private String code;

    /** 设备名称 */
    private String deviceName;

    /** 测点类型 */
    private Integer type;

    /** 设备key */
    private String deviceKey;

    /** 前缀 */
    private String prefix;

    /** 是否实时获取 */
    private Integer realtimeFlag;

    /** 同步频率（分钟） */
    private Long frequency;

    /** 单位 */
    private String unit;

    /** 是否为故障诊断 */
    private Integer failureFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
