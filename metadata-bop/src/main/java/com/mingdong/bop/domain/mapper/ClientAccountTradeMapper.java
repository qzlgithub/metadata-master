package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientAccountTrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClientAccountTradeMapper
{
    void add(ClientAccountTrade clientAccountTrade);

    void delete(Long id);

    int countAll();

    void updateById(ClientAccountTrade clientAccountTrade);

    ClientAccountTrade findById(Long id);

    List<ClientAccountTrade> getAll();

    //多表关联封装到map里面返回
    List<Map<String, Object>> findAll();

    int countByIncome(Integer income);

    int countBy(@Param("corpId") Long corpId, @Param("income") Integer income);
}
