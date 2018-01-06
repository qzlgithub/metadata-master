package com.mingdong.bop.model;

public class ImageCode
{
    private String code;
    private String base64Code;

    public ImageCode(String code, String base64Code)
    {
        this.code = code;
        this.base64Code = base64Code;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getBase64Code()
    {
        return base64Code;
    }

    public void setBase64Code(String base64Code)
    {
        this.base64Code = base64Code;
    }
}
