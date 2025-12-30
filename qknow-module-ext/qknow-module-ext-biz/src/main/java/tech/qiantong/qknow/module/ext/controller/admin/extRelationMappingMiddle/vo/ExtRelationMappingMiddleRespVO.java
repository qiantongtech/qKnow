package tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 关系映射中间 Response VO 对象 ext_relation_mapping_middle
 *
 * @author qknow
 * @date 2025-12-16
 */
@Schema(description = "关系映射中间 Response VO")
@Data
public class ExtRelationMappingMiddleRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "关系表id")
    @Schema(description = "关系表id", example = "")
    private Long relationId;

    @Excel(name = "中间表名称")
    @Schema(description = "中间表名称", example = "")
    private String tableName;

    @Excel(name = "关联源表字段")
    @Schema(description = "关联源表字段", example = "")
    private String tableField;

    @Excel(name = "关联目标表字段")
    @Schema(description = "关联目标表字段", example = "")
    private String relationField;

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
