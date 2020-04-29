package com.lqc.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import play.libs.Json;
import play.libs.ws.*;

import javax.inject.Inject;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * ClassName: WSUtils
 * CreateTime 2017年12月11日 下午5:35:25
 * author : liqinchao
 * Description: Play HTTP请求工具类
 */
@Component
public class WSUtils implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;

    @Inject
    public WSUtils(WSClient ws) {
        this.ws = ws;
    }
    /**
     * get请求
     * @param url
     * @param headMap
     * @param paramMap
     * @return
     */
    public CompletionStage<WSResponse> get(String url, Map<String, String> headMap
            , Map<String, String> paramMap) {
        WSRequest request = ws.url(url);
        // 追加headers
        if (null != headMap) {
            for (String key : headMap.keySet()) {
                request.addHeader(key, headMap.get(key));
            }
        }
        // 参数
        if (null != paramMap) {
            for (String key : paramMap.keySet()) {
                request.addQueryParameter(key, paramMap.get(key));
            }
        }
        return request
                .setRequestTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .get();
    }

    /**
     * post请求
     * @param url
     * @param headMap
     * @param paramMap
     * @return
     */
    public CompletionStage<WSResponse> post(String url, Map<String, String> headMap
            , Map<String, String> paramMap) {
        WSRequest request = ws.url(url);
        // 追加headers
        if (null != headMap) {
            for (String key : headMap.keySet()) {
                request.addHeader(key, headMap.get(key));
            }
        }
        JsonNode para=(null==paramMap)?Json.newObject():Json.toJson(paramMap);
        return request
                .setRequestTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .post(para);
    }

}
