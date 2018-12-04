package com.may.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : may
 * CreateTime : 2018/11/29 11:52
 * PackageName: com.may.utils
 * Description :Http Client工具类
 */
public class HttpUtils {
    private static Logger logger = LogManager.getLogger("utils.HttpUtils");

    /**
     * 发送 post请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> headMap, Map<String, String> paramMap)
            throws Exception {
        return post(url, headMap, paramMap, null);
    }

    /**
     * 发送 post请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @param body     消息体：Json格式的字符串
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> headMap, Map<String, String> paramMap, String body)
            throws Exception {
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (null != headMap) {
                for(Map.Entry<String,String> entry:headMap.entrySet()){
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //打印headers
            for (Header header : httpPost.getAllHeaders()) {
                System.out.println("post header-->" + header.getName() + " : " + header.getValue());
                logger.debug("post header-->" + header.getName() + " : " + header.getValue());
            }
            List<NameValuePair> paraList = new ArrayList<>();
            if (null != paramMap) {
                for(Map.Entry<String,String> entry:paramMap.entrySet()){
                    paraList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                    System.out.println("post入参-->"+entry.getKey() + " : " + entry.getValue());
                    logger.debug("post入参--> "+entry.getKey() + " : " + entry.getValue());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(paraList, "UTF-8"));
            if (StringUtils.isNotBlank(body)) {
                httpPost.setEntity(new StringEntity(body, "UTF-8"));
            }
            logger.debug("executing post " + httpPost.getURI());
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                logger.debug("executing post result" + result);
                System.out.println("--------------------------------------");
                System.out.println("Response content: " + result);
                System.out.println("--------------------------------------");
            }
        } catch (Exception e) {
            logger.error("执行post异常：", e);
            e.printStackTrace();
            throw e;
        } finally {
            closeConn(response, httpclient);
        }
        return result;
    }

    /**
     * 发送 get请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return
     */
    public static String get(String url, Map<String, String> headMap, Map<String, String> paramMap) {
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder uri_b = new URIBuilder(url);
            // 参数
            if (null != paramMap) {
                for(Map.Entry<String,String> entry:paramMap.entrySet()){
                    uri_b.setParameter(entry.getKey(), entry.getValue());
                    System.out.println("get入参-->"+entry.getKey() + " : " + entry.getValue());
                    logger.debug("get入参--> "+entry.getKey() + " : " + entry.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(uri_b.build());
            // 追加headers
            if (null != headMap) {
                for (String key : headMap.keySet()) {
                    httpGet.setHeader(key, headMap.get(key));
                }
            }
            //打印headers
            for (Header header : httpGet.getAllHeaders()) {
                System.out.println("get header-->" + header.getName() + " : " + header.getValue());
                logger.debug("get header-->" + header.getName() + " : " + header.getValue());
            }
            System.out.println("执行get请求uri：" + httpGet.getURI());
            logger.debug("执行get请求uri：" + httpGet.getURI());
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 打印响应内容长度
//					System.out.println("Response content length: "+ entity.getContentLength());
                result = EntityUtils.toString(entity);
                // 打印响应内容
                System.out.println("Response content: \n" + result);
            }
        } catch (Exception e) {
            logger.error("执行get异常：", e);
            e.printStackTrace();
        } finally {
            closeConn(response, httpclient);
        }
        return result;
    }

    /**
     * 关闭相关连接
     *
     * @param response
     * @param httpclient
     */
    private static void closeConn(CloseableHttpResponse response, CloseableHttpClient httpclient) {
        try {
            if (null != response) {
                response.close();
            }
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("关闭连接异常：",e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channelCode", "GGYY01BJ78800");
        get("https://center-hxcpms.234g.cn/api/business/checkChannel", null, paramMap);
    }
}
