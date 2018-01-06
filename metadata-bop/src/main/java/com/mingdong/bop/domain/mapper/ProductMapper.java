package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Product;

import java.util.List;

public interface ProductMapper
{
    void add(Product product);

    void delete(Long id);

    void updateById(Product product);

    Product findById(Long id);

    List<Product> getAll();

    int countAll();

    Product findByCode(String code);

    void updateSkipNull(Product product);

    List<Product> getListByStatus(Integer enabled);
}
