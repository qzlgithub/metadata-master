package com.mingdong.mis.util;

import com.mingdong.core.exception.MetadataHttpException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils
{
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils()
    {
    }

    public static HttpEntity get(String uri, Map<String, String> headers, Map<String, Object> data) throws IOException
    {
        HttpClient client = HttpConnectionManager.getHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        for(Map.Entry entry : data.entrySet())
        {
            params.add(new BasicNameValuePair(entry.getKey() + "", entry.getValue() + ""));
        }
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
        HttpGet get = new HttpGet(uri + "?" + str);
        if(headers != null)
        {
            for(Map.Entry<String, String> entry : headers.entrySet())
            {
                get.addHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse response = client.execute(get);
        return response.getEntity();
    }

    public static HttpEntity postForm(String uri, Map<String, Object> data) throws IOException
    {
        HttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> form = new ArrayList<>();
        for(Map.Entry entry : data.entrySet())
        {
            form.add(new BasicNameValuePair(entry.getKey() + "", entry.getValue() + ""));
        }
        post.setEntity(new UrlEncodedFormEntity(form, DEFAULT_CHARSET));
        HttpResponse response = client.execute(post);
        return response.getEntity();
    }

    public static HttpEntity postData(String uri, Map<String, String> headers, String content)
            throws MetadataHttpException
    {
        HttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(content, DEFAULT_CHARSET));
        if(headers != null)
        {
            for(Map.Entry<String, String> entry : headers.entrySet())
            {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try
        {
            HttpResponse response = client.execute(post);
            return response.getEntity();
        }
        catch(IOException e)
        {
            logger.error("Failed to do http post request to {}", uri);
            throw new MetadataHttpException("error to do http post request");
        }
    }
}
