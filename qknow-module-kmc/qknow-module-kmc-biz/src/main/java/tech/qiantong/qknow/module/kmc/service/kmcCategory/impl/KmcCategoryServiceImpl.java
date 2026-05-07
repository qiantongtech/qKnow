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

package tech.qiantong.qknow.module.kmc.service.kmcCategory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.spring.SpringUtils;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategorySaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcCategory.KmcCategoryMapper;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识分类Service业务层处理
 *
 * @author qknow
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcCategoryServiceImpl extends ServiceImpl<KmcCategoryMapper, KmcCategoryDO> implements IKmcCategoryService {
    @Resource
    private KmcCategoryMapper kmcCategoryMapper;

    @Override
    public PageResult<KmcCategoryDO> getKmcCategoryPage(KmcCategoryPageReqVO pageReqVO) {
        return kmcCategoryMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKmcCategory(KmcCategorySaveReqVO createReqVO) {
        //判断祖级列表字段是否为空串
        if (StringUtils.isEmpty(createReqVO.getAncestors())) {
            createReqVO.setAncestors(String.valueOf(createReqVO.getParentId()));
        }
        //获取到父级id的详细数据
        KmcCategoryDO categoryById = kmcCategoryMapper.selectKmcCategoryById(createReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            createReqVO.setAncestors(categoryById.getAncestors() + "," + createReqVO.getParentId());
        }
        //插入知识分类
        KmcCategoryDO createObj = BeanUtils.toBean(createReqVO, KmcCategoryDO.class);
        kmcCategoryMapper.insert(createObj);
        return createObj.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateKmcCategory(KmcCategorySaveReqVO updateReqVO) {
        // 获取当前要更新的分类原始数据
        KmcCategoryDO originalCategory = kmcCategoryMapper.selectById(updateReqVO.getId());
        if (originalCategory == null) {
            throw new ServiceException("要更新的知识分类不存在");
        }

        //获取到父级id的详细数据
        KmcCategoryDO categoryById = kmcCategoryMapper.selectKmcCategoryById(updateReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            updateReqVO.setAncestors(categoryById.getAncestors() + "," + updateReqVO.getParentId());
        }
        // 更新知识分类
        KmcCategoryDO updateObj = BeanUtils.toBean(updateReqVO, KmcCategoryDO.class);

        // 检查knowledgeBaseId是否发生变化，如果变化则需要更新所有子分类
        if (originalCategory.getKnowledgeBaseId() != null &&
                !originalCategory.getKnowledgeBaseId().equals(updateObj.getKnowledgeBaseId())) {
            updateChildrenKnowledgeBaseId(updateReqVO.getId(), updateObj.getKnowledgeBaseId());
        }

        return kmcCategoryMapper.updateById(updateObj);
    }


    /**
     * 更新指定分类的所有子分类的knowledgeBaseId
     * @param parentId 父分类ID
     * @param knowledgeBaseId 新的知识库ID
     */
    private void updateChildrenKnowledgeBaseId(Long parentId, Long knowledgeBaseId) {
        // 获取所有子分类
        List<KmcCategoryDO> children = kmcCategoryMapper.selectChildrenCategoryById(parentId);
        if (StringUtils.isNotEmpty(children)) {
            for (KmcCategoryDO child : children) {
                child.setKnowledgeBaseId(knowledgeBaseId);
            }
            kmcCategoryMapper.updateBatch(children);
        }
    }
    @Override
    public int removeKmcCategory(Collection<Long> idList) {
        // 批量删除知识分类
        return kmcCategoryMapper.deleteBatchIds(idList);
    }

    @Override
    public KmcCategoryDO getKmcCategoryById(Long id) {
        return kmcCategoryMapper.selectById(id);
    }

    @Override
    public List<KmcCategoryDO> getKmcCategoryList() {
        return kmcCategoryMapper.selectList();
    }

    @Override
    public Map<Long, KmcCategoryDO> getKmcCategoryMap() {
        List<KmcCategoryDO> kmcCategoryList = kmcCategoryMapper.selectList();
        return kmcCategoryList.stream()
                .collect(Collectors.toMap(
                        KmcCategoryDO::getId,
                        kmcCategoryDO -> kmcCategoryDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入知识分类数据
     *
     * @param importExcelList 知识分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importKmcCategory(List<KmcCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcCategoryRespVO respVO : importExcelList) {
            try {
                KmcCategoryDO kmcCategoryDO = BeanUtils.toBean(respVO, KmcCategoryDO.class);
                Long kmcCategoryId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcCategoryId != null) {
                        KmcCategoryDO existingKmcCategory = kmcCategoryMapper.selectById(kmcCategoryId);
                        if (existingKmcCategory != null) {
                            kmcCategoryMapper.updateById(kmcCategoryDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcCategoryId + " 的知识分类记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcCategoryId + " 的知识分类记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcCategoryDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcCategoryId);
                    KmcCategoryDO existingKmcCategory = kmcCategoryMapper.selectOne(queryWrapper);
                    if (existingKmcCategory == null) {
                        kmcCategoryMapper.insert(kmcCategoryDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcCategoryId + " 的知识分类记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcCategoryId + " 的知识分类记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }

    @Override
    public List<KmcCategoryDO> getKmcCategoryAllList(KmcCategoryDO kmcCategoryDO) {
        KmcCategoryDO dictType = BeanUtils.toBean(kmcCategoryDO, KmcCategoryDO.class);
        return kmcCategoryMapper.getKmcCategoryAllList(dictType);
    }

    @Override
    public List<TreeSelects> selectCategoryTreeList(KmcCategoryDO kmcCategoryDO) {
        kmcCategoryDO.setDelFlag(false);
        List<KmcCategoryDO> list = SpringUtils.getAopProxy(this).getKmcCategoryAllList(kmcCategoryDO);
        return buildKmcCategoryTreeSelect(list);
    }

    @Override
    public List<TreeSelects> buildKmcCategoryTreeSelect(List<KmcCategoryDO> kmcCategoryResp)
    {
        List<KmcCategoryDO> KmcCategoryTrees = buildKmcCategoryTree(kmcCategoryResp);
        return KmcCategoryTrees.stream().map(TreeSelects::new).collect(Collectors.toList());
    }

    public List<KmcCategoryDO> buildKmcCategoryTree(List<KmcCategoryDO> KmcCategorys)
    {
        List<KmcCategoryDO> returnList = new ArrayList<KmcCategoryDO>();
        List<Long> tempList = KmcCategorys.stream().map(KmcCategoryDO::getId).collect(Collectors.toList());
        for (KmcCategoryDO type : KmcCategorys)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(type.getParentId()))
            {
                recursionFn(KmcCategorys, type);
                returnList.add(type);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = KmcCategorys;
        }
        return returnList;
    }

    private void recursionFn(List<KmcCategoryDO> list, KmcCategoryDO t)
    {
        // 得到子节点列表
        List<KmcCategoryDO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (KmcCategoryDO tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    private List<KmcCategoryDO> getChildList(List<KmcCategoryDO> list, KmcCategoryDO t)
    {
        List<KmcCategoryDO> tlist = new ArrayList<KmcCategoryDO>();
        Iterator<KmcCategoryDO> it = list.iterator();
        while (it.hasNext())
        {
            KmcCategoryDO n = (KmcCategoryDO) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<KmcCategoryDO> list, KmcCategoryDO t)
    {
        return getChildList(list, t).size() > 0;
    }

}
