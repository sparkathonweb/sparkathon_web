package com.shellming.controllers;

import com.shellming.utils.ObjectStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ruluo1992 on 12/15/2015.
 */
@Controller
@RequestMapping(value = "/storage")
public class StorageController {

    @RequestMapping(value = "put")
    @ResponseBody
    public String put(HttpServletRequest request){
        ObjectStorageUtil objectStorageUtil = new ObjectStorageUtil();
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        objectStorageUtil.put(key, value);
        return "success";
    }

    @RequestMapping(value = "list", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String list(){
        ObjectStorageUtil objectStorageUtil = new ObjectStorageUtil();
        List<String> keys = objectStorageUtil.list();
        StringBuilder sb = new StringBuilder();
        for(String key : keys){
            sb.append(key).append("\r\n");
        }
        return sb.toString();
    }

    @RequestMapping(value = "get")
    @ResponseBody
    public String get(HttpServletRequest request){
        String key = request.getParameter("key");
        ObjectStorageUtil objectStorageUtil = new ObjectStorageUtil();
        if(StringUtils.isEmpty(key)){
            return "empty key";
        }
        return objectStorageUtil.get(key);
    }
}
