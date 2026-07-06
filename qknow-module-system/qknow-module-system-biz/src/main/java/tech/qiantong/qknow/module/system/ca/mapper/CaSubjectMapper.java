/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.system.ca.mapper;

import tech.qiantong.qknow.module.system.ca.domain.CaSubject;

import java.util.List;

/**
 * 主体管理Mapper接口
 *
 * @author qknow
 * @date 2024-08-18
 */
public interface CaSubjectMapper
{
    /**
     * 查询主体管理
     *
     * @param id 主体管理主键
     * @return 主体管理
     */
    public CaSubject selectCaSubjectById(Long id);

    /**
     * 查询主体管理列表
     *
     * @param caSubject 主体管理
     * @return 主体管理集合
     */
    public List<CaSubject> selectCaSubjectList(CaSubject caSubject);

    /**
     * 新增主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    public int insertCaSubject(CaSubject caSubject);

    /**
     * 修改主体管理
     *
     * @param caSubject 主体管理
     * @return 结果
     */
    public int updateCaSubject(CaSubject caSubject);

    /**
     * 删除主体管理
     *
     * @param id 主体管理主键
     * @return 结果
     */
    public int deleteCaSubjectById(Long id);

    /**
     * 批量删除主体管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCaSubjectByIds(Long[] ids);
}
