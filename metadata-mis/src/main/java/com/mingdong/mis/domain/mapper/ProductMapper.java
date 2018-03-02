package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper
{
    void add(Product product);

    void updateById(Product product);

    Product findById(Long id);

    List<Product> getListByStatus(Integer enabled);

    Product findByName(String name);

    void updateSkipNull(Product product);

    int countBy(@Param("keyword") String keyword, @Param("type") Integer type, @Param("custom") Integer custom,
            @Param("enabled") Integer enabled);

    List<Product> getListBy(@Param("keyword") String keyword, @Param("type") Integer type,
            @Param("custom") Integer custom, @Param("enabled") Integer enabled);
}
