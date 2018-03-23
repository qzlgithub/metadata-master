package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "request_log")
public class RequestLog
{
    @Id
    private String requestNo;
    @Field("timestamp")
    private Date timestamp;
    @Field("client_id")
    private Long clientId;
    @Field("client_user_id")
    private Long clientUserId;
    @Field("product_id")
    private Long productId;
    @Field("host")
    private String host;
    @Field("request_params")
    private Object requestParams;
    @Field("hit")
    private Integer hit;
    @Field("bill_plan")
    private Integer billPlan;
    @Field("fee")
    private Long fee;
    @Field("balance")
    private Long balance;

    @Field("corp_name")
    private String corpName;
    @Field("request_username")
    private String requestUsername;
    @Field("primary_username")
    private String primaryUsername;
    @Field("product_name")
    private String productName;

    public String getRequestNo()
    {
        return requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getClientUserId()
    {
        return clientUserId;
    }

    public void setClientUserId(Long clientUserId)
    {
        this.clientUserId = clientUserId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Object getRequestParams()
    {
        return requestParams;
    }

    public void setRequestParams(Object requestParams)
    {
        this.requestParams = requestParams;
    }

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public Long getFee()
    {
        return fee;
    }

    public void setFee(Long fee)
    {
        this.fee = fee;
    }

    public Long getBalance()
    {
        return balance;
    }

    public void setBalance(Long balance)
    {
        this.balance = balance;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public String getRequestUsername()
    {
        return requestUsername;
    }

    public void setRequestUsername(String requestUsername)
    {
        this.requestUsername = requestUsername;
    }

    public String getPrimaryUsername()
    {
        return primaryUsername;
    }

    public void setPrimaryUsername(String primaryUsername)
    {
        this.primaryUsername = primaryUsername;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }
}
