package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProdConsDetail;

import java.util.List;

public interface ProdConsDetailMapper
{
    int countByProduct(Long productId);

    List<ProdConsDetail> getAllByProduct(Long productId);
}
