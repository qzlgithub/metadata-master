package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductTxt;

import java.util.List;

public interface ProductTxtMapper
{
    void add(ProductTxt productTxt);

    void delete(Long id);

    void updateById(ProductTxt productTxt);

    ProductTxt findById(Long id);

    List<ProductTxt> getAll();
}
