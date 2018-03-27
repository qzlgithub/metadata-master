package com.mingdong.mis.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.mingdong.mis.mongo.dao.impl",
                         mongoTemplateRef = AnalysisMongoConfig.MONGO_TEMPLATE)
public class AnalysisMongoConfig
{
    protected static final String MONGO_TEMPLATE = "analysisMongoTemplate";
}
