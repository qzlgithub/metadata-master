package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Product;

import java.util.List;

public interface ProductMapper
{
    void add(Product product);

    void updateById(Product product);

    Product findById(Long id);

    Product findByCode(String code);

    List<Product> getListByStatus(Integer enabled);

    Product findByName(String name);
}
