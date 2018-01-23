package com.mingdong.bop.domain;

import com.mingdong.bop.domain.entity.ApiReqInfo;
import com.mingdong.bop.domain.entity.ClientUser;
import com.mingdong.bop.domain.entity.DictProductType;
import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRequestInfoDTO;
import com.mingdong.core.model.dto.UserDTO;

public class TransformDTO
{
    public static void clientUserToDTO(ClientUser left,UserDTO right){
        right.setUserId(left.getId());
        right.setClientId(left.getClientId());
        right.setName(left.getName());
        right.setPhone(left.getPhone());
        right.setUsername(left.getUsername());
        right.setEnabled(left.getEnabled());
    }

    public static void dictProductTypeToDTO(DictProductType left, DictProductTypeDTO right)
    {
        right.setId(left.getId());
        right.setCode(left.getCode());
        right.setName(left.getName());
        right.setRemark(left.getRemark());
        right.setEnabled(left.getEnabled());
    }

    public static void productRechargeToDTO(ProductRechargeInfo left, ProductRechargeInfoDTO right)
    {
        right.setId(left.getId());
        right.setAmount(left.getAmount());
        right.setBalance(left.getBalance());
        right.setContractNo(left.getContractNo());
        right.setCorpName(left.getCorpName());
        right.setContractNo(left.getContractNo());
        right.setProductName(left.getProductName());
        right.setRechargeType(left.getRechargeType());
        right.setRemark(left.getRemark());
        right.setShortName(left.getShortName());
        right.setTradeNo(left.getTradeNo());
        right.setTradeTime(left.getTradeTime());
        right.setUsername(left.getUsername());
    }

    public static void productRequestToDTO(ApiReqInfo left, ProductRequestInfoDTO right)
    {
        right.setId(left.getId());
        right.setBalance(left.getBalance());
        right.setCorpName(left.getCorpName());
        right.setCallTime(left.getCallTime());
        right.setProductName(left.getProductName());
        right.setShortName(left.getShortName());
        right.setSuc(left.getSuc());
        right.setUnitAmt(left.getUnitAmt());
        right.setUsername(left.getUsername());
    }
}
