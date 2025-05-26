package tech.qiantong.qknow.auth.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.auth.domain.AuthClient;
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
import java.util.Date;
import java.util.List;

/**
 * 应用管理Controller
 *
 * @author qknow
 * @date 2024-08-31
 */
@RestController
@RequestMapping("/auth/client")
public class AuthClientController extends BaseController {
    @Autowired
    private IService<AuthClient> authClientService;

/**
 * 查询应用管理列表
 */
@PreAuthorize("@ss.hasPermi('auth:client:list')")
@GetMapping("/list")
    public TableDataInfo list(AuthClient authClient) {
        startPage();
        QueryWrapper<AuthClient> queryWrapper = new QueryWrapper<>(authClient);
        List<AuthClient> list = authClientService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 导出应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('auth:client:export')")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuthClient authClient) {
        QueryWrapper<AuthClient> queryWrapper = new QueryWrapper<>(authClient);
        List<AuthClient> list = authClientService.list(queryWrapper);
        ExcelUtil<AuthClient> util = new ExcelUtil<>(AuthClient.class);
        util.exportExcel(response, list, "应用管理数据");
    }

    /**
     * 获取应用管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('auth:client:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(authClientService.getById(id));
    }

    /**
     * 新增应用管理
     */
    @PreAuthorize("@ss.hasPermi('auth:client:add')")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuthClient authClient) {
        authClient.setCreatorId(getUserId());
        authClient.setCreateBy(getNickName());
        authClient.setSecretKey(IdUtil.simpleUUID());
        return toAjax(authClientService.save(authClient));
    }

    /**
     * 修改应用管理
     */
    @PreAuthorize("@ss.hasPermi('auth:client:edit')")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuthClient authClient) {
        authClient.setUpdatorId(getUserId());
        authClient.setUpdateBy(getNickName());
        authClient.setUpdateTime(new Date());
        return toAjax(authClientService.updateById(authClient));
    }

    /**
     * 删除应用管理
     */
    @PreAuthorize("@ss.hasPermi('auth:client:remove')")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(authClientService.removeByIds(Arrays.asList(ids)));
    }
}
