package tech.qiantong.qknow.module.ext.repository;

import org.neo4j.driver.types.Node;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtraction;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck.ExtUnstruckExtraction;
import tech.qiantong.qknow.neo4j.repository.BaseRepository;

import java.util.List;

public interface ExtUnstruckNeo4jRepository extends BaseRepository<ExtUnstruckExtraction, Long> {

}
