package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductInfo;

import java.util.List;

public interface ProductInfoMapper
{
    List<ProductInfo> getAll();

    int countAll();
}
