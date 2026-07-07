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

package tech.qiantong.qknow.module.kb.service.tool.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.spring.SpringUtils;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolCategorySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolCategoryDO;
import tech.qiantong.qknow.module.kb.dal.mapper.tool.KbToolCategoryMapper;
import tech.qiantong.qknow.module.kb.domain.TreeSelectsTool;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolCategoryService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
public class KbToolCategoryServiceImpl extends ServiceImpl<KbToolCategoryMapper, KbToolCategoryDO> implements IKbToolCategoryService {

    @Override
    public Long createToolCategory(KbToolCategorySaveReqVO createReqVO) {
        //判断祖级列表字段是否为空串
        if (StringUtils.isEmpty(createReqVO.getAncestors())) {
            createReqVO.setAncestors(String.valueOf(createReqVO.getParentId()));
        }
        //获取到父级id的详细数据
        KbToolCategoryDO categoryById = baseMapper.selectToolCategoryById(createReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            createReqVO.setAncestors(categoryById.getAncestors() + "," + createReqVO.getParentId());
        }
        //插入知识分类
        KbToolCategoryDO createObj = BeanUtils.toBean(createReqVO, KbToolCategoryDO.class);
        baseMapper.insert(createObj);
        return createObj.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateToolCategory(KbToolCategorySaveReqVO updateReqVO) {
        // 获取当前要更新的分类原始数据
        KbToolCategoryDO originalCategory = baseMapper.selectById(updateReqVO.getId());
        if (originalCategory == null) {
            throw new ServiceException("要更新的知识分类不存在");
        }

        //获取到父级id的详细数据
        KbToolCategoryDO categoryById = baseMapper.selectToolCategoryById(updateReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            updateReqVO.setAncestors(categoryById.getAncestors() + "," + updateReqVO.getParentId());
        }
        // 更新知识分类
        KbToolCategoryDO updateObj = BeanUtils.toBean(updateReqVO, KbToolCategoryDO.class);

        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeToolCategory(Collection<Long> idList) {
        // 批量删除知识分类
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KbToolCategoryDO getToolCategoryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<KbToolCategoryDO> getToolCategoryAllList(KbToolCategoryDO kmcCategoryDO) {
        KbToolCategoryDO dictType = BeanUtils.toBean(kmcCategoryDO, KbToolCategoryDO.class);
        return baseMapper.getToolCategoryAllList(dictType);
    }

    @Override
    public List<TreeSelectsTool> selectCategoryTreeList(KbToolCategoryDO kmcCategoryDO) {
        kmcCategoryDO.setDelFlag(false);
        List<KbToolCategoryDO> list = SpringUtils.getAopProxy(this).getToolCategoryAllList(kmcCategoryDO);
        return buildToolCategoryTreeSelect(list);
    }

    @Override
    public List<TreeSelectsTool> buildToolCategoryTreeSelect(List<KbToolCategoryDO> kmcCategoryResp) {
        List<KbToolCategoryDO> KmcCategoryTrees = buildToolCategoryTree(kmcCategoryResp);
        return KmcCategoryTrees.stream().map(TreeSelectsTool::new).collect(Collectors.toList());
    }

    public List<KbToolCategoryDO> buildToolCategoryTree(List<KbToolCategoryDO> KmcCategorys) {
        List<KbToolCategoryDO> returnList = new ArrayList<KbToolCategoryDO>();
        List<Long> tempList = KmcCategorys.stream().map(KbToolCategoryDO::getId).toList();
        for (KbToolCategoryDO type : KmcCategorys) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(type.getParentId())) {
                recursionFn(KmcCategorys, type);
                returnList.add(type);
            }
        }
        if (returnList.isEmpty()) {
            returnList = KmcCategorys;
        }
        return returnList;
    }

    /**
     * 获取工具分类树列表
     *
     * @return 工具分类树列表
     */
    @Override
    public List<Map<String, Object>> getTreeList() {
        List<KbToolCategoryDO> allCategories = super.list();
        Map<Long, List<KbToolCategoryDO>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != 0) // 过滤掉顶级分类（parentId=0的）
                .collect(Collectors.groupingBy(KbToolCategoryDO::getParentId)); // 按parentId分组
        List<Map<String, Object>> result = Lists.newArrayList();
        allCategories.stream()
                .filter(c -> c.getParentId() == 0) // 筛选顶级分类
                .forEach(topCategory -> {
                    // 创建当前顶级分类的节点
                    Map<String, Object> categoryNode = Maps.newLinkedHashMap();
                    categoryNode.put("id", topCategory.getId()); // 分类ID
                    categoryNode.put("name", topCategory.getName()); // 分类名称
                    List<Map<String, Object>> children = buildCategoryTree(
                            topCategory.getId(), // 当前分类ID作为父ID
                            childrenMap // 子分类分组数据
                    );
                    categoryNode.put("children", children);// 设置子分类列表
                    result.add(categoryNode);
                });

        return result;
    }

    /**
     * 获取工具分类Map
     *
     * @return 工具分类Map
     */
    @Override
    public Map<Long, String> getCategoryMap() {
        List<KbToolCategoryDO> list = super.list();
        return list.stream().collect(Collectors.toMap(KbToolCategoryDO::getId, KbToolCategoryDO::getName));
    }

    /**
     * 递归构建分类树结构并累计文档总数
     *
     * @param parentId    当前父分类ID
     * @param childrenMap 所有子分类的分组映射（按parentId分组）
     * @return 当前父分类下的子分类树结构
     */
    private List<Map<String, Object>> buildCategoryTree(Long parentId, Map<Long, List<KbToolCategoryDO>> childrenMap) {
        // 如果当前父ID没有子分类，返回空列表
        if (!childrenMap.containsKey(parentId)) {
            return Lists.newArrayList();
        }
        return childrenMap.get(parentId).stream()
                .map(child -> {
                    // 创建当前子分类节点
                    Map<String, Object> childNode = Maps.newHashMap();
                    childNode.put("id", child.getId()); // 分类ID
                    childNode.put("name", child.getName()); // 分类名称
                    List<Map<String, Object>> grandchildren = buildCategoryTree(
                            child.getId(), // 当前分类作为父分类
                            childrenMap
                    );
                    childNode.put("children", grandchildren);// 设置子分类列表

                    return childNode;
                })
                .collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<KbToolCategoryDO> list, KbToolCategoryDO t) {
        return getChildList(list, t).size() > 0;
    }


    private void recursionFn(List<KbToolCategoryDO> list, KbToolCategoryDO t) {
        // 得到子节点列表
        List<KbToolCategoryDO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (KbToolCategoryDO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    private List<KbToolCategoryDO> getChildList(List<KbToolCategoryDO> list, KbToolCategoryDO t) {
        List<KbToolCategoryDO> tlist = new ArrayList<>();
        for (KbToolCategoryDO entity : list) {
            if (StringUtils.isNotNull(entity.getParentId())
                    && entity.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(entity);
            }
        }
        return tlist;
    }
}
