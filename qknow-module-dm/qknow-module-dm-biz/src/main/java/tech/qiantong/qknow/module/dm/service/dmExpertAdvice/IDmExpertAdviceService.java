package tech.qiantong.qknow.module.dm.service.dmExpertAdvice;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceSaveReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdvicePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceRespVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice.DmExpertAdviceDO;
/**
 * 专家经验Service接口
 *
 * @author qknow
 * @date 2025-02-20
 */
public interface IDmExpertAdviceService extends IService<DmExpertAdviceDO> {

    /**
     * 获得专家经验分页列表
     *
     * @param pageReqVO 分页请求
     * @return 专家经验分页列表
     */
    PageResult<DmExpertAdviceDO> getDmExpertAdvicePage(DmExpertAdvicePageReqVO pageReqVO);

    /**
     * 创建专家经验
     *
     * @param createReqVO 专家经验信息
     * @return 专家经验编号
     */
    Long createDmExpertAdvice(DmExpertAdviceSaveReqVO createReqVO);

    /**
     * 更新专家经验
     *
     * @param updateReqVO 专家经验信息
     */
    int updateDmExpertAdvice(DmExpertAdviceSaveReqVO updateReqVO);

    /**
     * 删除专家经验
     *
     * @param idList 专家经验编号
     */
    int removeDmExpertAdvice(Collection<Long> idList);

    /**
     * 获得专家经验详情
     *
     * @param id 专家经验编号
     * @return 专家经验
     */
    DmExpertAdviceDO getDmExpertAdviceById(Long id);

    /**
     * 获得全部专家经验列表
     *
     * @return 专家经验列表
     */
    List<DmExpertAdviceDO> getDmExpertAdviceList();

    /**
     * 获得全部专家经验 Map
     *
     * @return 专家经验 Map
     */
    Map<Long, DmExpertAdviceDO> getDmExpertAdviceMap();


    /**
     * 导入专家经验数据
     *
     * @param importExcelList 专家经验数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmExpertAdvice(List<DmExpertAdviceRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
