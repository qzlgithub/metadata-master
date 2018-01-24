package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductTxt;

public interface ProductTxtMapper
{
    void add(ProductTxt productTxt);

    void updateById(ProductTxt productTxt);

    ProductTxt findById(Long id);

}
