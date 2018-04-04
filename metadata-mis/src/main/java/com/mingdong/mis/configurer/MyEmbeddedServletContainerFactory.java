package com.mingdong.mis.configurer;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class MyEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory
{
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)
    {
        return super.getEmbeddedServletContainer(initializers);
    }

    protected void customizeConnector(Connector connector)
    {
        super.customizeConnector(connector);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        //最大连接数
        protocol.setMaxConnections(1000);
        //最大线程数
        protocol.setMaxThreads(1000);
        protocol.setAcceptCount(1000);
        //初始线程数
        protocol.setMinSpareThreads(100);
        //连接超时时间
        protocol.setConnectionTimeout(20000);
    }
}
