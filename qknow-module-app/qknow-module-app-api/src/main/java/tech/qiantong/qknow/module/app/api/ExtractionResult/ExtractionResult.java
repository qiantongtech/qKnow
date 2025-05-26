package tech.qiantong.qknow.module.app.api.ExtractionResult;

import lombok.Data;

@Data
public class ExtractionResult {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String docId;

    private String name;

    private String paragraphIndex;

    private String taskId;
}
