package com.dap.paas.console.seq.util.http;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className HttpClientUtil
 * @description 请求工具类
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 通过post方式调用http接口
     *
     * @param url     url路径
     * @param jsonParam    json格式的参数
     * @param reSend     重发次数
     * @return String
     */
    public static String sendPostByJson(String url, String jsonParam, int reSend) {

        // 声明返回结果
        String result = "";

        // 开始请求API接口时间
        long startTime = System.currentTimeMillis();

        // 请求API接口的响应时间
        long endTime = 0L;
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        try {

            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();

            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            Header header = new BasicHeader("Accept-Encoding", null);
            httpPost.setHeader(header);

            // 设置报文和通讯格式
            StringEntity stringEntity = new StringEntity(jsonParam, HttpConstant.UTF8_ENCODE);
            stringEntity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            stringEntity.setContentType(HttpConstant.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            LOGGER.info("请求{}接口的参数为{}", url, jsonParam);

            // 执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            LOGGER.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                LOGGER.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByJson(url, jsonParam, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        } finally {
            httpClose(httpEntity);
        }

        // 请求接口的响应时间
        endTime = System.currentTimeMillis();
        LOGGER.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;
    }

    /**
     * http关闭
     *
     * @param httpEntity httpEntity
     */
    public static void httpClose(HttpEntity httpEntity) {
        try {
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            LOGGER.error("http请求释放资源异常", e);
        }
    }

    /**
     * 通过post方式调用http接口
     *
     * @param url     url路径
     * @param map    json格式的参数
     * @param reSend     重发次数
     * @return String
     */
    public static String sendPostByForm(String url, Map<String, String> map, int reSend) {

        // 声明返回结果
        String result = "";

        // 开始请求API接口时间
        long startTime = System.currentTimeMillis();

        // 请求API接口的响应时间
        long endTime = 0L;
        HttpEntity httpEntity = null;
        UrlEncodedFormEntity entity = null;
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        try {

            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();

            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);

            // 设置参数
            List<NameValuePair> list = new ArrayList<>();
            map.forEach((k, v) -> list.add(new BasicNameValuePair(k, v)));
            entity = new UrlEncodedFormEntity(list, HttpConstant.UTF8_ENCODE);
            httpPost.setEntity(entity);
            LOGGER.info("请求{}接口的参数为{}", url, map);

            // 执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            LOGGER.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                LOGGER.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByForm(url, map, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        } finally {
            httpClose(httpEntity);
        }
        //请求接口的响应时间
        endTime = System.currentTimeMillis();
        LOGGER.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;

    }

    /**
     * 通过post方式调用http接口
     *
     * @param url     url路径
     * @param xmlParam    json格式的参数
     * @param reSend     重发次数
     * @return String
     */
    public static String sendPostByXml(String url, String xmlParam, int reSend) {

        // 声明返回结果
        String result = "";

        // 开始请求API接口时间
        long startTime = System.currentTimeMillis();

        // 请求API接口的响应时间
        long endTime = 0L;
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        try {

            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();

            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            StringEntity stringEntity = new StringEntity(xmlParam, HttpConstant.UTF8_ENCODE);
            stringEntity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            stringEntity.setContentType(HttpConstant.TEXT_XML);
            httpPost.setEntity(stringEntity);
            LOGGER.info("请求{}接口的参数为{}", url, xmlParam);

            // 执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, HttpConstant.UTF8_ENCODE);
        } catch (Exception e) {
            LOGGER.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                LOGGER.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByJson(url, xmlParam, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        } finally {
            httpClose(httpEntity);
        }
        // 请求接口的响应时间
        endTime = System.currentTimeMillis();
        LOGGER.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;
    }
}
