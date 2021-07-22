package com.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

/**
 * @ClassName TestString
 * @Description TODO
 * @Author liqinchao
 * @Date 2021/7/6 11:34
 * @Version 1.0
 **/
public class TestString {
    public static void main(String[] args) {
        String req = "[{\"clientData\":{\"platform\":\"\",\"version\":\"2aa\"},\"request\":{\"page\":1,\"size\":10}}]";
        String ver = null;
        if (req.contains("version")) {
            ver=req.substring(req.indexOf("version"),req.lastIndexOf("version\":.*\"{2}"));
        }
        System.out.println(ver);
    }
}
