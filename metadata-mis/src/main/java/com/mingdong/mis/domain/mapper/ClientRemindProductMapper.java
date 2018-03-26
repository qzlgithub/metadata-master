package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientRemindProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientRemindProductMapper
{
    void addList(List<ClientRemindProduct> list);

    void deleteByRemindIds(@Param("remindIds") List<Long> remindIds);

    void disposeByClientRemindId(Long remindId);
}
