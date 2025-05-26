package tech.qiantong.qknow.module.ext.service.extUnstructTask;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;

/**
 * 非结构化抽取任务Service接口
 *
 * @author qknow
 * @date 2025-02-18
 */
public interface IExtUnstructTaskService extends IService<ExtUnstructTaskDO> {

    public void consumeQueue();

    public AjaxResult executeExtraction(ExtUnstructTaskSaveReqVO createReqVO);

    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO);

    /**
     * 获得非结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 非结构化抽取任务分页列表
     */
    PageResult<ExtUnstructTaskDO> getExtUnstructTaskPage(ExtUnstructTaskPageReqVO pageReqVO);

    /**
     * 创建非结构化抽取任务
     *
     * @param createReqVO 非结构化抽取任务信息
     * @return 非结构化抽取任务编号
     */
    Long createExtUnstructTask(ExtUnstructTaskSaveReqVO createReqVO);

    /**
     * 更新非结构化抽取任务
     *
     * @param updateReqVO 非结构化抽取任务信息
     */
    int updateExtUnstructTask(ExtUnstructTaskSaveReqVO updateReqVO);

    /**
     * 删除非结构化抽取任务
     *
     * @param idList 非结构化抽取任务编号
     */
    int removeExtUnstructTask(Collection<Long> idList);

    /**
     * 获得非结构化抽取任务详情
     *
     * @param id 非结构化抽取任务编号
     * @return 非结构化抽取任务
     */
    ExtUnstructTaskDO getExtUnstructTaskById(Long id);

    /**
     * 发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult releaseByTaskId(ExtUnstructTaskDO unstructTaskDO);

    /**
     * 取消发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult cancelReleaseByTaskId(ExtUnstructTaskDO unstructTaskDO);

    /**
     * 获得全部非结构化抽取任务列表
     *
     * @return 非结构化抽取任务列表
     */
    List<ExtUnstructTaskDO> getExtUnstructTaskList();

    /**
     * 获得全部非结构化抽取任务 Map
     *
     * @return 非结构化抽取任务 Map
     */
    Map<Long, ExtUnstructTaskDO> getExtUnstructTaskMap();


    /**
     * 导入非结构化抽取任务数据
     *
     * @param importExcelList 非结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtUnstructTask(List<ExtUnstructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
