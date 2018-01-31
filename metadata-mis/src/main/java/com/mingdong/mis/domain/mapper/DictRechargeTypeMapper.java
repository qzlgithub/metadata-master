package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.DictRechargeType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictRechargeTypeMapper
{
    void add(DictRechargeType dictRechargeType);

    void updateById(DictRechargeType dictRechargeType);

    DictRechargeType findById(Long id);

    DictRechargeType findByName(String name);

    void updateSkipNull(DictRechargeType dictRechargeType);

    List<DictRechargeType> getListByStatus(@Param("enabled") Integer enabled, @Param("deleted") Integer deleted);
}
