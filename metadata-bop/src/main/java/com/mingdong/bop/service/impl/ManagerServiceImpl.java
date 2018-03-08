package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.AdminUserVO;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.model.NewManagerVO;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.AdminSessionDTO;
import com.mingdong.core.model.dto.AdminUserDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.GroupDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.LoginDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ManagerRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private CommonRpcService commonRpcService;
    @Resource
    private ManagerRpcService managerRpcService;

    @Override
    public void userLogin(String username, String password, String sessionId, RestResp resp)
    {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        loginDTO.setSessionId(sessionId);
        AdminSessionDTO adminSessionDTO = managerRpcService.adminLogin(loginDTO);
        if(RestResult.SUCCESS != adminSessionDTO.getResult())
        {
            resp.setError(adminSessionDTO.getResult());
            return;
        }
        // 删除用户旧的sessionId
        if(adminSessionDTO.getSessionId() != null)
        {
            redisDao.dropManagerSession(adminSessionDTO.getSessionId());
        }
        // 缓存用户的账号及权限
        ManagerSession ms = new ManagerSession();
        ms.setManagerId(adminSessionDTO.getUserId());
        ms.setName(adminSessionDTO.getName());
        ms.setPrivileges(adminSessionDTO.getFunctionList());
        ms.setAddAt(System.currentTimeMillis());
        ms.setRoleType(adminSessionDTO.getRoleType());
        redisDao.saveManagerSession(sessionId, ms);
        resp.addData(Field.NAME, adminSessionDTO.getName());
    }

    @Override
    public void userLogout(String sessionId)
    {
        redisDao.dropManagerSession(sessionId);
    }

    @Override
    public void changePassword(Long managerId, String oldPwd, String newPwd, RestResp resp)
    {
        ResultDTO resultDTO = managerRpcService.updateManagerPwd(managerId, newPwd, oldPwd);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getAccountRoleList(Page page, RestListResp res)
    {
        ListDTO<GroupDTO> listDTO = managerRpcService.getAccountGroupList(null);
        res.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(GroupDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.ID, o.getId() + "");
                m.put(Field.NAME, o.getName());
                if(CollectionUtils.isEmpty(o.getModuleNameList()))
                {
                    m.put(Field.MODULE, "");
                }
                else
                {
                    m.put(Field.MODULE, StringUtils.join(o.getModuleNameList().toArray(), "，"));
                }
                m.put(Field.ENABLED, o.getEnabled());
                list.add(m);
            }
            res.setList(list);
        }
    }

    @Override
    public void addAccountRole(String name, List<Long> privilegeIdList, RestResp resp)
    {
        GroupDTO roleDTO = new GroupDTO();
        roleDTO.setName(name);
        roleDTO.setPrivilegeIdList(privilegeIdList);
        ResultDTO resultDTO = managerRpcService.addAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editRole(Long groupId, String groupName, List<Long> privilege, RestResp resp)
    {
        GroupDTO roleDTO = new GroupDTO();
        roleDTO.setId(groupId);
        roleDTO.setName(groupName);
        roleDTO.setPrivilegeIdList(privilege);
        ResultDTO resultDTO = managerRpcService.editAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getManagerList(Integer roleType, Integer enabled, Page page, RestListResp res)
    {
        ListDTO<ManagerInfoDTO> listDTO = managerRpcService.getAdminUserList(roleType, enabled, page);
        res.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(ManagerInfoDTO manager : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, manager.getManagerId() + "");
                map.put(Field.USERNAME, manager.getUsername());
                map.put(Field.ROLE_TYPE, manager.getRoleType());
                map.put(Field.GROUP_NAME, manager.getGroupName());
                map.put(Field.NAME, manager.getName());
                map.put(Field.PHONE, manager.getPhone());
                map.put(Field.REGISTER_DATE, DateUtils.format(manager.getRegisterTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.ENABLED, manager.getEnabled());
                list.add(map);
            }
            res.setList(list);
        }
    }

    @Override
    public Map<String, Object> getAccountInfoData(Long userId)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(Field.MANAGER_ID, userId + "");
        // 查询账户的基本信息及权限配置信息
        UserInfoDTO userInfoDTO = managerRpcService.getAccountInfo(userId);
        data.put(Field.USERNAME, userInfoDTO.getUsername());
        data.put(Field.NAME, userInfoDTO.getName());
        data.put(Field.PHONE, userInfoDTO.getPhone());
        data.put(Field.QQ, userInfoDTO.getQq());
        data.put(Field.ROLE_ID, userInfoDTO.getGroupId());
        data.put(Field.ROLE_TYPE, userInfoDTO.getRoleType());
        data.put(Field.ENABLED, userInfoDTO.getEnabled());
        List<String> privilege = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userInfoDTO.getPrivilegeIdList()))
        {
            for(Long o : userInfoDTO.getPrivilegeIdList())
            {
                privilege.add(o + "");
            }
        }
        data.put(Field.PRIVILEGE, privilege);
        // 获取系统账户的角色字典
        ListDTO<DictDTO> listDTO = commonRpcService.getAdminGroupDict();
        List<Dict> roleDict = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictDTO o : listDTO.getList())
            {
                roleDict.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        data.put(Field.ROLE_DICT, roleDict);
        return data;
    }

    @Override
    public void addAdminUser(NewManagerVO newManagerVO, RestResp resp)
    {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setRoleType(newManagerVO.getRoleType());
        userDTO.setGroupId(newManagerVO.getGroupId());
        userDTO.setUsername(newManagerVO.getUsername());
        userDTO.setPassword(Md5Utils.encrypt(newManagerVO.getPassword()));
        userDTO.setName(newManagerVO.getName());
        userDTO.setPhone(newManagerVO.getPhone());
        userDTO.setQq(newManagerVO.getQq());
        userDTO.setEnabled(newManagerVO.getEnabled());
        userDTO.setPrivilegeIdList(newManagerVO.getPrivilege());
        ResultDTO resultDTO = managerRpcService.addAdminUser(userDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editAdminUser(AdminUserVO adminUserVO, RestResp resp)
    {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setUserId(adminUserVO.getManagerId());
        userDTO.setGroupId(adminUserVO.getGroupId());
        userDTO.setName(adminUserVO.getName());
        userDTO.setPhone(adminUserVO.getPhone());
        userDTO.setQq(adminUserVO.getQq());
        userDTO.setEnabled(adminUserVO.getEnabled());
        userDTO.setRoleType(adminUserVO.getRoleType());
        userDTO.setPrivilegeIdList(adminUserVO.getPrivilege());
        ResultDTO resultDTO = managerRpcService.editAdminUser(userDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public Map<String, Object> getAccountRoleInfo(Long groupId)
    {
        Map<String, Object> map = new HashMap<>();
        GroupDTO roleDTO = managerRpcService.getAccountRoleInfo(groupId);
        map.put(Field.ROLE_ID, groupId + "");
        map.put(Field.ROLE_NAME, roleDTO.getName());
        List<String> privilegeList = new ArrayList<>();
        for(Long privilegeId : roleDTO.getPrivilegeIdList())
        {
            privilegeList.add(privilegeId + "");
        }
        map.put(Field.PRIVILEGE_LIST, privilegeList);
        return map;
    }

    @Override
    public void getRolePrivilege(Long groupId, RestResp resp)
    {
        List<String> privilegeList = new ArrayList<>();
        GroupDTO roleDTO = managerRpcService.getAccountRoleInfo(groupId);
        if(!CollectionUtils.isEmpty(roleDTO.getPrivilegeIdList()))
        {
            for(Long o : roleDTO.getPrivilegeIdList())
            {
                privilegeList.add(o + "");
            }
        }
        resp.addData(Field.PRIVILEGE_LIST, privilegeList);
    }

    @Override
    public void changeRoleStatus(Long groupId, Integer status, RestResp resp)
    {
        ResultDTO resultDTO = managerRpcService.changeRoleStatus(groupId, status);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void changeManagerStatus(Long userId, Integer status, RestResp resp)
    {
        ResultDTO resultDTO = managerRpcService.changeUserStatus(userId, status);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void checkIfGroupExist(String name, RestResp resp)
    {
        resp.addData(Field.EXIST, commonRpcService.checkIfGroupExist(name));
    }

    @Override
    public List<Dict> getAdminUserDict()
    {
        List<Dict> list = new ArrayList<>();
        ListDTO<DictDTO> listDTO = commonRpcService.getAdminUserDict();
        List<DictDTO> dataList = listDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(DictDTO o : dataList)
            {
                list.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        return list;
    }
}
