package tech.qiantong.qknow.module.kb.api.flow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class KbRuntimeRespDTO {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "botId", example = "")
    private Long botId;

    @Schema(description = "输入问题", example = "")
    private String input;

    @Schema(description = "输出结果", example = "")
    private String output;

    @Schema(description = "运行状态", example = "")
    private Integer status;

    @Schema(description = "运行时间(毫秒)", example = "")
    private Long runtime;

    @Schema(description = "创建人", example = "")
    private String createBy;

    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Schema(description = "备注", example = "")
    private String remark;
}
