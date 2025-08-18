package tech.qiantong.qknow.module.ext.dal.dataobject.extraction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import tech.qiantong.qknow.neo4j.domain.BaseNeo4jEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Node("ExtExtraction")
public class ExtExtractionDO extends BaseNeo4jEntity {
    @Id
    @GeneratedValue
    Long id;
    private String head;
    @Property(name = "taskId")
    private Long taskId;
    private Integer docId;
    private Integer paragraphIndex;
    private String tail;
    private String relation;
    private String confidence;
    private String workspaceId;
}
