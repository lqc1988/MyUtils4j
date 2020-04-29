package com.lqc.test;

import com.alibaba.fastjson.JSONObject;
import com.lqc.util.CommonUtil;
import com.lqc.util.HttpUtils;
import com.lqc.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * @author: liqinchao
 * @Date: 2020/1/16 15:27
 * @Description:
 */
public class CallShopXX {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    /**
     * 请求shop++商品更新接口
     * 商品模块 / 商品更新接口
     * 地址：api/product/update
     * 类型：POST
     *
     * @param id    商品ID
     * @param name  商品名称
     * @param price 商品价格，保留两位有效数字
     * @return
     * @throws Exception
     */
    public static void callShopXXProductUpdate(String id, String name, String price, String grantAppKey) throws Exception {
        String opt = "请求shop++商品更新接口，";
        String url =  "https://xxx/api/product/edit";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("name", name);
        paramMap.put("price", price);
        paramMap.put("grantAppKey", grantAppKey);
        SecurityUtil.encrypt4ShopXX(paramMap);
        JSONObject paramJson = new JSONObject(paramMap);
        logger.info(opt + "start，入参：{}", paramJson);
        String resp = HttpUtils.post(url, null, paramMap);
        logger.info(opt + "end，入参：{}，响应：{}", paramJson, resp);
    }

    public static void main(String[] args) {
        try {
            File csv=new File("D:\\t\\116.csv");
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String lineDta = "";

            while ((lineDta = textFile.readLine()) != null){
                System.out.println(lineDta);
                String[] data=lineDta.split(",");
                String id=data[0];
                String name=data[1];
                String price=data[2];
                String key=data[3];
                callShopXXProductUpdate(id,name,price,key);
            }
            //callShopXXProductUpdate("2020011626672","停不下来的“大师兄”",
            //        new BigDecimal("30.00"),"c1dfc45857954622865d6a5d112fc65f");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
