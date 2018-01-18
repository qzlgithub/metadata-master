package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductClientInfoMapper
{
    int countListByClient(Long clientId);

    List<ProductClientInfo> getListByClient(Long clientId);

    ProductClientInfo getClientProductInfo(Long clientProductId);

    List<ProductClientInfo> getClientDictList(Long clientId);

    List<ProductClientInfo> getInfoListBy(@Param("clientId") Long clientId, @Param("isOpen") Integer isOpen,
            @Param("selectedType") Integer[] selectedType);

    int countInfoListBy(@Param("clientId") Long clientId, @Param("isOpen") Integer isOpen,
            @Param("selectedType") Integer[] selectedType);
}
