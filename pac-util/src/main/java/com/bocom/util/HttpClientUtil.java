package com.bocom.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static String charset = "UTF-8";

    private static final String CHAR_SET = ",charset:";

    private HttpClientUtil() {
    }

    private static String decodeBase64(String str) {
        try {
            return new String(Base64.decodeBase64(str), charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("HttpClientUtil.decodeBase64 error", e);
        }
        return null;
    }

    public static String getBase64(String url) {
        return decodeBase64(get(url));
    }

    public static String postBase64(String url, Map<String, Object> map) {
        return decodeBase64(post(url, map));
    }

    public static String postBase64Charset(String url, Map<String, Object> map, String charset) {
        return decodeBase64(postCharset(url, map, charset));
    }

    public static String get(String url) {
        return get(url, null, charset);
    }

    private static String postCharset(String url, Map<String, Object> map, String charset) {
        return post(url, map, charset);
    }

    public static String post(String url, Map<String, Object> map) {
        return post(url, map, charset);
    }

    public static String postBase64(String url, String json) {
        return decodeBase64(post(url, json));
    }

    public static String postBase64(String url, String json, String charset) {
        return decodeBase64(postCharset(url, json, charset));
    }

    private static String postCharset(String url, String json, String charset) {
        return post(url, json, charset);
    }

    public static String post(String url, String json) {
        return post(url, json, charset);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, params, charset);
    }

    public static String getBase64Data(String url, Map<String, String> params) {
        return decodeBase64(get(url, params, charset));
    }

    public static String get(String url, Map<String, String> params,
                             String charset) {
        if (logger.isInfoEnabled()) {
            logger.info("HttpClientUtil.get url:" + url + CHAR_SET + charset + " params is " + params);
        }
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            // 设置参数
            if (null != params && params.size() > 0) {
                List<NameValuePair> list = new ArrayList<>();
                Iterator iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> elem = (Entry<String, String>) iterator
                            .next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem
                            .getValue()));
                }
                URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
                if (!list.isEmpty()) {
                    uriBuilder.setParameters(list);
                }
                httpGet.setURI(uriBuilder.build());
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            logger.info("HttpClientUtil.get error", e);
        }
        return result;
    }

    public static String post(String url, Map<String, Object> map, String charset) {
        if (logger.isInfoEnabled()) {
            logger.info("HttpClientUtil.post url" + url + ",map:" + map
                    + CHAR_SET + charset);
        }
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            // 设置参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000).setConnectTimeout(20000).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            if (null != map) {
                List<NameValuePair> list = new ArrayList<>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> elem = (Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (!list.isEmpty()) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                    httpPost.setEntity(entity);
                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            logger.info("HttpClientUtil.post error", e);
        }
        return result;
    }

    public static String post(String url, String json, String charset) {
        if (logger.isInfoEnabled()) {
            logger.info("HttpClientUtil.post url:" + url + "  param : " + json + CHAR_SET + charset);
        }
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            // 设置参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000).setConnectTimeout(20000).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(json, Charset.forName("UTF-8"));
            entity.setContentEncoding(charset);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            logger.info("HttpClientUtil.post error", e);
        }
        return result;
    }
}
