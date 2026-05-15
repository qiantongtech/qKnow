/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

package tech.qiantong.qknow.module.ext.service.extTaskLog.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extTaskLog.ExtTaskLogDetailMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extTaskLog.ExtTaskLogMapper;
import tech.qiantong.qknow.module.ext.extEnum.ExtLogStatusEnum;
import tech.qiantong.qknow.module.ext.extEnum.ExtTaskTypeEnum;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogService;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 抽取任务执行日志Service业务层处理
 *
 * @author qknow
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtTaskLogServiceImpl extends ServiceImpl<ExtTaskLogMapper, ExtTaskLogDO> implements IExtTaskLogService {
    @Resource
    private ExtTaskLogMapper extTaskLogMapper;
    @Resource
    private ExtTaskLogDetailMapper logDetailMapper;

    @Override
    public PageResult<ExtTaskLogDO> getExtTaskLogPage(ExtTaskLogPageReqVO pageReqVO) {
        return extTaskLogMapper.selectPage(pageReqVO);
    }

    /**
     * 获得抽取任务执行日志分页列表
     *
     * @param detailPageReqVO 分页请求
     * @return 抽取任务执行日志分页列表
     */
    @Override
    public PageResult<ExtTaskLogDetailDO> getLogDetailPage(ExtTaskLogDetailPageReqVO detailPageReqVO) {
        return logDetailMapper.selectPage(detailPageReqVO);
    }

    @Override
    public Long createExtTaskLog(ExtTaskLogSaveReqVO createReqVO) {
        ExtTaskLogDO dictType = BeanUtils.toBean(createReqVO, ExtTaskLogDO.class);
        extTaskLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public ExtTaskLogDO getExtTaskLogById(Long id) {
        return extTaskLogMapper.selectById(id);
    }

    /**
     * 开始执行日志
     *
     * @param taskId   执行的任务id
     * @param taskName 任务名称
     * @param taskType 任务类型
     * @return 日志id
     */
    @Override
    public Long startInvoke(Long workSpaceId, Long taskId, String taskName, ExtTaskTypeEnum taskType) {
        ExtTaskLogDO entity = new ExtTaskLogDO();
        entity.setWorkspaceId(workSpaceId);
        entity.setTaskId(taskId);
        entity.setTaskName(taskName);
        entity.setTaskType(taskType.getValue());
        entity.setStatus(ExtLogStatusEnum.SUCCESS.getValue());// 默认是成功的状态
        entity.setStartTime(new Date());
        extTaskLogMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 记录日志步骤
     *
     * @param logId 日志id
     * @param step  执行步骤
     */
    @Override
    public void recordStep(Long logId, String step) {
        if (step.length() > 2000) {
            step = step.substring(0, 2000);
        }
        ExtTaskLogDO taskLogDO = extTaskLogMapper.selectById(logId);
        if (Objects.isNull(taskLogDO)) {
            throw new ServiceException("获取日志对象出错");
        }
        ExtTaskLogDetailDO detailEntity = new ExtTaskLogDetailDO();
        detailEntity.setLogId(logId);
        detailEntity.setWorkspaceId(taskLogDO.getWorkspaceId());
        detailEntity.setStep(step);
        logDetailMapper.insert(detailEntity);
        // 同时修改下结束事件
        updateEndTime(logId);
    }

    /**
     * 执行结束
     *
     * @param logId    日志id
     * @param status   日志的执行状态
     * @param errorMsg 错误日志
     */
    @Override
    public void endInvoke(Long logId, ExtLogStatusEnum status, String errorMsg) {
        if (Objects.isNull(errorMsg)) {
            errorMsg = "";
        }
        if (errorMsg.length() > 2000) {
            errorMsg = errorMsg.substring(0, 2000);
        }
        LambdaUpdateWrapper<ExtTaskLogDO> updateWrapper = Wrappers.<ExtTaskLogDO>lambdaUpdate()
                .set(ExtTaskLogDO::getStatus, status.getValue())
                .set(ExtTaskLogDO::getErrorMsg, errorMsg)
                .set(ExtTaskLogDO::getEndTime, new Date())
                .eq(ExtTaskLogDO::getId, logId);
        super.update(updateWrapper);
    }

    @Override
    public Integer removeExtTaskById(List<Long> list) {
        return extTaskLogMapper.deleteByIds(list);
    }

    /**
     * 更新日志的结束时间
     *
     * @param logId 日志id
     */
    private void updateEndTime(Long logId) {
        LambdaUpdateWrapper<ExtTaskLogDO> updateWrapper = Wrappers.<ExtTaskLogDO>lambdaUpdate()
                .set(ExtTaskLogDO::getEndTime, new Date())
                .eq(ExtTaskLogDO::getId, logId);
        super.update(updateWrapper);
    }
}
