package com.mingdong.mis.configurer;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import javax.annotation.Resource;

@Configuration
public class MultipleMongoConfig
{
    @Resource
    private MultipleMongoProperties multipleMongoProperties;

    @Primary
    @Bean(name = AnalysisMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate analysisMongoTemplate() throws Exception
    {
        return new MongoTemplate(analysisFactory(this.multipleMongoProperties.getAnalysis()));
    }

    @Bean
    @Qualifier(TransactionMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate transactionMongoTemplate() throws Exception
    {
        return new MongoTemplate(transactionFactory(this.multipleMongoProperties.getTransaction()));
    }

    @Bean
    @Primary
    public MongoDbFactory analysisFactory(MongoProperties mongo) throws Exception
    {
        return new SimpleMongoDbFactory(new MongoClientURI(mongo.getUri()));
    }

    @Bean
    public MongoDbFactory transactionFactory(MongoProperties mongo) throws Exception
    {
        return new SimpleMongoDbFactory(new MongoClientURI(mongo.getUri()));
    }
}
