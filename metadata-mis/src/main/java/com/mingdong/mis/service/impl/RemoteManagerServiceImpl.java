package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.NewRole;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;
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
    public RoleListDTO getRoleList(Page page)
    {
        RoleListDTO roleListDTO = new RoleListDTO();
        List<RoleDTO> dataList = new ArrayList<>();
        roleListDTO.setDataList(dataList);
        RoleDTO roleDTO;
        if(page == null)
        {
            List<Role> roleList = roleMapper.getList();
            if(CollectionUtils.isNotEmpty(roleList))
            {
                for(Role item : roleList)
                {
                    roleDTO = new RoleDTO();
                    EntityUtils.copyProperties(item, roleDTO);
                    dataList.add(roleDTO);
                }
            }
        }
        else
        {
            int total = roleMapper.countAll();
            int pages = page.getTotalPage(total);
            roleListDTO.setPages(pages);
            roleListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<Role> roleList = roleMapper.getList();
                if(CollectionUtils.isNotEmpty(roleList))
                {
                    for(Role item : roleList)
                    {
                        roleDTO = new RoleDTO();
                        dataList.add(roleDTO);
                        EntityUtils.copyProperties(item, roleDTO);
                    }
                }
            }
        }
        return roleListDTO;
    }

    @Override
    public RoleDTO getRoleByName(String name)
    {
        RoleDTO roleDTO = new RoleDTO();
        Role role = roleMapper.findByName(name);
        if(role == null)
        {
            return null;
        }
        EntityUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveRolePrivilegeList(List<RolePrivilegeDTO> toAddList)
    {
        ResultDTO resultDTO = new ResultDTO();
        List<RolePrivilege> dataList = new ArrayList<>();
        RolePrivilege rolePrivilege;
        for(RolePrivilegeDTO item : toAddList)
        {
            rolePrivilege = new RolePrivilege();
            EntityUtils.copyProperties(item, rolePrivilege);
            dataList.add(rolePrivilege);
        }
        rolePrivilegeMapper.addList(dataList);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveRole(RoleDTO roleDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = new Role();
        EntityUtils.copyProperties(roleDTO, role);
        roleMapper.add(role);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public RoleDTO getRoleById(Long roleId)
    {
        RoleDTO roleDTO = new RoleDTO();
        Role role = roleMapper.findById(roleId);
        if(role == null)
        {
            return null;
        }
        EntityUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }

    @Override
    @Transactional
    public ResultDTO deleteRolePrivilegeByRoleId(Long roleId)
    {
        ResultDTO resultDTO = new ResultDTO();
        rolePrivilegeMapper.deleteByRole(roleId);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateRoleSkipNull(NewRole newRole)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = roleMapper.findById(newRole.getRoleDTO().getId());
        if(role == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        role = roleMapper.findByName(newRole.getRoleDTO().getName());
        if(role != null && !newRole.getRoleDTO().getId().equals(role.getId()))
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        if(newRole.getPrivilege() != null)
        {
            rolePrivilegeMapper.deleteByRole(newRole.getRoleDTO().getId());
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newRole.getPrivilege());
            Date current = new Date();
            List<RolePrivilege> toAddList = new ArrayList<>();
            for(Long id : allPrivilegeIdList)
            {
                RolePrivilege rp = new RolePrivilege();
                rp.setCreateTime(current);
                rp.setUpdateTime(current);
                rp.setRoleId(newRole.getRoleDTO().getId());
                rp.setPrivilegeId(id);
                toAddList.add(rp);
            }
            rolePrivilegeMapper.addList(toAddList);
        }
        role = new Role();
        EntityUtils.copyProperties(newRole.getRoleDTO(), role);
        roleMapper.updateSkipNull(role);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
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
            if(CollectionUtils.isNotEmpty(managerInfoList))
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
                if(CollectionUtils.isNotEmpty(managerInfoList))
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
        if(CollectionUtils.isNotEmpty(managerPrivilegeList))
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
    @Transactional
    public ResultDTO saveManagerPrivilegeList(List<ManagerPrivilegeDTO> list)
    {
        ResultDTO resultDTO = new ResultDTO();
        List<ManagerPrivilege> dataList = new ArrayList<>();
        ManagerPrivilege managerPrivilege;
        for(ManagerPrivilegeDTO item : list)
        {
            managerPrivilege = new ManagerPrivilege();
            EntityUtils.copyProperties(item, managerPrivilege);
            dataList.add(managerPrivilege);
        }
        managerPrivilegeMapper.addList(dataList);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveManager(ManagerDTO managerDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager manager = new Manager();
        EntityUtils.copyProperties(managerDTO, manager);
        managerMapper.add(manager);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO deleteManagerPrivilegeByManagerId(Long managerId)
    {
        ResultDTO resultDTO = new ResultDTO();
        managerPrivilegeMapper.deleteByManager(managerId);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerById(ManagerDTO managerDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager manager = new Manager();
        EntityUtils.copyProperties(managerDTO, manager);
        managerMapper.updateById(manager);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public RolePrivilegeListDTO getRolePrivilegeListByRoleId(Long roleId)
    {
        RolePrivilegeListDTO rolePrivilegeListDTO = new RolePrivilegeListDTO();
        List<RolePrivilegeDTO> dataList = new ArrayList<>();
        rolePrivilegeListDTO.setDataList(dataList);
        RolePrivilegeDTO rolePrivilegeDTO;
        List<RolePrivilege> rolePrivileges = rolePrivilegeMapper.getByRole(roleId);
        if(CollectionUtils.isNotEmpty(rolePrivileges))
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
    public ResultDTO addRole(NewRole newRole)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = roleMapper.findByName(newRole.getRoleDTO().getName());
        if(role != null)
        {
            resultDTO.setResult(RestResult.ROLE_NAME_EXIST);
            return resultDTO;
        }
        if(newRole.getPrivilege() != null)
        {
            Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(newRole.getPrivilege());
            Date current = new Date();
            List<RolePrivilege> toAddList = new ArrayList<>();
            for(Long id : allPrivilegeIdList)
            {
                RolePrivilege rp = new RolePrivilege();
                rp.setCreateTime(current);
                rp.setUpdateTime(current);
                rp.setPrivilegeId(id);
                rp.setRoleId(newRole.getRoleDTO().getId());
                toAddList.add(rp);
            }
            rolePrivilegeMapper.addList(toAddList);
        }
        role = new Role();
        EntityUtils.copyProperties(newRole.getRoleDTO(), role);
        roleMapper.add(role);
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
