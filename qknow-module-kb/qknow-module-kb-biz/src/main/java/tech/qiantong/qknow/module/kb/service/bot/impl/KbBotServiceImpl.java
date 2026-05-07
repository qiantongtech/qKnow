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

package tech.qiantong.qknow.module.kb.service.bot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotSaveReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;
import tech.qiantong.qknow.module.kb.dal.mapper.bot.KbBotMapper;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowEdgeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowNodeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowService;

import java.util.Collection;
import java.util.Objects;

/**
 * bot 管理Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbBotServiceImpl extends ServiceImpl<KbBotMapper, KbBotDO> implements IKbBotService {

    @Resource
    private IKbFlowService flowService;

    @Resource
    private IKbFlowNodeService flowNodeService;
    @Resource
    private IKbFlowEdgeService flowEdgeService;

    /**
     * 获得bot 管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return bot 管理分页列表
     */
    @Override
    public PageResult<KbBotDO> getKbBotPage(KbBotPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    /**
     * 创建bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    @Override
    public Long createKbBot(KbBotSaveReqVO createReqVO) {
        KbBotDO dictType = BeanUtils.toBean(createReqVO, KbBotDO.class);
        baseMapper.insert(dictType);
        return dictType.getId();
    }

    /**
     * 更新bot 管理
     *
     * @param updateReqVO bot 管理信息
     */
    @Override
    public int updateKbBot(KbBotSaveReqVO updateReqVO) {
        // 相关校验

        // 更新bot 管理
        KbBotDO updateObj = BeanUtils.toBean(updateReqVO, KbBotDO.class);
        return baseMapper.updateById(updateObj);
    }

    /**
     * 删除bot
     *
     * @param idList botId 集合
     */
    @Override
    public int removeKbBot(Collection<Long> idList) {
        int result = 0;
        // 批量删除bot 管理
        for (Long id : idList) {
            result += removeKbBot(id);
        }
        return result;
    }

    /**
     * 获得 bot
     *
     * @param id botId
     * @return bot
     */
    @Override
    public KbBotDO getKbBotById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 删除 bot
     *
     * @param id botId
     */
    private int removeKbBot(Long id) {
        // 首先根据id 获取 bot
        KbBotDO kbBotDO = baseMapper.selectById(id);
        if (kbBotDO.getBuiltinFlag().equals(1)){
            throw new ServiceException("内置 Bot，不允许删除");
        }
        // 如果是 chatFlow 或者 workflow 需要进一步删除流程数据
        if (Objects.equals(kbBotDO.getType(), BotTypeEnums.WORK_FLOW.getCode())
                || Objects.equals(kbBotDO.getType(), BotTypeEnums.CHAT_FLOW.getCode())) {
            flowService.deleteByBotId(kbBotDO.getId());
        }
        return baseMapper.deleteById(id);
    }

    /**
     * 复制bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    @Override
    public Long copyKbBot(KbBotSaveReqVO createReqVO) {
        // 1. 获取【源Bot ID】
        Long sourceBotId = createReqVO.getId();

        // 2. 构建新Bot
        KbBotDO newBot = BeanUtils.toBean(createReqVO, KbBotDO.class);
        newBot.setId(null); // 关键：新Bot必须清空ID，让数据库自增
        newBot.setBuiltinFlag(0);
        baseMapper.insert(newBot);
        Long newBotId = newBot.getId(); // 拿到新生成的ID

        // 3. 查询【源Bot】的流程节点、连线
        KbFlowVO flowVO = new KbFlowVO();
        flowVO.setNodes(flowNodeService.flowVOByBotId(sourceBotId));
        flowVO.setEdges(flowEdgeService.flowVOByBotId(sourceBotId));

        // 4. 把流程绑定到【新Bot】
        flowVO.setBotId(newBotId);
        flowVO.setWorkspaceId(newBot.getWorkspaceId());
        // 5. 批量保存新流程
        flowNodeService.submitBatch(flowVO);
        flowEdgeService.submitBatch(flowVO);

        return newBotId;
    }


}
