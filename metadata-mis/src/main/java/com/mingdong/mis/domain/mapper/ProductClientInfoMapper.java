package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductClientInfoMapper
{
    List<ProductClientInfo> getListByClient(Long clientId);

    ProductClientInfo getClientProductInfo(Long clientProductId);

    List<ProductClientInfo> getClientDictList(Long clientId);

    // TODO deleted
    List<ProductClientInfo> getInfoListBy(@Param("clientId") Long clientId, @Param("isOpen") Integer isOpen,
            @Param("selectedType") Integer[] selectedType);

    int countInfoListBy(@Param("clientId") Long clientId, @Param("isOpen") Integer isOpen,
            @Param("selectedType") Integer[] selectedType);

    List<ProductClientInfo> getClientProductCustomBy(Long clientId);

    int countBy(@Param("clientId") Long clientId, @Param("typeIdList") List<Integer> typeIdList,
            @Param("incOpened") Integer incOpened);

    List<ProductClientInfo> getListBy(@Param("clientId") Long clientId, @Param("typeIdList") List<Integer> typeIdList,
            @Param("incOpened") Integer incOpened);

    int countByClientOpened(@Param("clientId") Long clientId, @Param("opened") Integer opened);

    List<ProductClientInfo> getListByClientOpened(@Param("clientId") Long clientId, @Param("opened") Integer opened);
}
