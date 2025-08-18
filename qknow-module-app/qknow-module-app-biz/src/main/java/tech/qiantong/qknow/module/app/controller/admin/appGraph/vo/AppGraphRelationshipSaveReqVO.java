package tech.qiantong.qknow.module.app.controller.admin.appGraph.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 三元组 Request VO ext_schema
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "三元组 Response VO")
@Data
public class AppGraphRelationshipSaveReqVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "实体id1")
    @NotBlank(message = "实体不能为空")
    private Long entityId1;

    @Schema(description = "关系名称")
    @NotBlank(message = "关系不能为空")
    private String relationshipType;

    @Schema(description = "实体id2")
    @NotBlank(message = "实体不能为空")
    private Long entityId2;

}
