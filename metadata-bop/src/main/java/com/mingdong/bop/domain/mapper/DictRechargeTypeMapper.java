package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.DictRechargeType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictRechargeTypeMapper
{
    void add(DictRechargeType dictRechargeType);

    void delete(Long id);

    void updateById(DictRechargeType dictRechargeType);

    DictRechargeType findById(Long id);

    List<DictRechargeType> getAll();

    DictRechargeType findByName(String name);

    void updateSkipNull(DictRechargeType dictRechargeType);

    List<DictRechargeType> getByStatus(@Param("enabled") Integer enabled);

    List<DictRechargeType> getListByStatus(@Param("enabled") Integer enabled, @Param("deleted") Integer deleted);
}
