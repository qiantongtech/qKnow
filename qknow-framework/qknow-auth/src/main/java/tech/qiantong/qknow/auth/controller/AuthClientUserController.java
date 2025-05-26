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
