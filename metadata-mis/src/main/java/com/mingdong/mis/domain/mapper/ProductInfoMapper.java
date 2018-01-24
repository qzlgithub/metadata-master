package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductInfo;

import java.util.List;

public interface ProductInfoMapper
{
    List<ProductInfo> getAll();

    int countAll();
}
