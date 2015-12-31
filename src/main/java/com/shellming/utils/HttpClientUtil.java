package com.shellming.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ruluo1992 on 11/24/2015.
 */
public class HttpClientUtil {

    public static String doGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response  = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            response.close();
            return result;
        }finally {
            httpClient.close();
        }
    }

    public static String doGetWithHttps(String url, String username, String passwd) throws Exception{
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, passwd);
        provider.setCredentials(AuthScope.ANY, credentials);

        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient client = HttpClients.custom().
                setSSLSocketFactory(sslsf).
                setDefaultCredentialsProvider(provider).
                build();

        // create the HTTP Post operation
        HttpGet httpGet = new HttpGet(url);
        String up = username + ":" + passwd;
        String header = new String(Base64.encodeBase64(up.getBytes()));
        httpGet.setHeader("X-SyncTimeOut", "30");
        httpGet.setHeader("Authorization", " Basic " + header);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity =  response.getEntity();
        return entity.toString();
    }

    public static String doGet() throws Exception {
        URL localURL = new URL("https://449ed3cf-b721-4d15-b8ff-ee932aecc63a:Rb9LMNclIq@twcservice.mybluemix.net/api/weather/v2/observations/current?units=m&geocode=34.53%2C84.50&language=en-US");
        HttpsURLConnection connection = (HttpsURLConnection) localURL.openConnection();
//        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);

        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (connection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
        }

        try {
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }


    public static void main(String[] args) {
        String oneUrl = "https://449ed3cf-b721-4d15-b8ff-ee932aecc63a:Rb9LMNclIq@twcservice.mybluemix.net:443/api/weather/v2/observations/current?units=m&geocode=34.53%2C84.50&language=en-US";
        String url = "https://twcservice.mybluemix.net:443/api/weather/v2/observations/current?units=m&geocode=34.53%2C84.50&language=en-US";
        String username = "449ed3cf-b721-4d15-b8ff-ee932aecc63a";
        String passwd = "Rb9LMNclIq";
        try {
//            System.out.println(doGetWithHttps(oneUrl, username, passwd));
            System.out.println(doGet(oneUrl));
//            doGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
