package com.mingdong.mis.configurer;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties
{
    private MongoProperties analysis = new MongoProperties();
    private MongoProperties transaction = new MongoProperties();

    public MongoProperties getAnalysis()
    {
        return analysis;
    }

    public void setAnalysis(MongoProperties analysis)
    {
        this.analysis = analysis;
    }

    public MongoProperties getTransaction()
    {
        return transaction;
    }

    public void setTransaction(MongoProperties transaction)
    {
        this.transaction = transaction;
    }
}
