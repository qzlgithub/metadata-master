package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.Manager;
import com.mingdong.mis.domain.entity.ManagerInfo;
import com.mingdong.mis.domain.entity.ManagerPrivilege;
import com.mingdong.mis.domain.entity.Privilege;
import com.mingdong.mis.domain.entity.Role;
import com.mingdong.mis.domain.entity.RolePrivilege;
import com.mingdong.mis.domain.mapper.ManagerInfoMapper;
import com.mingdong.mis.domain.mapper.ManagerMapper;
import com.mingdong.mis.domain.mapper.ManagerPrivilegeMapper;
import com.mingdong.mis.domain.mapper.PrivilegeMapper;
import com.mingdong.mis.domain.mapper.RoleMapper;
import com.mingdong.mis.domain.mapper.RolePrivilegeMapper;
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
    private ManagerMapper managerMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePrivilegeMapper rolePrivilegeMapper;
    @Resource
    private ManagerInfoMapper managerInfoMapper;
    @Resource
    private ManagerPrivilegeMapper managerPrivilegeMapper;
    @Resource
    private PrivilegeMapper privilegeMapper;

    @Override
    public ManagerDTO getManagerById(Long managerId)
    {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager byId = managerMapper.findById(managerId);
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
        Manager manager = managerMapper.findById(userId);
        if(manager != null)
        {
            // 基本信息
            userInfoDTO.setUsername(manager.getUsername());
            userInfoDTO.setName(manager.getName());
            userInfoDTO.setPhone(manager.getPhone());
            userInfoDTO.setQq(manager.getQq());
            userInfoDTO.setRoleId(manager.getRoleId());
            userInfoDTO.setEnabled(manager.getEnabled());
            // 用户权限信息
            List<ManagerPrivilege> managerPrivilegeList = managerPrivilegeMapper.getListByUser(userId);
            if(!CollectionUtils.isEmpty(managerPrivilegeList))
            {
                List<Long> privilegeIdList = new ArrayList<>(managerPrivilegeList.size());
                for(ManagerPrivilege o : managerPrivilegeList)
                {
                    privilegeIdList.add(o.getPrivilegeId());
                }
                userInfoDTO.setPrivilegeIdList(privilegeIdList);
            }
        }
        return userInfoDTO;
    }

    @Override
    public ManagerDTO getManagerByUsername(String username)
    {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager byUsername = managerMapper.findByUsername(username);
        if(byUsername == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byUsername, managerDTO);
        return managerDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerSkipNull(NewManager newManager)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager byId = managerMapper.findById(newManager.getManagerDTO().getId());
        if(byId == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(newManager.getPrivilege() != null)
        {
            Date current = new Date();
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newManager.getPrivilege());
            managerPrivilegeMapper.deleteByManager(newManager.getManagerDTO().getId());
            List<ManagerPrivilege> list = new ArrayList<>();
            for(Long privilegeId : allPrivilegeIdList)
            {
                ManagerPrivilege mp = new ManagerPrivilege();
                mp.setCreateTime(current);
                mp.setUpdateTime(current);
                mp.setManagerId(newManager.getManagerDTO().getId());
                mp.setPrivilegeId(privilegeId);
                list.add(mp);
            }
            managerPrivilegeMapper.addList(list);
        }
        Manager manager = new Manager();
        EntityUtils.copyProperties(newManager.getManagerDTO(), manager);
        managerMapper.updateSkipNull(manager);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public Integer isRoleNameExist(String name)
    {
        Role role = roleMapper.findByName(name);
        return role != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public ManagerInfoListDTO getManagerInfoList(Long roleId, Integer enabled, Page page)
    {
        ManagerInfoListDTO managerListDTO = new ManagerInfoListDTO();
        List<ManagerInfoDTO> dataList = new ArrayList<>();
        managerListDTO.setDataList(dataList);
        ManagerInfoDTO managerInfoDTO;
        if(page == null)
        {
            List<ManagerInfo> managerInfoList = managerInfoMapper.getListBy(roleId, enabled);
            if(!CollectionUtils.isEmpty(managerInfoList))
            {
                for(ManagerInfo item : managerInfoList)
                {
                    managerInfoDTO = new ManagerInfoDTO();
                    EntityUtils.copyProperties(item, managerInfoDTO);
                    dataList.add(managerInfoDTO);
                }
            }
        }
        else
        {
            int total = managerMapper.countBy(roleId, enabled);
            int pages = page.getTotalPage(total);
            managerListDTO.setPages(pages);
            managerListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ManagerInfo> managerInfoList = managerInfoMapper.getListBy(roleId, enabled);
                if(!CollectionUtils.isEmpty(managerInfoList))
                {
                    for(ManagerInfo item : managerInfoList)
                    {
                        managerInfoDTO = new ManagerInfoDTO();
                        dataList.add(managerInfoDTO);
                        EntityUtils.copyProperties(item, managerInfoDTO);
                    }
                }
            }
        }
        return managerListDTO;
    }

    @Override
    public ManagerPrivilegeListDTO getManagerPrivilegeListByManagerId(Long managerId)
    {
        ManagerPrivilegeListDTO managerPrivilegeListDTO = new ManagerPrivilegeListDTO();
        List<ManagerPrivilegeDTO> dataList = new ArrayList<>();
        managerPrivilegeListDTO.setDataList(dataList);
        List<ManagerPrivilege> managerPrivilegeList = managerPrivilegeMapper.getPrivilegeIdListByManager(managerId);
        ManagerPrivilegeDTO managerPrivilegeDTO;
        if(!CollectionUtils.isEmpty(managerPrivilegeList))
        {
            for(ManagerPrivilege item : managerPrivilegeList)
            {
                managerPrivilegeDTO = new ManagerPrivilegeDTO();
                EntityUtils.copyProperties(item, managerPrivilegeDTO);
                dataList.add(managerPrivilegeDTO);
            }
        }
        return managerPrivilegeListDTO;
    }

    @Override
    public RolePrivilegeListDTO getRolePrivilegeListByRoleId(Long roleId)
    {
        RolePrivilegeListDTO rolePrivilegeListDTO = new RolePrivilegeListDTO();
        List<RolePrivilegeDTO> dataList = new ArrayList<>();
        rolePrivilegeListDTO.setDataList(dataList);
        RolePrivilegeDTO rolePrivilegeDTO;
        List<RolePrivilege> rolePrivileges = rolePrivilegeMapper.getByRole(roleId);
        if(!CollectionUtils.isEmpty(rolePrivileges))
        {
            for(RolePrivilege item : rolePrivileges)
            {
                rolePrivilegeDTO = new RolePrivilegeDTO();
                EntityUtils.copyProperties(item, rolePrivilegeDTO);
                dataList.add(rolePrivilegeDTO);
            }
        }
        return rolePrivilegeListDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager manager = managerMapper.findById(managerId);
        if(manager == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(oldPwd)))
        {
            resultDTO.setResult(RestResult.INVALID_PASSCODE);
            return resultDTO;
        }
        String newPassword = Md5Utils.encrypt(newPwd);
        if(!manager.getPassword().equals(newPassword))
        {
            manager = new Manager();
            manager.setId(managerId);
            manager.setUpdateTime(new Date());
            manager.setPassword(newPassword);
            managerMapper.updateSkipNull(manager);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO addManager(NewManager newManager)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager byUsername = managerMapper.findByUsername(newManager.getManagerDTO().getUsername());
        if(byUsername != null)
        {
            resultDTO.setResult(RestResult.USERNAME_EXIST);
            return resultDTO;
        }
        Date current = new Date();
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newManager.getPrivilege());
        List<ManagerPrivilege> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            ManagerPrivilege mp = new ManagerPrivilege();
            mp.setUpdateTime(current);
            mp.setCreateTime(current);
            mp.setManagerId(newManager.getManagerDTO().getId());
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        managerPrivilegeMapper.addList(list);
        Manager manager = new Manager();
        EntityUtils.copyProperties(newManager.getManagerDTO(), manager);
        managerMapper.add(manager);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO changeRoleStatus(Long roleId, Integer status)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = roleMapper.findById(roleId);
        if(role == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(!role.getEnabled().equals(status))
        {
            Role obj = new Role();
            obj.setId(roleId);
            obj.setUpdateTime(new Date());
            obj.setEnabled(status);
            roleMapper.updateSkipNull(obj);
        }
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO editAccountRole(RoleDTO roleDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = roleMapper.findById(roleDTO.getId());
        if(role == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        Role obj = roleMapper.findByName(roleDTO.getName());
        if(obj != null && !roleDTO.getId().equals(obj.getId()))
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        Date date = new Date();
        // 清空角色的旧权限数据
        rolePrivilegeMapper.deleteByRole(roleDTO.getId());
        // 保存角色的新权限数据
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(roleDTO.getPrivilegeIdList());
        List<RolePrivilege> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            RolePrivilege rp = new RolePrivilege();
            rp.setCreateTime(date);
            rp.setUpdateTime(date);
            rp.setRoleId(roleDTO.getId());
            rp.setPrivilegeId(id);
            toAddList.add(rp);
        }
        if(!CollectionUtils.isEmpty(toAddList))
        {
            rolePrivilegeMapper.addList(toAddList);
        }
        // 保存角色名称
        if(!role.getName().equals(roleDTO.getName()))
        {
            obj = new Role();
            obj.setId(roleDTO.getId());
            obj.setUpdateTime(date);
            obj.setName(roleDTO.getName());
            roleMapper.updateSkipNull(obj);
        }
        return resultDTO;
    }

    @Override
    public RoleDTO getAccountRoleInfo(Long roleId)
    {
        RoleDTO roleDTO = new RoleDTO();
        Role role = roleMapper.findById(roleId);
        if(role == null)
        {
            return roleDTO;
        }
        roleDTO.setName(role.getName());
        List<RolePrivilege> dataList = rolePrivilegeMapper.getByRole(roleId);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Long> privilegeIdList = new ArrayList<>();
            for(RolePrivilege o : dataList)
            {
                privilegeIdList.add(o.getPrivilegeId());
            }
            roleDTO.setPrivilegeIdList(privilegeIdList);
        }
        return roleDTO;
    }

    @Override
    @Transactional
    public ResultDTO addAccountRole(RoleDTO roleDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = roleMapper.findByName(roleDTO.getName());
        if(role != null)
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        Date date = new Date();
        role = new Role();
        role.setCreateTime(date);
        role.setUpdateTime(date);
        role.setName(roleDTO.getName());
        role.setEnabled(TrueOrFalse.TRUE);
        roleMapper.add(role);
        if(!CollectionUtils.isEmpty(roleDTO.getPrivilegeIdList()))
        {
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(roleDTO.getPrivilegeIdList());
            List<RolePrivilege> toAddList = new ArrayList<>();
            for(Long id : allPrivilegeIdList)
            {
                RolePrivilege rp = new RolePrivilege();
                rp.setCreateTime(date);
                rp.setUpdateTime(date);
                rp.setRoleId(role.getId());
                rp.setPrivilegeId(id);
                toAddList.add(rp);
            }
            rolePrivilegeMapper.addList(toAddList);
        }
        return resultDTO;
    }

    @Override
    public ListDTO<RoleDTO> getAccountRoleList(Page page)
    {
        ListDTO<RoleDTO> listDTO = new ListDTO<>();
        if(page == null)
        {
            List<Role> dataList = roleMapper.getList();
            List<RoleDTO> list = new ArrayList<>();
            for(Role o : dataList)
            {
                RoleDTO r = new RoleDTO();
                r.setId(o.getId());
                r.setEnabled(o.getEnabled());
                r.setName(o.getName());
                r.setModuleNameList(getRoleModuleNameList(o.getId()));
                list.add(r);
            }
            listDTO.setList(list);
        }
        else
        {
            int total = roleMapper.countAll();
            int pages = page.getTotalPage(total);
            listDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<Role> dataList = roleMapper.getList();
                List<RoleDTO> list = new ArrayList<>();
                for(Role o : dataList)
                {
                    RoleDTO r = new RoleDTO();
                    r.setId(o.getId());
                    r.setName(o.getName());
                    r.setEnabled(o.getEnabled());
                    r.setModuleNameList(getRoleModuleNameList(o.getId()));
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
        List<Role> roleList = roleMapper.getByStatus(TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(roleList))
        {
            List<DictDTO> list = new ArrayList<>(roleList.size());
            for(Role o : roleList)
            {
                list.add(new DictDTO(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    /**
     * 查询指定角色已分配的模块名称列表
     */
    private List<String> getRoleModuleNameList(Long roleId)
    {
        List<Privilege> dataList = privilegeMapper.getModuleListByRole(roleId);
        List<String> list = new ArrayList<>();
        for(Privilege o : dataList)
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
            List<Privilege> privilegeList = privilegeMapper.getParentIdByChildId(new ArrayList<>(privilege));
            for(Privilege p : privilegeList)
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
