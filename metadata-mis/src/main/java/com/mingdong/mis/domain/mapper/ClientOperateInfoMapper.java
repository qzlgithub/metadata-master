package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientOperateInfo;

import java.util.List;

public interface ClientOperateInfoMapper
{
    List<ClientOperateInfo> getListByClientUser(Long clientUserId);
}