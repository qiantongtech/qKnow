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

package tech.qiantong.qknow.module.kb.service.flow;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;

import java.util.List;

/**
 * bot流程节点Service接口
 *
 * @author qknow
 * @date 2026-03-18
 */
public interface IKbFlowService {
    /**
     * 根据 BotId 查询流程
     *
     * @param botId botId
     * @return 流程对象
     */
    KbFlowVO queryFlow(Long botId);

    /**
     * 根据 BotId 删除流程
     *
     * @param botId botId
     */
    void deleteByBotId(Long botId);

    /**
     * 创建流程
     *
     * @param flowVO 流程对象
     * @return 操作是否成功
     */
    Boolean submitFlow(KbFlowVO flowVO);

    /**
     * 测试执行流程
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    Flux<CommonResult<String>> testExecuteFlow(KbFlowVO flowVO, JSONObject input);

    /**
     * 测试运行 chatFlow
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    Flux<CommonResult<String>> testExecuteChatFlow(KbFlowVO flowVO, JSONObject input, JSONArray messageList);

    /**
     * 正式执行流程
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    KbRuntimeRespVO executeFlow(KbFlowVO flowVO, JSONObject input);

    /**
     * 正式执行流程(流式输出)
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    Flux<CommonResult<String>> executeFlowStream(KbFlowVO flowVO, JSONObject input);

    /**
     * 正式运行 chatFlow
     *
     * @param flowVO      流程对象
     * @param input       输入参数
     * @param messageList 消息列表
     * @return 执行结果
     */
    Flux<CommonResult<String>> executeChatFlow(KbFlowVO flowVO, JSONObject input, List<Message> messageList);
}
