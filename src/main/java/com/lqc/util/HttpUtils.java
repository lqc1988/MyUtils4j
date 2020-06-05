package com.lqc.util;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName : HttpUtils
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : Http Client工具类
 */
public class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private static HttpPost assembleHttpPost(String url, Map<String, String> headMap, Map<String, Object> paramMap,
                                    String body) throws Exception{
        String opt = "发送POST请求";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paraList = new ArrayList<>();
        if (null != paramMap) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                paraList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
                logger.debug(opt + ",入参==>" + entry.getKey() + ":" + entry.getValue());
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paraList, "UTF-8"));
        if (null != headMap) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        for (Header header : httpPost.getAllHeaders()) {
            logger.debug(opt + ",header==>" + header.getName() + ":" + header.getValue());
        }
        if (StringUtils.isNotBlank(body)) {
            logger.debug(opt+"body==> " + body);
            httpPost.setEntity(new StringEntity(body, "UTF-8"));
        }
        logger.debug(opt + ",URI==>" + httpPost.getURI());
        return httpPost;
    }
    /**
     * 发送POST请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> headMap, Map<String, Object> paramMap)
            throws Exception {
        return post(url, headMap, paramMap, null);
    }

    /**
     * 发送POST请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @param body     消息体：Json格式的字符串
     * @return String
     * @throws Exception
     */
    public static String post(String url, Map<String, String> headMap, Map<String, Object> paramMap,
                              String body) throws Exception {
        String opt = "发送POST请求";
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost =assembleHttpPost(url,headMap,paramMap,body);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                logger.debug(opt + ",响应==>" + result);
            }
        } catch (Exception e) {
            logger.error(opt + ",异常：", e);
            e.printStackTrace();
            throw e;
        } finally {
            closeConn(response, httpClient);
        }
        return result;
    }

    /**
     * 发送POST请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return byte[]
     */
    public static byte[] postByte(String url, Map<String, String> headMap,
                                  Map<String, Object> paramMap)  throws Exception {
        String opt = "发送POST请求";
        byte[] result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost =assembleHttpPost(url,headMap,paramMap,null);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toByteArray(entity);
                logger.debug(opt + ",响应==>" + result);
            }
        } catch (Exception e) {
            logger.error(opt + ",异常：", e);
            e.printStackTrace();
            throw e;
        } finally {
            closeConn(response, httpClient);
        }
        return result;
    }

    private static HttpGet assembleHttpGet(String url, Map<String, String> headMap,
                                           Map<String, String> paramMap) throws Exception {
        String opt = "发送GET请求";
        URIBuilder uri_b = new URIBuilder(url);
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                uri_b.setParameter(entry.getKey(), entry.getValue());
                logger.debug(opt + ",入参==>" + entry.getKey() + ":" + entry.getValue());
            }
        }
        HttpGet httpGet = new HttpGet(uri_b.build());
        if (null != headMap) {
            for (String key : headMap.keySet()) {
                httpGet.setHeader(key, headMap.get(key));
            }
        }
        for (Header header : httpGet.getAllHeaders()) {
            logger.debug(opt + ",header==>" + header.getName() + ":" + header.getValue());
        }
        logger.debug(opt + ",URI==>" + httpGet.getURI());
        return httpGet;
    }

    /**
     * 发送GET请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return String
     */
    public static String get(String url, Map<String, String> headMap, Map<String, String> paramMap) throws Exception {
        String opt = "发送GET请求";
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = assembleHttpGet(url, headMap, paramMap);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                logger.debug(opt + ",响应==>" + result);
            }
        } catch (Exception e) {
            logger.error(opt + ",异常：", e);
            e.printStackTrace();
            throw e;
        } finally {
            closeConn(response, httpClient);
        }
        return result;
    }


    /**
     * 发送GET请求
     *
     * @param url      目标地址
     * @param headMap  追加的headers
     * @param paramMap 参数
     * @return byte[]
     */
    public static byte[] getByte(String url, Map<String, String> headMap, Map<String, String> paramMap) throws Exception {
        String opt = "发送GET请求";
        byte[] result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = assembleHttpGet(url, headMap, paramMap);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toByteArray(entity);
                logger.debug(opt + ",响应==>" + result);
            }
        } catch (Exception e) {
            logger.error(opt + ",异常：", e);
            e.printStackTrace();
            throw e;
        } finally {
            closeConn(response, httpClient);
        }
        return result;
    }

    /**
     * 关闭相关连接
     *
     * @param response
     * @param httpClient
     */
    private static void closeConn(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            if (null != response) {
                response.close();
            }
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("关闭连接异常：", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("abc", "123");
        get("http://members.3322.org/dyndns/getip", null, paramMap);
    }
}
