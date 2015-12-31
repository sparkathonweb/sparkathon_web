package com.shellming.controllers;

import com.shellming.tasks.DataFetchTask;
import com.shellming.utils.HttpClientUtil;
import com.shellming.utils.ObjectStorageUtil;
import com.shellming.utils.OuterStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by ruluo1992 on 11/24/2015.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        String oneUrl = "https://449ed3cf-b721-4d15-b8ff-ee932aecc63a:Rb9LMNclIq@twcservice.mybluemix.net:443/api/weather/v2/observations/current?units=m&geocode=34.53%2C84.50&language=en-US";
        try {
            return HttpClientUtil.doGet(oneUrl);
        } catch (IOException e) {
            return e.toString();
        }
    }

    @RequestMapping(value = "/fetch")
    @ResponseBody
    public String fetchData(){
        DataFetchTask task = new DataFetchTask();
        task.fetchData();
        return "success";
    }

    @RequestMapping(value = "/output")
    @ResponseBody
    public String testOutput(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! enter output");
        OuterStorageUtil.save("中文测试", "中文测试");
        return "output";
    }
}
