package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientOperateInfo;

import java.util.List;

public interface ClientOperateInfoMapper
{
    List<ClientOperateInfo> getListByClientUser(Long clientUserId);
}
