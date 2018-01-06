package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.CorpProdRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CorpProdRechargeMapper
{
    void add(CorpProdRecharge corpProdRecharge);

    void delete(Long id);

    void updateById(CorpProdRecharge corpProdRecharge);

    CorpProdRecharge findById(Long id);

    List<CorpProdRecharge> getAll();

    int countBy(@Param("corpId") Long corpId, @Param("productId") Long productId);
}
