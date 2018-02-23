package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.DictRechargeType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictRechargeTypeMapper
{
    void add(DictRechargeType obj);

    void updateById(DictRechargeType obj);

    DictRechargeType findById(Integer id);

    DictRechargeType findByName(String name);

    void updateSkipNull(DictRechargeType obj);

    List<DictRechargeType> getListByStatus(@Param("enabled") Integer enabled, @Param("deleted") Integer deleted);

    /**
     * 查询可用的充值类型列表（即未删除的数据）
     */
    List<DictRechargeType> getAvailableList();
}
