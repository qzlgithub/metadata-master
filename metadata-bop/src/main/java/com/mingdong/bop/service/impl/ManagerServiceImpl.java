package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.Param;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.model.ManagerVO;
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
import com.mingdong.core.util.IDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService
{
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private RemoteManagerService remoteManagerService;

    @Override
    public void userLogin(String username, String password, String sessionId, RestResp resp)
    {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        loginDTO.setSessionId(sessionId);
        AdminSessionDTO adminSessionDTO = remoteManagerService.adminLogin(loginDTO);
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
        ms.setRoleCode(adminSessionDTO.getRoleCode());
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
        ResultDTO resultDTO = remoteManagerService.updateManagerPwd(managerId, newPwd, oldPwd);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getAccountRoleList(Page page, RestListResp res)
    {
        ListDTO<GroupDTO> listDTO = remoteManagerService.getAccountGroupList(null);
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
        ResultDTO resultDTO = remoteManagerService.addAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editRole(Long groupId, String groupName, List<Long> privilege, RestResp resp)
    {
        GroupDTO roleDTO = new GroupDTO();
        roleDTO.setId(groupId);
        roleDTO.setName(groupName);
        roleDTO.setPrivilegeIdList(privilege);
        ResultDTO resultDTO = remoteManagerService.editAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getManagerList(String roleCode, Integer enabled, Page page, RestListResp res)
    {
        ManagerInfoListDTO managerListDTO = remoteManagerService.getManagerInfoList(roleCode, enabled, page);
        res.setTotal(managerListDTO.getTotal());
        List<ManagerInfoDTO> managerList = managerListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(managerList.size());
        if(!CollectionUtils.isEmpty(managerList))
        {
            for(ManagerInfoDTO manager : managerList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, manager.getManagerId() + "");
                map.put(Field.USERNAME, manager.getUsername());
                map.put(Field.ROLE_NAME, manager.getRoleName());
                map.put(Field.NAME, manager.getName());
                map.put(Field.PHONE, manager.getPhone());
                map.put(Field.REGISTER_DATE, DateUtils.format(manager.getRegisterTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.ENABLED, manager.getEnabled());
                list.add(map);
            }
        }
        res.setList(list);
    }

    @Override
    public Map<String, Object> getAccountInfoData(Long userId)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(Field.MANAGER_ID, userId + "");
        // 查询账户的基本信息及权限配置信息
        UserInfoDTO userInfoDTO = remoteManagerService.getAccountInfo(userId);
        data.put(Field.USERNAME, userInfoDTO.getUsername());
        data.put(Field.NAME, userInfoDTO.getName());
        data.put(Field.PHONE, userInfoDTO.getPhone());
        data.put(Field.QQ, userInfoDTO.getQq());
        data.put(Field.ROLE_ID, userInfoDTO.getGroupId());
        data.put(Field.ROLE_CODE, userInfoDTO.getRoleCode());
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
        ListDTO<DictDTO> listDTO = remoteManagerService.getAccountRoleDict();
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
    public void addManager(NewManagerVO newManagerVO, RestResp resp)
    {
        NewManager newManager = new NewManager();
        ManagerDTO manager = new ManagerDTO();
        Long managerId = IDUtils.getManagerId(param.getNodeId());
        Date current = new Date();
        manager.setId(managerId);
        manager.setCreateTime(current);
        manager.setUpdateTime(current);
        manager.setUsername(newManagerVO.getUsername());
        manager.setPassword(Md5Utils.encrypt(newManagerVO.getPassword()));
        manager.setName(newManagerVO.getName());
        manager.setPhone(newManagerVO.getPhone());
        manager.setQq(newManagerVO.getQq());
        manager.setGroupId(newManagerVO.getRoleId());
        manager.setEnabled(newManagerVO.getEnabled());
        manager.setRoleCode(newManagerVO.getRoleCode());
        newManager.setManagerDTO(manager);
        newManager.setPrivilege(newManagerVO.getPrivilege());
        ResultDTO resultDTO = remoteManagerService.addManager(newManager);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editManager(ManagerVO vo, RestResp resp)
    {
        NewManager newManager = new NewManager();
        ManagerDTO manager = new ManagerDTO();
        Date current = new Date();
        manager.setId(vo.getManagerId());
        manager.setUpdateTime(current);
        manager.setGroupId(vo.getRoleId());
        manager.setName(vo.getName());
        manager.setPhone(vo.getPhone());
        manager.setQq(vo.getQq());
        manager.setEnabled(vo.getEnabled());
        manager.setRoleCode(vo.getRoleCode());
        newManager.setManagerDTO(manager);
        newManager.setPrivilege(vo.getPrivilege());
        ResultDTO resultDTO = remoteManagerService.updateManagerSkipNull(newManager);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public Map<String, Object> getAccountRoleInfo(Long groupId)
    {
        Map<String, Object> map = new HashMap<>();
        GroupDTO roleDTO = remoteManagerService.getAccountRoleInfo(groupId);
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
        GroupDTO roleDTO = remoteManagerService.getAccountRoleInfo(groupId);
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
        ResultDTO resultDTO = remoteManagerService.changeRoleStatus(groupId, status);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void changeManagerStatus(Long userId, Integer status, RestResp resp)
    {
        ResultDTO resultDTO = remoteManagerService.changeUserStatus(userId, status);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void checkIfRoleNameExist(String name, RestResp resp)
    {
        resp.addData(Field.EXIST, remoteManagerService.isRoleNameExist(name));
    }

    @Override
    public List<Map<String, Object>> getManagerListMap(Integer enabled)
    {
        ManagerInfoListDTO managerInfoList = remoteManagerService.getManagerInfoList(null, enabled, null);
        List<ManagerInfoDTO> dataList = managerInfoList.getDataList();
        List<Map<String, Object>> dataListMap = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dataList))
        {
            Map<String, Object> dataMap;
            for(ManagerInfoDTO item : dataList)
            {
                dataMap = new HashMap<>();
                dataMap.put(Field.ID, item.getManagerId() + "");
                dataMap.put(Field.NAME, item.getName());
                dataListMap.add(dataMap);
            }
        }
        return dataListMap;
    }
}
