package tech.qiantong.qknow.module.ext.repository;

import org.neo4j.driver.types.Node;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtraction;
import tech.qiantong.qknow.neo4j.repository.BaseRepository;

import java.util.List;

public interface ExtNeo4jRepository extends BaseRepository<ExtExtraction, Long> {

    /**
     * 根据任务id删除节点及其关系 结构化
     *
     * @param taskId
     * @return
     */
    @Query("MATCH (a:ExtUnStruck {dynamic_properties_task_id: $taskId}) " +
            "DETACH DELETE a")
    void deleteExtUnStruck(@Param("taskId") Long taskId);


    /**
     * 根据任务id删除节点及其关系 非结构化
     *
     * @param taskId
     * @return
     */
    @Query("MATCH (a:ExtStruck {dynamic_properties_task_id: $taskId}) " +
            "DETACH DELETE a")
    void deleteExtStruck(@Param("taskId") Long taskId);
}
