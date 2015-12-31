package com.shellming.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 12/15/2015.
 */
public class OuterStorageUtil {
    public static final String URL = "http://45.78.49.159/storage";

    public static void save(String key, String value) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("key", key));
            nvps.add(new BasicNameValuePair("value", value));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            httpPost.setHeader("charset", "utf-8");
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String str = "MjAxNS0xMi0xNi0wMy3lhoXokpnlj6Q=";
        try {
            String decode = new String(Base64.decodeBase64(str), "utf-8");
            System.out.println(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
