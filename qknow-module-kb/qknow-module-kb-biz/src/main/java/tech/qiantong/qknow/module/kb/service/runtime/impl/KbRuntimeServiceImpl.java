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

package tech.qiantong.qknow.module.kb.service.runtime.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimePageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeDO;
import tech.qiantong.qknow.module.kb.dal.mapper.runtime.KbRuntimeMapper;
import tech.qiantong.qknow.module.kb.service.flow.bo.KbFlowBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.NodeRunResultBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.RuntimeContextBO;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeNodeService;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeService;

import java.util.Collection;

/**
 * bot运行Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
public class KbRuntimeServiceImpl extends ServiceImpl<KbRuntimeMapper, KbRuntimeDO> implements IKbRuntimeService {
    @Resource
    private IKbRuntimeNodeService nodeService;

    @Override
    public PageResult<KbRuntimeDO> getKbRuntimePage(KbRuntimePageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    public int removeKbRuntime(Collection<Long> idList) {
        // 批量删除bot运行
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KbRuntimeDO getKbRuntimeById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 创建运行时上下文
     *
     * @param flowBO 流程的 bo 对象
     * @param input  输入参数
     * @return 运行时上下文
     */
    @Override
    public RuntimeContextBO createRuntimeContext(KbFlowBO flowBO, JSONObject input) {
        KbRuntimeDO runtimeDO = new KbRuntimeDO();
        runtimeDO.setWorkspaceId(flowBO.getWorkspaceId());
        runtimeDO.setBotId(flowBO.getBotId());
        runtimeDO.setInput(input.toJSONString());
        runtimeDO.setStatus(RuntimeStatusEnums.RUNNING.getCode());

        return new RuntimeContextBO(runtimeDO, input);
    }

    /**
     * 创建并保存运行时上下文
     *
     * @param flowBO 流程的 bo 对象
     * @param input  输入参数
     * @return 运行时上下文
     */
    @Override
    public RuntimeContextBO createSaveRuntimeContext(KbFlowBO flowBO, JSONObject input) {
        KbRuntimeDO runtimeDO = new KbRuntimeDO();
        runtimeDO.setWorkspaceId(flowBO.getWorkspaceId());
        runtimeDO.setBotId(flowBO.getBotId());
        runtimeDO.setInput(input.toJSONString());
        runtimeDO.setStatus(RuntimeStatusEnums.RUNNING.getCode());
        baseMapper.insert(runtimeDO);

        return new RuntimeContextBO(runtimeDO, input);
    }

    /**
     * 保存运行成功的记录
     *
     * @param runtimeDO 运行时记录
     */
    @Override
    public void saveRunSuccess(KbRuntimeDO runtimeDO) {
        runtimeDO.setStatus(RuntimeStatusEnums.SUCCESS.getCode());
        // 将创建时间转成毫秒的时间戳
        long time = runtimeDO.getCreateTime().getTime();
        runtimeDO.setRuntime(System.currentTimeMillis() - time);
        baseMapper.saveRunSuccess(runtimeDO);
    }

    /**
     * 保存运行失败的记录
     *
     * @param runtimeDO 运行时记录
     */
    @Override
    public void saveRunError(KbRuntimeDO runtimeDO) {
        runtimeDO.setStatus(RuntimeStatusEnums.ERROR.getCode());
        // 将创建时间转成毫秒的时间戳
        long time = runtimeDO.getCreateTime().getTime();
        runtimeDO.setRuntime(System.currentTimeMillis() - time);
        baseMapper.updateById(runtimeDO);
    }

    /**
     * 保存节点运行状态
     *
     * @param nodeResult     节点执行结果
     * @param runtimeContext 流程的运行上下文
     */
    @Override
    public void saveRuntimeNode(NodeRunResultBO nodeResult, RuntimeContextBO runtimeContext) {
        KbRuntimeNodeDO runtimeNodeDO = new KbRuntimeNodeDO();
        runtimeNodeDO.setWorkspaceId(runtimeContext.getRuntimeDO().getWorkspaceId());
        runtimeNodeDO.setRuntimeId(runtimeContext.getRuntimeDO().getId());
        runtimeNodeDO.setNodeUuid(nodeResult.getNodeUuid());
        runtimeNodeDO.setStep(nodeResult.getStep());
        runtimeNodeDO.setInput(JSONObject.toJSONString(nodeResult.getInput()));
        runtimeNodeDO.setOutput(JSONObject.toJSONString(nodeResult.getOutput()));
        runtimeNodeDO.setStatus(nodeResult.getStatus());
        nodeService.save(runtimeNodeDO);
    }
}
