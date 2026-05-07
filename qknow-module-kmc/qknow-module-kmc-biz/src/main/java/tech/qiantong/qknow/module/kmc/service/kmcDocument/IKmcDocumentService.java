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

package tech.qiantong.qknow.module.kmc.service.kmcDocument;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentTrackReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 知识文件Service接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface IKmcDocumentService extends IService<KmcDocumentDO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO);

    List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids);

    /**
     * 创建知识文件
     *
     * @param createReqVO 知识文件信息
     * @return 知识文件编号
     */
    Integer createKmcDocument(KmcDocumentSaveReqVO createReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKmcDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KmcDocumentDO getKmcDocumentById(Long id);

    KmcDocumentDO getKmcDocumentByIdJoinSync(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KmcDocumentDO> getKmcDocumentList();

    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument);

    List<Map<String, Object>> getFileTypes(Long knowledgeBaseId);

    /**
     * 文件跟踪保存
     * @param documentTrackList
     * @return
     */
    Boolean trackCount(List<KmcDocumentTrackReqVO> documentTrackList, Long userId, String userName);

    /**
     * 获取该知识库下的第一个文档
     * @param knowledgeBaseId
     * @return
     */
    KmcDocumentDO getKnowledgeBaseOne(Long knowledgeBaseId);

    /**
     * 检查文件名称是否有重复的
     * @param id
     * @param fileNames
     * @param knowledgeBaseId
     * @return
     */
    String checkFileName(Long id, String fileNames, Long knowledgeBaseId);

    /**
     * 获取知识库下的文件数量
     * @param knowledgeBaseIdList
     * @return
     */
    Map<Long, Integer> getFileCount(List<Long> knowledgeBaseIdList);
}
