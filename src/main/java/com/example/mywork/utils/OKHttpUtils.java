package com.example.mywork.utils;


import com.example.mywork.entity.enums.ResponseCodeEnum;
import com.example.mywork.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OKHttpUtils {
    /**
     * 请求超时时间5秒
     */
    private static final int TIME_OUT_SECONDS = 5000 ;

    private static final int SOCKET_TIMEOUT = 35000 ;

    private static final int TIME_LIVE_MAX = 350000 ;

    private static final Integer MAX_TOTAL=400;

    private static Logger logger = LoggerFactory.getLogger(OKHttpUtils.class);
    /**
     * 创建 HttpClient 客户端（配置超时和重定向）
     */
    private static CloseableHttpClient getHttpClient() {

        ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIME_OUT_SECONDS).setConnectTimeout(TIME_OUT_SECONDS).setSocketTimeout(TIME_OUT_SECONDS).setSocketTimeout(SOCKET_TIMEOUT).build();

        // 构建客户端（禁止连接失败重试）
        return HttpClients.custom().setDefaultConnectionConfig(connectionConfig).setDefaultRequestConfig(requestConfig).build();

    }
    /**
     * 构建请求头
     */
    private static void addHeaders(HttpGet httpGet, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() == null ? "" : entry.getValue();
                httpGet.addHeader(key, value);
            }
        }
    }

    private static void addHeaders(HttpPost httpPost, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() == null ? "" : entry.getValue();
                httpPost.addHeader(key, value);
            }
        }
    }

    /**
     * 构建表单参数
     */
    private static UrlEncodedFormEntity getFormEntity(Map<String, String> params) {
        List<NameValuePair> formParams = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() == null ? "" : entry.getValue();
                formParams.add(new BasicNameValuePair(key, value));
            }
        }
        // 使用 UTF-8 编码（OkHttp 默认也是 UTF-8）
        return new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
    }



    /**
     * GET 请求
     */
    public static String getRequest(String url) throws BusinessException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            addHeaders(httpGet, null); // 无自定义头

            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            logger.info("getRequest请求地址:{},返回信息:{}", url, responseStr);
            return responseStr;

        } catch (IOException e) {
            // 处理超时和连接异常（HttpClient 中超时会抛出 IOException 子类）
            if (e.getMessage().contains("timeout") || e instanceof java.net.ConnectException) {
                logger.error("HttpClient GET 请求超时,url:{}", url, e);
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            logger.error("HttpClient GET 请求异常", e);
            return null;
        } finally {
            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭响应流异常", e);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("关闭 HttpClient 异常", e);
                }
            }
        }
    }

    /**
     * POST 表单请求
     */
    public static String postRequest(String url, Map<String, String> params) throws BusinessException, JsonProcessingException {
        if (params == null) {
            params = new HashMap<>();
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            addHeaders(httpPost, null); // 无自定义头

            // 设置表单参数
            UrlEncodedFormEntity formEntity = getFormEntity(params);
            httpPost.setEntity(formEntity);

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            logger.info("postRequest请求地址:{},参数:{},返回信息:{}",
                    url, JsonUtils.convertObj2Json(params), responseStr);
            return responseStr;

        } catch (IOException e) {
            // 处理超时和连接异常
            if (e.getMessage().contains("timeout") || e instanceof java.net.ConnectException) {
                logger.error("HttpClient POST 请求超时,url:{},请求参数：{}",
                        url, JsonUtils.convertObj2Json(params), e);
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            logger.error("HttpClient POST 请求异常,url:{},请求参数：{}",
                    url, JsonUtils.convertObj2Json(params), e);
            return null;
        } finally {
            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭响应流异常", e);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("关闭 HttpClient 异常", e);
                }
            }
        }
    }

}
