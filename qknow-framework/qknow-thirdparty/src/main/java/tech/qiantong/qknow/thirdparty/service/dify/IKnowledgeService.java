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

package tech.qiantong.qknow.thirdparty.service.dify;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.*;

import java.util.List;

/**
 * 灵桐-知识库Service接口
 *
 * @author qknow
 * @date 2025-02-19
 */
public interface IKnowledgeService {
    /**
     * 上次文档
     * @param createFile
     * @return
     */
    public CreateFileResult createByFile(CreateFile createFile, String datasetId, String apiKey);

    /**
     * 获取文件状态
     * @param batch
     * @return
     */
    public String getFileStatus(String batch, String datasetId, String apiKey);

    /**
     * 删除文件状态
     * @param documentId
     * @return
     */
    public Boolean deleteFileStatus(String documentId, String datasetId, String apiKey);
    /**
     * 检索文件
     * @param retrieve
     * @return
     */
    public List<RetrieveResult> findFileRetrieve(Retrieve retrieve, String datasetId, String apiKey);

    /**
     * 获取片段
     * @param documentId
     * @param datasetId
     * @param apiKey
     * @return 所以片段详细数据
     */
    public List<Segments> segments(String documentId, String datasetId, String apiKey);

    /**
     * 创建知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    public String createDatasets(Datasets datasets, String apiKey);

    /**
     * 修改知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    public String updateDatasets(Datasets datasets, String apiKey);

    /**
     * 删除知识库
     * @param datasetId
     * @param apiKey
     */
    public void deleteDatasets(String datasetId, String apiKey);

    /**
     * 获取文本嵌入模型
     * @param apiKey
     * @return JSONArray 返回格式参考dify文档,格式比较复杂，所以通过json格式返回
     */
    public JSONArray getTextEmbedding(String apiKey);

    /**
     * 获取Rerank
     * 隐藏接口，dify文档暂无，但是接口可用
     * @param apiKey
     * @return JSONArray 同getTextEmbedding
     */
    public JSONArray getRerank(String apiKey);


    /**
     * 删除父节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @return 删除成功返回true
     */
    public boolean removeRootSegment(String segmentId, String datasetId,String documentId, String apiKey);


    /**
     * 删除子节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @return 删除成功返回true
     */
    public boolean removeChildSegment(String segmentId, String datasetId,String documentId,String parentId, String apiKey);

    /**
     * 新增父节点分段信息
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param params 请求参数
     * @return 分段信息
     */
    public JSONObject addRootSegment(String datasetId, String documentId, String apiKey,  JSONObject params);


    /**
     * 新增子节点分段信息
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @param params 请求参数
     * @return 分段信息 addRootSegment
     */
    public JSONObject addChildSegment(String datasetId, String documentId, String parentId, String apiKey,  JSONObject params);


    /**
     * 编辑父级节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @return 分段信息
     */
    public JSONObject editRootSegment(String segmentId, String datasetId, String documentId , String apiKey,  JSONObject params);


    /**
     * 编辑子级节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @return 分段信息
     */
    public JSONObject editChildSegment(String segmentId, String datasetId, String documentId, String parentId, String apiKey,  JSONObject params);




    /**
     * 获取分段信息
     * @param datasetId 知识库id
     * @param documentId 文档id
     * @param apiKey apiKey
     * @return JSONArray 同getTextEmbedding
     */
    public JSONArray getSegments(String datasetId,String documentId, String apiKey);

}
