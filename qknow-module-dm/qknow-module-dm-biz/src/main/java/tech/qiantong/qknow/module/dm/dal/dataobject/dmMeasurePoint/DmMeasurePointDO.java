package tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 物联网测点 DO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Data
@TableName(value = "dm_measure_point")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("dm_measure_point_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmMeasurePointDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Boolean delFlag;


}
