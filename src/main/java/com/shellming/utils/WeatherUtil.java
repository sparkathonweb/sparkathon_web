package com.shellming.utils;

import com.google.gson.Gson;
import com.shellming.models.RunExpModel;
import com.shellming.models.Weather;
import com.shellming.models.WeatherObservationCurrent;
import com.shellming.models.WeatherResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.spark.storage.StorageUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 1/7/2016.
 */
public class WeatherUtil {
    public static void main(String[] args) {
        String dir = "F:\\Courseware\\高级软件工程\\data\\2015-12-25";
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles();
        StringBuilder sb = new StringBuilder();
        try {
            for(File file : files){
                String fileName = file.getName();
                System.out.println(fileName);
                System.out.println(new String(Base64.decodeBase64(fileName), "utf-8"));
                fileName = new String(Base64.decodeBase64(fileName), "utf-8");
                String content = IOUtils.toString(new FileReader(file));
//                System.out.println(content);
                Gson gson = new Gson();
                WeatherResponse response = gson.fromJson(content, WeatherObservationCurrent.class);
                List<Weather> weathers = response.getWeatherData();
                Weather weather = weathers.get(0);
                RunExpModel model = RunExpModel.fromWeather(weather);
                String modelJson = gson.toJson(model);
                System.out.println(modelJson);
                sb.append(modelJson).append("\n");
            }
            File target = new File(dir, "target");
            BufferedWriter writer = new BufferedWriter(new FileWriter(target));
            writer.write(sb.toString());
            writer.close();
//            String value = URLEncoder.encode(sb.toString(), "utf-8");
//            System.out.println(value);
//            Map<String, String> data = new HashMap<>();
//            data.put("key", "target");
//            data.put("value", sb.toString());
//            HttpUtil.doPost(data, "http://sparkathon.mybluemix.net/storage/put");
//            storageUtil.put("target", sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
