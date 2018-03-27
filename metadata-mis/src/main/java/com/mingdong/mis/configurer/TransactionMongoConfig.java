package com.mingdong.mis.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.mingdong.mis.mongo.dao.impl",
                         mongoTemplateRef = TransactionMongoConfig.MONGO_TEMPLATE)
public class TransactionMongoConfig
{
    protected static final String MONGO_TEMPLATE = "transactionMongoTemplate";
}
