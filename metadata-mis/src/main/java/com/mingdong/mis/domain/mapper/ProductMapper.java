package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper
{
    void add(Product o);

    void updateById(Product o);

    void updateSkipNull(Product o);

    Product findById(Long id);

    Product findByName(String name);

    List<Product> getListByStatus(Integer enabled);

    int countBy(@Param("keyword") String keyword, @Param("type") Integer type, @Param("custom") Integer custom,
            @Param("enabled") Integer enabled);

    List<Product> getListBy(@Param("keyword") String keyword, @Param("type") Integer type,
            @Param("custom") Integer custom, @Param("enabled") Integer enabled);

    int countByType(@Param("typeIdList") List<Integer> productTypeList, @Param("custom") Integer custom);

    List<Product> getListByType(@Param("typeIdList") List<Integer> productTypeList, @Param("custom") Integer custom);

}
