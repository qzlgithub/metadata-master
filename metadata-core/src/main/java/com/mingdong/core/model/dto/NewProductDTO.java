package com.mingdong.core.model.dto;

import java.io.Serializable;

public class NewProductDTO implements Serializable
{
    private ProductDTO productDTO;
    private ProductTxtDTO productTxtDTO;

    public ProductDTO getProductDTO()
    {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO)
    {
        this.productDTO = productDTO;
    }

    public ProductTxtDTO getProductTxtDTO()
    {
        return productTxtDTO;
    }

    public void setProductTxtDTO(ProductTxtDTO productTxtDTO)
    {
        this.productTxtDTO = productTxtDTO;
    }
}
