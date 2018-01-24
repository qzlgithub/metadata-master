package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
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
import java.util.List;

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
        Manager byId = managerMapper.findByUsername(username);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, managerDTO);
        return managerDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateManagerSkipNull(ManagerDTO managerUpd)
    {
        ResultDTO resultDTO = new ResultDTO();
        Manager manager = new Manager();
        EntityUtils.copyProperties(managerUpd, manager);
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
        if(role == null){
            return null;
        }
        EntityUtils.copyProperties(role,roleDTO);
        return roleDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveRolePrivilegeList(List<RolePrivilegeDTO> toAddList)
    {
        ResultDTO resultDTO = new ResultDTO();
        List<RolePrivilege> dataList = new ArrayList<>();
        RolePrivilege rolePrivilege;
        for(RolePrivilegeDTO item : toAddList){
            rolePrivilege = new RolePrivilege();
            EntityUtils.copyProperties(item,rolePrivilege);
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
        if(role == null){
            return null;
        }
        EntityUtils.copyProperties(role,roleDTO);
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
    public ResultDTO updateRoleSkipNull(RoleDTO roleDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Role role = new Role();
        EntityUtils.copyProperties(roleDTO,role);
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
        if(CollectionUtils.isNotEmpty(managerPrivilegeList)){
            for(ManagerPrivilege item : managerPrivilegeList){
                managerPrivilegeDTO = new ManagerPrivilegeDTO();
                EntityUtils.copyProperties(item,managerPrivilegeDTO);
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
        for(ManagerPrivilegeDTO item : list){
            managerPrivilege = new ManagerPrivilege();
            EntityUtils.copyProperties(item,managerPrivilege);
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
        EntityUtils.copyProperties(managerDTO,manager);
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
        EntityUtils.copyProperties(managerDTO,manager);
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
        if(CollectionUtils.isNotEmpty(rolePrivileges)){
            for(RolePrivilege item : rolePrivileges){
                rolePrivilegeDTO = new RolePrivilegeDTO();
                EntityUtils.copyProperties(item,rolePrivilegeDTO);
                dataList.add(rolePrivilegeDTO);
            }
        }
        return rolePrivilegeListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeListByIds(List<Long> ids){
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        PrivilegeDTO privilegeDTO;
        List<Privilege> privilegeList = privilegeMapper.getParentIdByChildId(ids);
        if(CollectionUtils.isNotEmpty(privilegeList)){
            for(Privilege item : privilegeList){
                privilegeDTO = new PrivilegeDTO();
                EntityUtils.copyProperties(item,privilegeDTO);
                dataList.add(privilegeDTO);
            }
        }
        return privilegeListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        PrivilegeDTO privilegeDTO;
        List<Privilege> privilegeList = privilegeMapper.getTopListByRole(roleId);
        if(CollectionUtils.isNotEmpty(privilegeList)){
            for(Privilege item : privilegeList){
                privilegeDTO = new PrivilegeDTO();
                dataList.add(privilegeDTO);
                EntityUtils.copyProperties(item,privilegeDTO);
            }
        }
        return privilegeListDTO;
    }

}
