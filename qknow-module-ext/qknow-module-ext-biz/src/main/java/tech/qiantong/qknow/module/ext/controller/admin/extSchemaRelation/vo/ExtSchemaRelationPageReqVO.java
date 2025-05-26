package tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 关系配置 Request VO 对象 ext_schema_relation
 *
 * @author qknow
 * @date 2025-02-18
 */
@Schema(description = "关系配置 Request VO")
@Data
public class ExtSchemaRelationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "起点概念id", example = "")
    private Long startSchemaId;

    @Schema(description = "关系", example = "")
    private String relation;

    @Schema(description = "终点概念id", example = "")
    private Long endSchemaId;

    @Schema(description = "是否可逆", example = "")
    private Integer inverseFlag;

    //用于概念配置删除时传值，分别查询startSchemaId,endSchemaId有没有使用到
    @TableField(exist = false)
    private Long startEndId;






}
