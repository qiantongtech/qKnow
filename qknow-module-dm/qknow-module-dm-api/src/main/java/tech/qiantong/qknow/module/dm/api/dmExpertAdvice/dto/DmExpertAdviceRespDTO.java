package tech.qiantong.qknow.module.dm.api.dmExpertAdvice.dto;

import lombok.Data;

/**
 * 专家经验 DTO 对象 dm_expert_advice
 *
 * @author qknow
 * @date 2025-02-20
 */
@Data
public class DmExpertAdviceRespDTO {

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

    /** 告警条件 */
    private String alarmCond;

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
