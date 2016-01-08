package com.shellming.controllers;

import com.google.gson.Gson;
import com.shellming.models.RunExpModel;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.StreamingContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 1/3/2016.
 */
@Controller
@RequestMapping(value = "/exp")
public class ExpCalController extends BaseController{

    @RequestMapping(value = "cal")
    public void cal(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            while(true){
                String line = reader.readLine();
                if(line == null)
                    break;
                sb.append(line);
            }
            System.out.println("!!!!!!! get data " + sb.toString());

            List<Double> result = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                result.add(1.0 * i);
            }
            Gson gson = new Gson();
            String responseStr = gson.toJson(result);
            sendResponseAsJson(response, responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "test")
    public void testSpark(){

    }

    public static void main(String[] args) {
        JavaSparkContext context = new JavaSparkContext("spark://192.168.57.129:7077", "sparkathon");
//        context.addFile("test.txt");
        JavaRDD rdd = context.textFile("text.txt");
//        rdd.persist(StorageLevel.DISK_ONLY());
//        Configuration conf = context.hadoopConfiguration();
//        StreamingContext ssc = new StreamingContext();
//        ssc.textFileStream();

    }
}
