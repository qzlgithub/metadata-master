package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.AdminSessionResDTO;
import com.mingdong.core.model.dto.AdminUserReqDTO;
import com.mingdong.core.model.dto.GroupDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.LoginDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.service.ManagerRpcService;
import com.mingdong.mis.domain.entity.Function;
import com.mingdong.mis.domain.entity.Group;
import com.mingdong.mis.domain.entity.GroupFunction;
import com.mingdong.mis.domain.entity.User;
import com.mingdong.mis.domain.entity.UserFunction;
import com.mingdong.mis.domain.entity.UserInfo;
import com.mingdong.mis.domain.mapper.FunctionMapper;
import com.mingdong.mis.domain.mapper.GroupFunctionMapper;
import com.mingdong.mis.domain.mapper.GroupMapper;
import com.mingdong.mis.domain.mapper.UserFunctionMapper;
import com.mingdong.mis.domain.mapper.UserInfoMapper;
import com.mingdong.mis.domain.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagerRpcServiceImpl implements ManagerRpcService
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
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public AdminSessionResDTO adminLogin(LoginDTO loginDTO)
    {
        AdminSessionResDTO resDTO = new AdminSessionResDTO();
        // 1. 验证登陆信息正确性
        User manager = userMapper.findByUsername(loginDTO.getUsername());
        if(manager == null)
        {
            resDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return resDTO;
        }
        else if(TrueOrFalse.FALSE.equals(manager.getEnabled()))
        {
            resDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return resDTO;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(loginDTO.getPassword())))
        {
            resDTO.setResult(RestResult.INVALID_PASSCODE);
            return resDTO;
        }
        else if(!TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            resDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return resDTO;
        }
        // 删除用户旧的sessionId
        if(!StringUtils.isNullBlank(manager.getSessionId()))
        {
            resDTO.setSessionId(manager.getSessionId());
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

        resDTO.setUserId(manager.getId());
        resDTO.setName(manager.getName());
        resDTO.setRoleType(manager.getRoleType());
        resDTO.setFunctionList(privilegeListDTO.getList());
        return resDTO;
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
            userInfoDTO.setRoleType(user.getRoleType());
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
    public ResponseDTO editAdminUser(AdminUserReqDTO userDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userMapper.findById(userDTO.getUserId());
        if(user == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Date date = new Date();
        // 修改管理账号基本信息
        User tempUser = new User();
        tempUser.setId(userDTO.getUserId());
        tempUser.setUpdateTime(date);
        tempUser.setRoleType(userDTO.getRoleType());
        tempUser.setGroupId(userDTO.getGroupId());
        tempUser.setUsername(userDTO.getUsername());
        tempUser.setPassword(userDTO.getPassword());
        tempUser.setName(userDTO.getName());
        tempUser.setPhone(userDTO.getPhone());
        tempUser.setQq(userDTO.getQq());
        tempUser.setEnabled(userDTO.getEnabled());
        userMapper.updateSkipNull(tempUser);
        // 修改管理账号的权限信息
        userFunctionMapper.deleteByManager(userDTO.getUserId());
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(userDTO.getPrivilegeIdList());
        List<UserFunction> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            UserFunction mp = new UserFunction();
            mp.setCreateTime(date);
            mp.setUpdateTime(date);
            mp.setManagerId(userDTO.getUserId());
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        userFunctionMapper.addList(list);
        return responseDTO;
    }

    @Override
    public ListDTO<ManagerInfoDTO> getAdminUserList(Integer roleType, Integer enabled, Page page)
    {
        ListDTO<ManagerInfoDTO> listDTO = new ListDTO<>();
        int total = userMapper.countBy(roleType, enabled);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<UserInfo> dataList = userInfoMapper.getListBy(roleType, enabled);
            List<ManagerInfoDTO> list = new ArrayList<>(dataList.size());
            for(UserInfo o : dataList)
            {
                ManagerInfoDTO mi = new ManagerInfoDTO();
                mi.setEnabled(o.getEnabled());
                mi.setManagerId(o.getManagerId());
                mi.setName(o.getName());
                mi.setPhone(o.getPhone());
                mi.setRegisterTime(o.getRegisterTime());
                mi.setRoleType(o.getRoleType());
                mi.setUsername(o.getUsername());
                mi.setGroupId(o.getGroupId());
                mi.setGroupName(o.getGroupName());
                list.add(mi);
            }
            listDTO.setList(list);
        }
        return listDTO;
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
    public ResponseDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userMapper.findById(managerId);
        if(user == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        else if(!user.getPassword().equals(Md5Utils.encrypt(oldPwd)))
        {
            responseDTO.setResult(RestResult.INVALID_PASSCODE);
            return responseDTO;
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
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addAdminUser(AdminUserReqDTO userDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userMapper.findByUsername(userDTO.getUsername());
        if(user != null)
        {
            responseDTO.setResult(RestResult.USERNAME_EXIST);
            return responseDTO;
        }
        Date current = new Date();
        // 保存管理账号
        user = new User();
        user.setCreateTime(current);
        user.setUpdateTime(current);
        user.setGroupId(userDTO.getGroupId());
        user.setRoleType(userDTO.getRoleType());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setQq(userDTO.getQq());
        user.setEnabled(userDTO.getEnabled());
        userMapper.add(user);
        // 保存管理账号的权限配置
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(userDTO.getPrivilegeIdList());
        List<UserFunction> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            UserFunction mp = new UserFunction();
            mp.setUpdateTime(current);
            mp.setCreateTime(current);
            mp.setManagerId(user.getId());
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        userFunctionMapper.addList(list);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeRoleStatus(Long groupId, Integer status)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Group role = groupMapper.findById(groupId);
        if(role == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        if(!role.getEnabled().equals(status))
        {
            Group obj = new Group();
            obj.setId(groupId);
            obj.setUpdateTime(new Date());
            obj.setEnabled(status);
            groupMapper.updateSkipNull(obj);
        }
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editAccountRole(GroupDTO groupDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Group role = groupMapper.findById(groupDTO.getId());
        if(role == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Group obj = groupMapper.findByName(groupDTO.getName());
        if(obj != null && !groupDTO.getId().equals(obj.getId()))
        {
            responseDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return responseDTO;
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
        return responseDTO;
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
    public ResponseDTO addAccountRole(GroupDTO groupDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Group role = groupMapper.findByName(groupDTO.getName());
        if(role != null)
        {
            responseDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return responseDTO;
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
        return responseDTO;
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
    @Transactional
    public ResponseDTO changeUserStatus(Long userId, Integer status)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userMapper.findById(userId);
        if(user == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        User temp = new User();
        temp.setId(userId);
        temp.setUpdateTime(new Date());
        temp.setEnabled(status);
        userMapper.updateSkipNull(temp);
        return responseDTO;
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
