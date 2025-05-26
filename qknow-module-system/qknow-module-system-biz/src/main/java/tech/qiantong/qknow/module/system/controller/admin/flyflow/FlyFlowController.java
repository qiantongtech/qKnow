package tech.qiantong.qknow.module.system.controller.admin.flyflow;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import tech.qiantong.qknow.common.core.domain.entity.SysDept;
import tech.qiantong.qknow.common.core.domain.entity.SysRole;
import tech.qiantong.qknow.common.core.domain.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.module.system.domain.dto.DeptDto;
import tech.qiantong.qknow.module.system.domain.dto.RoleDto;
import tech.qiantong.qknow.module.system.domain.dto.UserDto;
import tech.qiantong.qknow.module.system.domain.dto.UserQueryDto;
import tech.qiantong.qknow.module.system.mapper.SysDeptMapper;
import tech.qiantong.qknow.module.system.mapper.SysRoleMapper;
import tech.qiantong.qknow.module.system.mapper.SysUserMapper;
import tech.qiantong.qknow.module.system.service.ISysDeptService;
import tech.qiantong.qknow.module.system.service.ISysUserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作流请求处理
 *
 * @author qknow
 */
@RestController
@RequestMapping("/flyflow")
public class FlyFlowController {
    private static final Logger log = LoggerFactory.getLogger(FlyFlowController.class);

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 根据用户id获取用户详情
     */
    @PostMapping("/userById")
    public Object userById(@RequestBody Map map) throws Exception {

        long userId = MapUtil.getLong(map, "userId");
        SysUser sysUser = sysUserService.selectUserById(userId);
        UserDto userDto = buildUserDto(sysUser);

        return userDto;
    }

    private static UserDto buildUserDto(SysUser sysUser) {
        UserDto userDto = new UserDto();
        userDto.setId(String.valueOf(sysUser.getUserId()));
        userDto.setParentId(null);
        userDto.setName(sysUser.getNickName());
        userDto.setAvatarUrl(sysUser.getAvatar());
        userDto.setDeptIdList(CollUtil.newArrayList(String.valueOf(sysUser.getDeptId())));
        userDto.setStatus(1 - Integer.parseInt(sysUser.getStatus()));
        userDto.setPhone(sysUser.getPhonenumber());
        userDto.setDeptName(sysUser.getDept().getDeptName());
        return userDto;
    }

    /**
     * 根据部门id集合和角色id集合查询人员id集合
     */
    @PostMapping("/userIdListByRoleIdListAndDeptIdList")
    public Object userIdListByRoleIdListAndDeptIdList(@RequestBody UserQueryDto userQueryDto) throws Exception {
        Set<Long> longs = sysUserMapper.queryUserIdListByRoleIdListAndDeptIdList(userQueryDto.getDeptIdList(),
                userQueryDto.getRoleIdList());

        return longs;
    }


    /**
     * 根据用户id查询角色id集合
     */
    @PostMapping("/roleIdListByUserId")
    public Object roleIdListByUserId(@RequestBody Map map) throws Exception {
        long userId = MapUtil.getLong(map, "userId");
        SysUser sysUser = sysUserService.selectUserById(userId);
        Long[] roleIds = sysUser.getRoleIds();

        return roleIds;
    }


    /**
     * 获取所有的角色
     */
    @PostMapping("/roleAll")
    public Object roleAll() throws Exception {
        List<SysRole> sysRoles = sysRoleMapper.selectRoleAll();
        List<RoleDto> roleDtoList = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            RoleDto roleDto = new RoleDto();
            roleDto.setName(sysRole.getRoleName());
            roleDto.setId(String.valueOf(sysRole.getRoleId()));
            roleDto.setStatus(1 - Integer.parseInt(sysRole.getStatus()));
            roleDtoList.add(roleDto);
        }

        return roleDtoList;
    }

    /**
     * 获取所有的部门
     */
    @PostMapping("/deptListByParentDeptId")
    public Object deptListByParentDeptId(@RequestBody Map map) throws Exception {
        Long parentDeptId = MapUtil.getLong(map, "parentDeptId");

        SysDept dept = new SysDept();
        dept.setParentId(parentDeptId);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptListByParentId(dept);
        List<DeptDto> deptDtoList = new ArrayList<>();

        for (SysDept sysDept : sysDepts) {
            DeptDto deptDto = buildDeptDto(sysDept);
            deptDtoList.add(deptDto);
        }

        return deptDtoList;
    }

    private static DeptDto buildDeptDto(SysDept sysDept) {
        DeptDto deptDto = new DeptDto();
        deptDto.setId(String.valueOf(sysDept.getDeptId()));
        deptDto.setName(sysDept.getDeptName());
        deptDto.setParentId(Convert.toStr(sysDept.getParentId()));
        deptDto.setLeaderUserIdList(new ArrayList<>());
        deptDto.setStatus(1 - Integer.parseInt(sysDept.getStatus()));
        //deptDto.setSort();
        return deptDto;
    }

    /**
     * 根据部门获取部门下的用户集合
     */
    @PostMapping("/userListByDeptId")
    public Object userListByDeptId(@RequestBody Map map) throws Exception {

        Long deptId = MapUtil.getLong(map, "deptId");
        SysUser param = new SysUser();
        param.setDeptId(deptId);
        List<SysUser> sysUsers = sysUserMapper.selectUserListByDeptId(param);
        List<UserDto> deptDtoList = new ArrayList<>();

        for (SysUser sysUser : sysUsers) {
            UserDto userDto = buildUserDto(sysUser);
            deptDtoList.add(userDto);
        }

        return deptDtoList;
    }

    /**
     * 根据部门获取部门下的用户集合
     */
    @PostMapping("/userByName")
    public Object userByName(@RequestBody Map map) throws Exception {
        String name = MapUtil.getStr(map, "name");

        SysUser param = new SysUser();
        param.setNickName(name);
        List<SysUser> sysUsers = sysUserMapper.selectUserList(param);

        List<UserDto> deptDtoList = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            UserDto userDto = buildUserDto(sysUser);
            deptDtoList.add(userDto);
        }

        return deptDtoList;
    }

    /**
     * 根据部门获取部门下的用户集合
     */
    @PostMapping("/batchGetDept")
    public Object batchGetDept(@RequestBody UserQueryDto map) throws Exception {
        List<String> deptIdList = map.getDeptIdList();
        List<DeptDto> list = new ArrayList<>();

        for (String s : deptIdList) {
            SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(s));
            DeptDto deptDto = buildDeptDto(sysDept);
            list.add(deptDto);
        }

        return list;
    }

    /**
     * 根据部门获取部门下的用户集合
     */
    @PostMapping("/messageNotify")
    public void messageNotify(@RequestBody Map map) throws Exception {

    }
}
