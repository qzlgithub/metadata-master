package com.mingdong.mis.util;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class HttpConnectionManager
{
    private static PoolingHttpClientConnectionManager connectionManager;

    @PostConstruct
    private void init()
    {
        try
        {
            //添加对https的支持，该sslContext没有加载客户端证书
            // 如果需要加载客户端证书，请使用如下sslContext,其中KEYSTORE_FILE和KEYSTORE_PASSWORD分别是你的证书路径和证书密码
            //KeyStore keyStore  =  KeyStore.getInstance(KeyStore.getDefaultType()
            //FileInputStream instream =   new FileInputStream(new File(KEYSTORE_FILE));
            //keyStore.load(instream, KEYSTORE_PASSWORD.toCharArray());
            //SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,KEYSTORE_PASSWORD.toCharArray())
            // .loadTrustMaterial(null, new TrustSelfSignedStrategy())
            //.build();
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf).register("http",
                            PlainConnectionSocketFactory.getSocketFactory()).build();
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(50);
            connectionManager.setDefaultMaxPerRoute(25);
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e)
        {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient()
    {
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }
}
