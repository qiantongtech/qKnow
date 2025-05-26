package tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryRespVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 知识分类 DO 对象 kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Data
@TableName(value = "kmc_category")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("kmc_category_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KmcCategoryDO {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 父级id */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 显示顺序 */
    private Long orderNum;

    /** 祖级列表 */
    private String ancestors;

    /** 是否有效 */
    private Integer validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /** 更新人id */
    private Long updaterId;

    private Long creatorId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;

    @TableField(exist = false)
    private List<KmcCategoryDO> children = new ArrayList<KmcCategoryDO>();
}
