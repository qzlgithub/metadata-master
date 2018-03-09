package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

import java.io.Serializable;

public class OpenClientProductDTO extends RequestDTO
{
    private ClientProductDTO clientProductDTO;
    private ProductRechargeDTO productRechargeDTO;

    public ClientProductDTO getClientProductDTO()
    {
        return clientProductDTO;
    }

    public void setClientProductDTO(ClientProductDTO clientProductDTO)
    {
        this.clientProductDTO = clientProductDTO;
    }

    public ProductRechargeDTO getProductRechargeDTO()
    {
        return productRechargeDTO;
    }

    public void setProductRechargeDTO(ProductRechargeDTO productRechargeDTO)
    {
        this.productRechargeDTO = productRechargeDTO;
    }
}
