package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductClientInfo;

import java.util.List;

public interface ProductClientInfoMapper
{
    List<ProductClientInfo> getListByClient(Long clientId);

    ProductClientInfo getClientProductInfo(Long clientProductId);
}
