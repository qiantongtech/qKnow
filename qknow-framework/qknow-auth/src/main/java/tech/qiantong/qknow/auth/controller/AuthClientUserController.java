/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.auth.domain.AuthClientUser;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.TableDataInfo;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 应用和用户关联Controller
 *
 * @author qknow
 * @date 2024-08-31
 */
@RestController
@RequestMapping("/auth/user")
public class AuthClientUserController extends BaseController {
    @Autowired
    private IService<AuthClientUser> authClientUserService;

/**
 * 查询应用和用户关联列表
 */
@PreAuthorize("@ss.hasPermi('auth:user:list')")
@GetMapping("/list")
    public TableDataInfo list(AuthClientUser authClientUser) {
        startPage();
        QueryWrapper<AuthClientUser> queryWrapper = new QueryWrapper<>(authClientUser);
        List<AuthClientUser> list = authClientUserService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 导出应用和用户关联列表
     */
    @PreAuthorize("@ss.hasPermi('auth:user:export')")
    @Log(title = "应用和用户关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuthClientUser authClientUser) {
        QueryWrapper<AuthClientUser> queryWrapper = new QueryWrapper<>(authClientUser);
        List<AuthClientUser> list = authClientUserService.list(queryWrapper);
        ExcelUtil<AuthClientUser> util = new ExcelUtil<>(AuthClientUser.class);
        util.exportExcel(response, list, "应用和用户关联数据");
    }

    /**
     * 获取应用和用户关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('auth:user:query')")
    @GetMapping(value = "/{clientId}")
    public AjaxResult getInfo(@PathVariable("clientId") Long clientId) {
        return success(authClientUserService.getById(clientId));
    }

    /**
     * 新增应用和用户关联
     */
    @PreAuthorize("@ss.hasPermi('auth:user:add')")
    @Log(title = "应用和用户关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuthClientUser authClientUser) {
        return toAjax(authClientUserService.save(authClientUser));
    }

    /**
     * 修改应用和用户关联
     */
    @PreAuthorize("@ss.hasPermi('auth:user:edit')")
    @Log(title = "应用和用户关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuthClientUser authClientUser) {
        return toAjax(authClientUserService.updateById(authClientUser));
    }

    /**
     * 删除应用和用户关联
     */
    @PreAuthorize("@ss.hasPermi('auth:user:remove')")
    @Log(title = "应用和用户关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clientIds}")
    public AjaxResult remove(@PathVariable Long[] clientIds) {
        return toAjax(authClientUserService.removeByIds(Arrays.asList(clientIds)));
    }
}
