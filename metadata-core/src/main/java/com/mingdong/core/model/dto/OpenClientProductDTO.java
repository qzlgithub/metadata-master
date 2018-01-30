package com.mingdong.core.model.dto;

import java.io.Serializable;

public class OpenClientProductDTO implements Serializable
{
    private ClientProductDTO clientProductDTO;
    private ProductRechargeDTO productRechargeDTO;
    private boolean isYear;//是否包年形式

    public boolean isYear()
    {
        return isYear;
    }

    public void setYear(boolean year)
    {
        isYear = year;
    }

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
