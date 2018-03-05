package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.RoleEnum;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.AdminSessionDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.LoginDTO;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.GroupDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.Function;
import com.mingdong.mis.domain.entity.Group;
import com.mingdong.mis.domain.entity.GroupFunction;
import com.mingdong.mis.domain.entity.User;
import com.mingdong.mis.domain.entity.UserFunction;
import com.mingdong.mis.domain.mapper.FunctionMapper;
import com.mingdong.mis.domain.mapper.GroupFunctionMapper;
import com.mingdong.mis.domain.mapper.GroupMapper;
import com.mingdong.mis.domain.mapper.UserFunctionMapper;
import com.mingdong.mis.domain.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoteManagerServiceImpl implements RemoteManagerService
{
    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupFunctionMapper groupFunctionMapper;
    @Resource
    private UserFunctionMapper userFunctionMapper;
    @Resource
    private FunctionMapper functionMapper;

    @Override
    public AdminSessionDTO adminLogin(LoginDTO loginDTO)
    {
        AdminSessionDTO adminSessionDTO = new AdminSessionDTO();
        // 1. 验证登陆信息正确性
        User manager = userMapper.findByUsername(loginDTO.getUsername());
        if(manager == null)
        {
            adminSessionDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return adminSessionDTO;
        }
        else if(TrueOrFalse.FALSE.equals(manager.getEnabled()))
        {
            adminSessionDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return adminSessionDTO;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(loginDTO.getPassword())))
        {
            adminSessionDTO.setResult(RestResult.INVALID_PASSCODE);
            return adminSessionDTO;
        }
        else if(!TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            adminSessionDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return adminSessionDTO;
        }
        // 删除用户旧的sessionId
        if(!StringUtils.isNullBlank(manager.getSessionId()))
        {
            adminSessionDTO.setSessionId(manager.getSessionId());
        }
        Date current = new Date();
        // 更新用户的sessionId
        User temp = new User();
        temp.setId(manager.getId());
        temp.setUpdateTime(current);
        temp.setSessionId(loginDTO.getSessionId());
        userMapper.updateSkipNull(temp);
        // 查询用户权限信息
        ListDTO<String> privilegeListDTO = getManagerPrivilegeListByManagerId(manager.getId());

        adminSessionDTO.setUserId(manager.getId());
        adminSessionDTO.setName(manager.getName());
        adminSessionDTO.setRoleCode(manager.getRoleCode());
        adminSessionDTO.setFunctionList(privilegeListDTO.getList());
        return adminSessionDTO;
    }

    @Override
    public ManagerDTO getManagerById(Long managerId)
    {
        ManagerDTO managerDTO = new ManagerDTO();
        User byId = userMapper.findById(managerId);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, managerDTO);
        return managerDTO;
    }

    @Override
    public UserInfoDTO getAccountInfo(Long userId)
    {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        User user = userMapper.findById(userId);
        if(user != null)
        {
            // 基本信息
            userInfoDTO.setUsername(user.getUsername());
            userInfoDTO.setName(user.getName());
            userInfoDTO.setPhone(user.getPhone());
            userInfoDTO.setQq(user.getQq());
            userInfoDTO.setGroupId(user.getGroupId());
            userInfoDTO.setEnabled(user.getEnabled());
            userInfoDTO.setRoleCode(user.getRoleCode());
            // 用户权限信息
            List<UserFunction> userFunctionList = userFunctionMapper.getListByUser(userId);
            if(!CollectionUtils.isEmpty(userFunctionList))
            {
                List<Long> privilegeIdList = new ArrayList<>(userFunctionList.size());
                for(UserFunction o : userFunctionList)
                {
                    privilegeIdList.add(o.getPrivilegeId());
                }
                userInfoDTO.setPrivilegeIdList(privilegeIdList);
            }
        }
        return userInfoDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerSkipNull(NewManager newManager) // TODO ddddd
    {
        ResultDTO resultDTO = new ResultDTO();
        User byId = userMapper.findById(newManager.getManagerDTO().getId());
        if(byId == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(newManager.getPrivilege() != null)
        {
            Date current = new Date();
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newManager.getPrivilege());
            userFunctionMapper.deleteByManager(newManager.getManagerDTO().getId());
            List<UserFunction> list = new ArrayList<>();
            for(Long privilegeId : allPrivilegeIdList)
            {
                UserFunction mp = new UserFunction();
                mp.setCreateTime(current);
                mp.setUpdateTime(current);
                mp.setManagerId(newManager.getManagerDTO().getId());
                mp.setPrivilegeId(privilegeId);
                list.add(mp);
            }
            userFunctionMapper.addList(list);
        }
        ManagerDTO managerDTO = newManager.getManagerDTO();
        User user = new User();
        user.setId(managerDTO.getId());
        user.setUpdateTime(managerDTO.getUpdateTime());
        user.setEnabled(managerDTO.getEnabled());
        user.setPassword(managerDTO.getPassword());
        user.setSessionId(managerDTO.getSessionId());
        user.setCreateTime(managerDTO.getCreateTime());
        user.setGroupId(managerDTO.getGroupId());
        user.setName(managerDTO.getName());
        user.setPhone(managerDTO.getPhone());
        user.setQq(managerDTO.getQq());
        user.setRoleCode(managerDTO.getRoleCode());
        user.setUsername(managerDTO.getUsername());
        userMapper.updateSkipNull(user);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public Integer isRoleNameExist(String name)
    {
        Group role = groupMapper.findByName(name);
        return role != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public ManagerInfoListDTO getManagerInfoList(String roleCode, Integer enabled, Page page)
    {
        ManagerInfoListDTO managerListDTO = new ManagerInfoListDTO();
        List<ManagerInfoDTO> dataList = new ArrayList<>();
        managerListDTO.setDataList(dataList);
        ManagerInfoDTO managerInfoDTO;
        if(page == null)
        {
            List<User> userInfoList = userMapper.getListBy(roleCode, enabled);
            if(!CollectionUtils.isEmpty(userInfoList))
            {
                for(User item : userInfoList)
                {
                    managerInfoDTO = new ManagerInfoDTO();
                    managerInfoDTO.setEnabled(item.getEnabled());
                    managerInfoDTO.setManagerId(item.getId());
                    managerInfoDTO.setName(item.getName());
                    managerInfoDTO.setPhone(item.getPhone());
                    managerInfoDTO.setRegisterTime(item.getCreateTime());
                    //                    managerInfoDTO.setRoleId(item.getRoleId());
                    managerInfoDTO.setUsername(item.getUsername());
                    managerInfoDTO.setRoleCode(item.getRoleCode());
                    managerInfoDTO.setRoleName(RoleEnum.getNameByCode(item.getRoleCode()));
                    dataList.add(managerInfoDTO);
                }
            }
        }
        else
        {
            int total = userMapper.countBy(roleCode, enabled);
            int pages = page.getTotalPage(total);
            managerListDTO.setPages(pages);
            managerListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<User> userInfoList = userMapper.getListBy(roleCode, enabled);
                if(!CollectionUtils.isEmpty(userInfoList))
                {
                    for(User item : userInfoList)
                    {
                        managerInfoDTO = new ManagerInfoDTO();
                        managerInfoDTO.setEnabled(item.getEnabled());
                        managerInfoDTO.setManagerId(item.getId());
                        managerInfoDTO.setName(item.getName());
                        managerInfoDTO.setPhone(item.getPhone());
                        managerInfoDTO.setRegisterTime(item.getCreateTime());
                        //                    managerInfoDTO.setRoleId(item.getRoleId());
                        managerInfoDTO.setRoleCode(item.getRoleCode());
                        managerInfoDTO.setUsername(item.getUsername());
                        managerInfoDTO.setRoleName(RoleEnum.getNameByCode(item.getRoleCode()));
                        dataList.add(managerInfoDTO);
                    }
                }
            }
        }
        return managerListDTO;
    }

    @Override
    public ListDTO<String> getManagerPrivilegeListByManagerId(Long managerId)
    {
        ListDTO<String> listDTO = new ListDTO<>();
        List<UserFunction> userFunctionList = userFunctionMapper.getListByUser(managerId);
        if(!CollectionUtils.isEmpty(userFunctionList))
        {
            List<String> list = new ArrayList<>(userFunctionList.size());
            for(UserFunction o : userFunctionList)
            {
                list.add(o.getPrivilegeId() + "");
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd)
    {
        ResultDTO resultDTO = new ResultDTO();
        User user = userMapper.findById(managerId);
        if(user == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        else if(!user.getPassword().equals(Md5Utils.encrypt(oldPwd)))
        {
            resultDTO.setResult(RestResult.INVALID_PASSCODE);
            return resultDTO;
        }
        String newPassword = Md5Utils.encrypt(newPwd);
        if(!user.getPassword().equals(newPassword))
        {
            user = new User();
            user.setId(managerId);
            user.setUpdateTime(new Date());
            user.setPassword(newPassword);
            userMapper.updateSkipNull(user);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO addManager(NewManager newManager)
    {
        ResultDTO resultDTO = new ResultDTO();
        User byUsername = userMapper.findByUsername(newManager.getManagerDTO().getUsername());
        if(byUsername != null)
        {
            resultDTO.setResult(RestResult.USERNAME_EXIST);
            return resultDTO;
        }
        Date current = new Date();
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newManager.getPrivilege());
        List<UserFunction> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            UserFunction mp = new UserFunction();
            mp.setUpdateTime(current);
            mp.setCreateTime(current);
            mp.setManagerId(newManager.getManagerDTO().getId());
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        userFunctionMapper.addList(list);
        ManagerDTO managerDTO = newManager.getManagerDTO();
        User user = new User();
        user.setId(managerDTO.getId());
        user.setUpdateTime(managerDTO.getUpdateTime());
        user.setEnabled(managerDTO.getEnabled());
        user.setPassword(managerDTO.getPassword());
        user.setSessionId(managerDTO.getSessionId());
        user.setCreateTime(managerDTO.getCreateTime());
        user.setGroupId(managerDTO.getGroupId());
        user.setName(managerDTO.getName());
        user.setPhone(managerDTO.getPhone());
        user.setQq(managerDTO.getQq());
        user.setRoleCode(managerDTO.getRoleCode());
        user.setUsername(managerDTO.getUsername());
        userMapper.add(user);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO changeRoleStatus(Long groupId, Integer status)
    {
        ResultDTO resultDTO = new ResultDTO();
        Group role = groupMapper.findById(groupId);
        if(role == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(!role.getEnabled().equals(status))
        {
            Group obj = new Group();
            obj.setId(groupId);
            obj.setUpdateTime(new Date());
            obj.setEnabled(status);
            groupMapper.updateSkipNull(obj);
        }
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO editAccountRole(GroupDTO groupDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Group role = groupMapper.findById(groupDTO.getId());
        if(role == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        Group obj = groupMapper.findByName(groupDTO.getName());
        if(obj != null && !groupDTO.getId().equals(obj.getId()))
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        Date date = new Date();
        // 清空角色的旧权限数据
        groupFunctionMapper.deleteByGroupId(groupDTO.getId());
        // 保存角色的新权限数据
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(groupDTO.getPrivilegeIdList());
        List<GroupFunction> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            GroupFunction rp = new GroupFunction();
            rp.setCreateTime(date);
            rp.setUpdateTime(date);
            rp.setGroupId(groupDTO.getId());
            rp.setPrivilegeId(id);
            toAddList.add(rp);
        }
        if(!CollectionUtils.isEmpty(toAddList))
        {
            groupFunctionMapper.addList(toAddList);
        }
        // 保存角色名称
        if(!role.getName().equals(groupDTO.getName()))
        {
            obj = new Group();
            obj.setId(groupDTO.getId());
            obj.setUpdateTime(date);
            obj.setName(groupDTO.getName());
            groupMapper.updateSkipNull(obj);
        }
        return resultDTO;
    }

    @Override
    public GroupDTO getAccountRoleInfo(Long groupId)
    {
        GroupDTO roleDTO = new GroupDTO();
        Group role = groupMapper.findById(groupId);
        if(role == null)
        {
            return roleDTO;
        }
        roleDTO.setName(role.getName());
        List<GroupFunction> dataList = groupFunctionMapper.getByGroupId(groupId);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Long> privilegeIdList = new ArrayList<>();
            for(GroupFunction o : dataList)
            {
                privilegeIdList.add(o.getPrivilegeId());
            }
            roleDTO.setPrivilegeIdList(privilegeIdList);
        }
        return roleDTO;
    }

    @Override
    @Transactional
    public ResultDTO addAccountRole(GroupDTO groupDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Group role = groupMapper.findByName(groupDTO.getName());
        if(role != null)
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        Date date = new Date();
        role = new Group();
        role.setCreateTime(date);
        role.setUpdateTime(date);
        role.setName(groupDTO.getName());
        role.setEnabled(TrueOrFalse.TRUE);
        groupMapper.add(role);
        if(!CollectionUtils.isEmpty(groupDTO.getPrivilegeIdList()))
        {
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(groupDTO.getPrivilegeIdList());
            List<GroupFunction> toAddList = new ArrayList<>();
            for(Long id : allPrivilegeIdList)
            {
                GroupFunction rp = new GroupFunction();
                rp.setCreateTime(date);
                rp.setUpdateTime(date);
                rp.setGroupId(role.getId());
                rp.setPrivilegeId(id);
                toAddList.add(rp);
            }
            groupFunctionMapper.addList(toAddList);
        }
        return resultDTO;
    }

    @Override
    public ListDTO<GroupDTO> getAccountGroupList(Page page)
    {
        ListDTO<GroupDTO> listDTO = new ListDTO<>();
        if(page == null)
        {
            List<Group> dataList = groupMapper.getList();
            List<GroupDTO> list = new ArrayList<>();
            for(Group o : dataList)
            {
                GroupDTO r = new GroupDTO();
                r.setId(o.getId());
                r.setEnabled(o.getEnabled());
                r.setName(o.getName());
                r.setModuleNameList(getGroupModuleNameList(o.getId()));
                list.add(r);
            }
            listDTO.setList(list);
        }
        else
        {
            int total = groupMapper.countAll();
            int pages = page.getTotalPage(total);
            listDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<Group> dataList = groupMapper.getList();
                List<GroupDTO> list = new ArrayList<>();
                for(Group o : dataList)
                {
                    GroupDTO r = new GroupDTO();
                    r.setId(o.getId());
                    r.setName(o.getName());
                    r.setEnabled(o.getEnabled());
                    r.setModuleNameList(getGroupModuleNameList(o.getId()));
                    list.add(r);
                }
                listDTO.setList(list);
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictDTO> getAccountRoleDict()
    {
        ListDTO<DictDTO> listDTO = new ListDTO<>();
        List<Group> roleList = groupMapper.getByStatus(TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(roleList))
        {
            List<DictDTO> list = new ArrayList<>(roleList.size());
            for(Group o : roleList)
            {
                list.add(new DictDTO(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResultDTO changeUserStatus(Long userId, Integer status)
    {
        ResultDTO resultDTO = new ResultDTO();
        User user = userMapper.findById(userId);
        if(user == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        User temp = new User();
        temp.setId(userId);
        temp.setUpdateTime(new Date());
        temp.setEnabled(status);
        userMapper.updateSkipNull(temp);
        return resultDTO;
    }

    /**
     * 查询指定角色已分配的模块名称列表
     */
    private List<String> getGroupModuleNameList(Long groupId)
    {
        List<Function> dataList = functionMapper.getModuleListByGroupId(groupId);
        List<String> list = new ArrayList<>();
        for(Function o : dataList)
        {
            list.add(o.getName());
        }
        return list;
    }

    /**
     * 查询权限列表及其父级权限的ID
     */
    private Set<Long> getRelatedPrivilegeId(List<Long> privilege)
    {
        Set<Long> idSet = new HashSet<>(privilege);
        Set<Long> parentIdSet = getParentId(idSet);
        idSet.addAll(parentIdSet);
        return idSet;
    }

    private Set<Long> getParentId(Set<Long> privilege)
    {
        Set<Long> set = new HashSet<>();
        if(!CollectionUtils.isEmpty(privilege))
        {
            List<Function> functionList = functionMapper.getParentIdByChildId(new ArrayList<>(privilege));
            for(Function p : functionList)
            {
                if(p.getParentId() != null && p.getParentId() != 0)
                {
                    set.add(p.getParentId());
                }
            }
            set.addAll(getParentId(set));
        }
        return set;
    }

}
