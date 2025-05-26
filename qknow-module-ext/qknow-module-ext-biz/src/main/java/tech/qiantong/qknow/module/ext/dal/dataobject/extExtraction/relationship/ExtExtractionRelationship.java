package tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.relationship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtraction;

@Data
@RelationshipProperties
public class ExtExtractionRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private ExtExtraction endNode;
}
