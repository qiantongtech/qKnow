package tech.qiantong.qknow.module.ext.api.extration.dto;

import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

@Data
public class ExtExtractionDTO extends BaseEntity {
    private String text;
    private Integer paragraphIndex;
    private Long taskId;
    private Long docId;
    private Long workspaceId;
    private String head;
}
