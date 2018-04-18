package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_register_platform")
public class FinRegisterPlatform
{
    @Field("platform_code")
    private String platformCode;
    @Field("platform_type")
    private String platformType;
    @Field("register_date")
    private String registerDate;

    public String getPlatformCode()
    {
        return platformCode;
    }

    public void setPlatformCode(String platformCode)
    {
        this.platformCode = platformCode;
    }

    public String getPlatformType()
    {
        return platformType;
    }

    public void setPlatformType(String platformType)
    {
        this.platformType = platformType;
    }

    public String getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(String registerDate)
    {
        this.registerDate = registerDate;
    }
}
