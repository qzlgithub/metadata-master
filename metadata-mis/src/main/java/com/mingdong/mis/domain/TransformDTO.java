package com.mingdong.mis.domain;

import com.mingdong.core.model.dto.ClientAccountDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRequestInfoDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.mis.domain.entity.ApiReqInfo;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientAccount;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.DictProductType;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;

public class TransformDTO
{
    public static void clientAccountDTOToDomain(ClientAccountDTO left, ClientAccount right){
        right.setId(left.getId());
        right.setBalance(left.getBalance());
        right.setCreateTime(left.getCreateTime());
        right.setEnabled(left.getEnabled());
        right.setUpdateTime(left.getUpdateTime());
    }

    public static void clientAccountToDTO(ClientAccount left,ClientAccountDTO right){
        right.setBalance(left.getBalance());
        right.setCreateTime(left.getCreateTime());
        right.setEnabled(left.getEnabled());
        right.setId(left.getId());
        right.setUpdateTime(left.getUpdateTime());
    }

    public static void clientUserToDTO(ClientUser left,ClientUserDTO right){
        right.setId(left.getId());
        right.setEnabled(left.getEnabled());
        right.setAppKey(left.getAppKey());
        right.setPassword(left.getPassword());
        right.setUpdateTime(left.getUpdateTime());
        right.setDeleted(left.getDeleted());
        right.setName(left.getName());
        right.setPhone(left.getPhone());
        right.setUsername(left.getUsername());
        right.setClientId(left.getClientId());
        right.setCreateTime(left.getCreateTime());
        right.setEmail(left.getEmail());
    }

    public static void clientUserDTOToDomain(ClientUserDTO left,ClientUser right){
        right.setEnabled(left.getEnabled());
        right.setId(left.getId());
        right.setAppKey(left.getAppKey());
        right.setPassword(left.getPassword());
        right.setUpdateTime(left.getUpdateTime());
        right.setDeleted(left.getDeleted());
        right.setName(left.getName());
        right.setPhone(left.getPhone());
        right.setUsername(left.getUsername());
        right.setClientId(left.getClientId());
        right.setCreateTime(left.getCreateTime());
        right.setEmail(left.getEmail());
    }

    public static void clientDTOToDomain(ClientDTO left,Client right){
        right.setAccountQty(left.getAccountQty());
        right.setId(left.getId());
        right.setUpdateTime(left.getUpdateTime());
        right.setCorpName(left.getCorpName());
        right.setCreateTime(left.getCreateTime());
        right.setDeleted(left.getDeleted());
        right.setIndustryId(left.getIndustryId());
        right.setLicense(left.getLicense());
        right.setManagerId(left.getManagerId());
        right.setPrimaryUserId(left.getPrimaryUserId());
        right.setShortName(left.getShortName());
    }

    public static void clientToDTO(Client left,ClientDTO right){
        right.setId(left.getId());
        right.setAccountQty(left.getAccountQty());
        right.setUpdateTime(left.getUpdateTime());
        right.setCorpName(left.getCorpName());
        right.setCreateTime(left.getCreateTime());
        right.setDeleted(left.getDeleted());
        right.setIndustryId(left.getIndustryId());
        right.setLicense(left.getLicense());
        right.setManagerId(left.getManagerId());
        right.setPrimaryUserId(left.getPrimaryUserId());
        right.setShortName(left.getShortName());
    }

    public static void dictIndustryToDTO(DictIndustry left,DictIndustryDTO right){
        right.setCode(left.getCode());
        right.setCreateTime(left.getCreateTime());
        right.setEnabled(left.getEnabled());
        right.setId(left.getId());
        right.setName(left.getName());
        right.setParentId(left.getParentId());
        right.setSeqNo(left.getSeqNo());
        right.setUpdateTime(left.getUpdateTime());
    }

    public static void clientInfoToDTO(ClientInfo left,ClientInfoDTO right){
        right.setAccountEnabled(left.getAccountEnabled());
        right.setAccountQty(left.getAccountQty());
        right.setClientId(left.getClientId());
        right.setCorpName(left.getCorpName());
        right.setEmail(left.getEmail());
        right.setIndustryId(left.getIndustryId());
        right.setLicense(left.getLicense());
        right.setManagerName(left.getManagerName());
        right.setName(left.getName());
        right.setPhone(left.getPhone());
        right.setRegisterTime(left.getRegisterTime());
        right.setShortName(left.getShortName());
        right.setUserEnabled(left.getUserEnabled());
        right.setUsername(left.getUsername());
    }

    public static void userToDTO(ClientUser left, UserDTO right)
    {
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
