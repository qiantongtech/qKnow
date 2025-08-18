package tech.qiantong.qknow.module.app.relationship;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import tech.qiantong.qknow.module.app.entity.AppGraphEntity;

@Data
@RelationshipProperties
public class AppGraphEntityRelationship {
    @Id
    @GeneratedValue
    Long id;

    @TargetNode
    private AppGraphEntity endNode;
}
