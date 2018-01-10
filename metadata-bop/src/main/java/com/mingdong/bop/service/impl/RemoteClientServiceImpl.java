package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.domain.entity.Manager;
import com.mingdong.bop.domain.entity.ManagerPrivilege;
import com.mingdong.bop.domain.mapper.ManagerMapper;
import com.mingdong.bop.domain.mapper.ManagerPrivilegeMapper;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.service.RemoteClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteClientServiceImpl implements RemoteClientService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private ManagerMapper managerMapper;
    @Resource
    private ManagerPrivilegeMapper managerPrivilegeMapper;

    private static Logger logger = LoggerFactory.getLogger(RemoteProductServiceImpl.class);

    @Override
    @Transactional
    public void userLogin(String username, String password, BLResp resp)
    {
        Manager manager  = managerMapper.findByUsername(username);
        if(manager == null){
            resp.result(RestResult.ACCOUNT_NOT_EXIST);
            return;
        }
        else if(TrueOrFalse.FALSE.equals(manager.getEnabled()))
        {
            resp.result(RestResult.ACCOUNT_DISABLED);
            return;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(password)))
        {
            resp.result(RestResult.INVALID_PASSCODE);
            return;
        }
        else if(!TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            resp.result(RestResult.ACCOUNT_DISABLED);
            return;
        }
        if(!StringUtils.isNullBlank(manager.getSessionId()))
        {
            redisDao.dropManagerSession(manager.getSessionId());
        }
        Date current = new Date();
        Manager managerUpd = new Manager();
        managerUpd.setId(manager.getId());
        managerUpd.setUpdateTime(current);
        managerMapper.updateSkipNull(managerUpd);
        List<String> privilegeList = getManagerPrivilegeIdList(manager.getId());
        ManagerSession ms = new ManagerSession();
        ms.setManagerId(manager.getId());
        ms.setName(manager.getName());
        ms.setPrivileges(privilegeList);
        ms.setAddAt(current.getTime());
    }

    @Override
    public void userLogout(String sessionId)
    {
        redisDao.dropManagerSession(sessionId);
    }

    private List<String> getManagerPrivilegeIdList(Long managerId)
    {
        List<ManagerPrivilege> dataList = managerPrivilegeMapper.getPrivilegeIdListByManager(managerId);
        List<String> list = new ArrayList<>(dataList.size());
        for(ManagerPrivilege mp : dataList)
        {
            list.add(mp.getPrivilegeId() + "");
        }
        return list;
    }
}
