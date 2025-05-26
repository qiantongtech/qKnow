package tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck.relationship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck.ExtUnstruckExtraction;

@Data
@RelationshipProperties
public class ExtUnstruckExtractionRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private ExtUnstruckExtraction endNode;
}
