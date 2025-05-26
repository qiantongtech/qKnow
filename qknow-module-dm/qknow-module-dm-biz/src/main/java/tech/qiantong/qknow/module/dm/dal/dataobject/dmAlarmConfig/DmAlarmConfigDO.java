package tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 告警配置 DO 对象 dm_alarm_config
 *
 * @author qknow
 * @date 2025-02-19
 */
@Data
@TableName(value = "dm_alarm_config")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("dm_alarm_config_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmAlarmConfigDO extends BaseEntity {
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
    @TableLogic
    private Boolean delFlag;


}
