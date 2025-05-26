package tech.qiantong.qknow.module.ext.service.extStructTask;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTask;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;

/**
 * 结构化抽取任务Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtStructTaskService extends IService<ExtStructTaskDO> {
    public void consumeQueue();
    /**
     * 执行抽取
     *
     * @param extStructTask
     * @return
     */
    public AjaxResult executeExtraction(ExtStructTaskPageReqVO extStructTask);

    public AjaxResult selectByTaskId(Long taskId, Integer extractType);

    public AjaxResult getAttributeInformation(List<String> list);

    /**
     * 发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult releaseByTaskId(ExtStructTaskDO structTaskDO);

    /**
     * 取消发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult cancelReleaseByTaskId(ExtStructTaskDO structTaskDO);

    /**
     * 新建结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    public AjaxResult addDataMapping(ExtStructTask extStructTask);

    public AjaxResult updateDataMapping(ExtStructTask extStructTask);

    /**
     * 获得结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 结构化抽取任务分页列表
     */
    PageResult<ExtStructTaskDO> getExtStructTaskPage(ExtStructTaskPageReqVO pageReqVO);

    /**
     * 创建结构化抽取任务
     *
     * @param createReqVO 结构化抽取任务信息
     * @return 结构化抽取任务编号
     */
    Long createExtStructTask(ExtStructTaskSaveReqVO createReqVO);

    /**
     * 更新结构化抽取任务
     *
     * @param updateReqVO 结构化抽取任务信息
     */
    int updateExtStructTask(ExtStructTaskSaveReqVO updateReqVO);

    /**
     * 删除结构化抽取任务
     *
     * @param idList 结构化抽取任务编号
     */
    int removeExtStructTask(Collection<Long> idList);

    /**
     * 获得结构化抽取任务详情
     *
     * @param id 结构化抽取任务编号
     * @return 结构化抽取任务
     */
    ExtStructTaskDO getExtStructTaskById(Long id);

    AjaxResult getInfo(Long id);

    /**
     * 获得全部结构化抽取任务列表
     *
     * @return 结构化抽取任务列表
     */
    List<ExtStructTaskDO> getExtStructTaskList();

    /**
     * 获得全部结构化抽取任务 Map
     *
     * @return 结构化抽取任务 Map
     */
    ConcurrentHashMap<Long, ExtStructTaskDO> getExtStructTaskMap();


    /**
     * 导入结构化抽取任务数据
     *
     * @param importExcelList 结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importExtStructTask(List<ExtStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
