package tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 专家经验 DO 对象 dm_expert_advice
 *
 * @author qknow
 * @date 2025-02-20
 */
@Data
@TableName(value = "dm_expert_advice")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("dm_expert_advice_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmExpertAdviceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Boolean delFlag;


}
