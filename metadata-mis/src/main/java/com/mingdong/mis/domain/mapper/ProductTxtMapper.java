package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductTxt;

public interface ProductTxtMapper
{
    void add(ProductTxt o);

    void updateById(ProductTxt o);

    ProductTxt findById(Long id);

}
