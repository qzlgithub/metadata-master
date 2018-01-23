package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductTxt;

public interface ProductTxtMapper
{
    void add(ProductTxt productTxt);

    void updateById(ProductTxt productTxt);

    ProductTxt findById(Long id);

}
