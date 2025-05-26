package tech.qiantong.qknow.neo4j.domain.relationship;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;

@Data
@RelationshipProperties
public class DynamicEntityRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private DynamicEntity endNode;
}
