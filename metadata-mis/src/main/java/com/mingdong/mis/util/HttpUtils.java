package com.mingdong.mis.util;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils
{
    private static final String DEFAULT_CHARSET = "UTF-8";

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

    public static HttpEntity postData(String uri, Map<String, String> headers, String content) throws IOException
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
        HttpResponse response = client.execute(post);
        return response.getEntity();
    }

    public static void main(String[] args) throws IOException
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("auth_token", "sdf23j4klksdjflksd903248529483");
        HttpEntity entity = postData("https://api.dsdatas.com/blackData/fireGeneralizeBlack", headers,
                "{\"timestamp\":12141321312,\"name\":\"祝俊\",\"idCard\":\"330721198910115417\",\"phone\":\"18868429798\"}");
        System.out.println(EntityUtils.toString(entity, DEFAULT_CHARSET));
    }
}
