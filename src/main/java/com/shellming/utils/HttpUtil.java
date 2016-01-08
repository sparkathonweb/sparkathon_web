package com.shellming.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 1/7/2016.
 */
public class HttpUtil {
    public static void doPost(Map<String,String> data, String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for(String key : data.keySet()){
            nvps.add(new BasicNameValuePair(key, data.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            httpPost.setHeader("charset", "utf-8");
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
