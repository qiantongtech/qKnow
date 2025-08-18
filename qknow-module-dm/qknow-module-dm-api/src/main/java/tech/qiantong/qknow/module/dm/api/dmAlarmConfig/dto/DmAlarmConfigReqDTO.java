package tech.qiantong.qknow.module.dm.api.dmAlarmConfig.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 告警配置 DTO 对象 dm_alarm_config
 *
 * @author qknow
 * @date 2025-02-19
 */
@Data
public class DmAlarmConfigReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 告警名称 */
    private String name;

    /** 工况编码 */
    private String condCode;

    /** 工况 */
    private String operateCond;

    /** 告警类型 */
    private Integer type;

    /** 阈值上限 */
    private BigDecimal upper;

    /** 阈值下限 */
    private BigDecimal  lower;

    /** 告警内容 */
    private String content;

    /** 预案 */
    private String plan;

    /** 预案简称 */
    private String planShort;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
