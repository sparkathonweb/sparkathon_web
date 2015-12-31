package com.shellming.tasks;

import com.shellming.models.Location;
import com.shellming.utils.CityUtil;
import com.shellming.utils.HttpClientUtil;
import com.shellming.utils.ObjectStorageUtil;
import com.shellming.utils.OuterStorageUtil;
import org.apache.commons.codec.binary.Base64;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.Access;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ruluo1992 on 12/14/2015.
 */
@Component
@Lazy(false)
public class DataFetchTask {

//    @Scheduled(cron = "*/10 * * * * *")
    public void testTask(){
        System.out.println("!!!!!!!! one task " + System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
        Date date = new Date();
        String time = format.format(date);
        try {
            OuterStorageUtil.save(time, System.currentTimeMillis() + "\r\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    * 每隔两小时抓取全国省会城市天气数据一次， 34 * 8 = 272， 一天请求限制500次，每隔1分钟请求一次
    * */
//    @Scheduled(cron = "0 0 */3 * * *")
    public void fetchData(){
        String baseUrl = "https://449ed3cf-b721-4d15-b8ff-ee932aecc63a:Rb9LMNclIq@twcservice.mybluemix.net:443/api/weather/v2/observations/current?units=m&geocode=%s,%s&language=en-US";
        List<String> provinces = CityUtil.listProvinces();
        int retry = 0;
        int maxRetry = 5;
        for(int i = 0; i < provinces.size(); i++){
            String province = provinces.get(i);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
            Date date = new Date();
            String time = format.format(date);
            String key = time + "-" + province;
            String data = "";
            try {
                key = Base64.encodeBase64String(key.getBytes("utf-8"));
                Location location = CityUtil.getLocation(province);
                String url = String.format(baseUrl, location.getLatitude(), location.getLongitude());
                data = HttpClientUtil.doGet(url);
                OuterStorageUtil.save(key, data);
            } catch (Exception e) {
                retry++;
                if(retry > maxRetry){       // 超过重试次数
                    retry = 0;
                    key = key + "error";
                    data = exceptionStacktraceToString(e);
                    OuterStorageUtil.save(key, data);
                }
                else{
                    i--;
                }
            }
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String exceptionStacktraceToString(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

    public static void main(String[] args) {
//        DataFetchTask task = new DataFetchTask();
//        task.fetchData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
        Date date = new Date();
        String time = format.format(date);
        System.out.println(time);
    }
}
