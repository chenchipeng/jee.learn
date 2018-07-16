package com.jee.learn.common.http;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * HttpClient简化请求方法HttpClientSurport
 * 
 * @author yjf
 * @version 1.0
 * 
 *          修改记录： 1.2016年12月19日 上午10:10:55 yjf new
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpClientSupport {

    private static Logger logger = LoggerFactory.getLogger(HttpClientSupport.class);

    /**
     * get请求返回响应的html内容
     * 
     * @param url
     * @param params
     * @return
     */
    public String executeHttpGet(String url, Map<String, String> params) {
        return this.executeHttpGet(HttpClients.custom().build(), url, params);
    }

    /**
     * get请求返回响应的html内容
     * 
     * @param httpclient
     * @param url
     * @param params
     * @return
     */
    public String executeHttpGet(HttpClient httpclient, String url, Map<String, String> params) {
        try {
            RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> e : params.entrySet()) {
                    requestBuilder.addParameter(e.getKey(), e.getValue());
                }
            }
            requestBuilder.setEntity(new StringEntity(""));

            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpclient.execute(request);
            logger.info("httpGet请求url={},响应状态={}", url, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity, "UTF-8");
                logger.debug(html);
                EntityUtils.consume(entity);

                return html;
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * post请求返回响应的html内容
     * 
     * @param url
     * @param params
     * @return
     */
    public String executeHttpPost(String url, Map<String, String> params) {
        return this.executeHttpPost(HttpClients.custom().build(), url, params);
    }

    /**
     * post请求返回响应的html内容
     * 
     * @param httpclient
     * @param url
     * @param params
     * @return
     */
    public String executeHttpPost(HttpClient httpclient, String url, Map<String, String> params) {
        try {
            RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> e : params.entrySet()) {
                    requestBuilder.addParameter(e.getKey(), e.getValue());
                }
            }

            HttpUriRequest request = requestBuilder.build();
            logger.debug("request url : {}", request.getURI());
            HttpResponse response = httpclient.execute(request);
            // logger.info("httpPost请求url={},响应状态={}", url,
            // response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity, "UTF-8");
                logger.debug(html);
                EntityUtils.consume(entity);

                return html;
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * post请求返回响应的html内容
     * 
     * @param httpclient
     * @param url
     * @param params
     * @return
     */
    public String executeHttpPost(String url, Map<String, String> headers, Map<String, String> params) {
        HttpClient httpclient = HttpClients.custom().build();
        try {
            RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> e : headers.entrySet()) {
                    requestBuilder.addHeader(e.getKey(), e.getValue());
                }
            }
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> e : params.entrySet()) {
                    requestBuilder.addParameter(e.getKey(), e.getValue());
                }
            }

            HttpUriRequest request = requestBuilder.build();
            logger.debug("request url : {}", request.getURI());
            HttpResponse response = httpclient.execute(request);
            // logger.info("httpPost请求url={},响应状态={}", url,
            // response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity, "UTF-8");
                logger.debug(html);
                EntityUtils.consume(entity);

                return html;
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * Post请求返回响应的html内容
     * 
     * @param url
     * @param params
     * @return
     */
    public String executeHttpPost(String url, Map<String, String> headers, String msgbody) {
        HttpClient httpclient = HttpClients.custom().build();
        try {
            RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> e : headers.entrySet()) {
                    requestBuilder.addHeader(e.getKey(), e.getValue());
                }
            }
            requestBuilder.setEntity(new StringEntity(msgbody, "UTF-8"));

            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpclient.execute(request);
            logger.info("httpGet请求url={},响应状态={}", url, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity, "UTF-8");
                logger.debug(html);
                EntityUtils.consume(entity);

                return html;
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * post请求返回响应的重定向的url
     * 
     * @param httpclient
     * @param url
     * @param params
     * @return
     */
    public String executeHttpPostRedirect(HttpClient httpclient, String url, Map<String, String> params) {
        try {
            RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> e : params.entrySet()) {
                    requestBuilder.addParameter(e.getKey(), e.getValue());
                }
            }

            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpclient.execute(request);
            // logger.info("httpPost请求url={},响应状态={}", url,
            // response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String redirectUrl = response.getLastHeader("Location").getValue();
            logger.info("redirectUrl={}", redirectUrl);
            EntityUtils.consume(entity);

            return redirectUrl;

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }
    
}
