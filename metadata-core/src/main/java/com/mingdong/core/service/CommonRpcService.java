package com.mingdong.core.service;

import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;

public interface CommonRpcService
{
    /**
     * 查询指定的客户用户名是否已被占用
     *
     * @return 1-已占用，0-未占用
     */
    Integer checkIfClientExist(String username);

    /**
     * 检测管理权限分组是否已存在
     *
     * @return 1-存在，0-不存在
     */
    Integer checkIfGroupExist(String name);

    /**
     * 检查是否已存在指定编码的行业分类
     *
     * @return 1-已存在，0-不存在
     */
    Integer checkIfIndustryExist(String code);

    /**
     * 产品字典
     */
    ListDTO<DictDTO> getProductDict();

    /**
     * 管理权限分组字典
     */
    ListDTO<DictDTO> getAdminGroupDict();

    /**
     * 管理账号字典
     */
    ListDTO<DictDTO> getAdminUserDict();

    /**
     * 客户产品字典
     */
    ListDTO<DictDTO> getClientProductDict(Long clientId);

    /**
     * 充值类型字典
     */
    ListDTO<DictDTO> getRechargeTypeDict();
}
