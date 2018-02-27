package com.mingdong.core.model.dto;

import java.io.Serializable;

public class OpenClientProductDTO implements Serializable
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
