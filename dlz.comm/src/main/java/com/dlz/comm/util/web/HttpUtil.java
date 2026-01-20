package com.dlz.comm.util.web;

import com.dlz.comm.exception.BussinessException;
import com.dlz.comm.exception.HttpException;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.web.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP工具类
 * 
 * 提供HTTP请求执行和结果处理的功能
 *
 * @author dk
 * @since 2023
 */
@Slf4j
public class HttpUtil {
    /**
     * 执行HTTP请求
     *
     * @param request HTTP请求对象
     * @param param HTTP请求参数
     * @return HTTP响应对象
     */
    private static HttpResponse executeHttp(HttpRequestBase request, HttpRequestParam param) {
//        HttpClient httpClient = HttpConnUtil.wrapClient(param.getUrl(),param.getRequestConfig());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> headers = param.getHeaders();
        if(param.getRequestConfig() != null) {
            request.setConfig(param.getRequestConfig());
        }
        headers.forEach(request::addHeader);
        try {
            if (request instanceof HttpEntityEnclosingRequestBase) {
                String payLoad = param.getPayload();
                if (payLoad == null && !param.getPara().isEmpty()) {
                    if (param.getMimeType().equals(HttpConstans.MIMETYPE_FORM)) {
                        payLoad = buildUrl(param.getPara(), param.getCharsetNameRequest());
                    } else {
                        payLoad = JacksonUtil.getJson(param.getPara());
                    }
                }
                if (payLoad == null) {
                    payLoad = "";
                }
                ContentType contentType = ContentType.create(param.getMimeType(), param.getCharsetNameRequest());
                StringEntity entity = new StringEntity(payLoad, contentType);
                ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
            } else if (!param.getPara().isEmpty()) {
                request.setURI(new URI(buildUrl(param.getUrl(), null, param.getPara(), param.getCharsetNameRequest())));
//                request.setURI(buildUrI(param.getUrl(), null, param.getPara()));
            }
            return httpClient.execute(request, param.getLocalContext());
        } catch (Exception e) {
            throw new SystemException(mkError(e, request.getURI().toString(), request.getMethod()));
        }
    }


    /**
     * 执行HTTP请求并解析结果
     *
     * @param request HTTP请求对象
     * @param param HTTP请求参数
     * @return 解析后的结果对象
     */
    public static Object doHttp(HttpRequestBase request, HttpRequestParam param) {
        Object result = null;
        try {
            HttpResponse execute = executeHttp(request, param);
            int statusCode = execute.getStatusLine().getStatusCode();
            ResponseHandler responseHandler = param.getResponseHandler();
            result = responseHandler.handle(param, statusCode, execute);
        } catch (BussinessException | HttpException | SystemException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException(mkError(e, request.getURI().toString(), request.getMethod()), e);
        } finally {
            request.releaseConnection();
            if (param.isShowLog() && log.isDebugEnabled()) {
                if (result instanceof Document) {
                    log.debug("doHttp " + request.getMethod() + " url:" + request.getURI() + " para:" + param.getPara() + " re:" + ((Document) result).asXML());
                } else {
                    log.debug("doHttp " + request.getMethod() + " url:" + request.getURI() + " para:" + param.getPara() + " re:" + result);
                }
            }
        }
        return result;
    }

    /**
     * 构建错误信息
     *
     * @param e 异常对象
     * @param url 请求URL
     * @param method 请求方法
     * @return 错误信息字符串
     */
    private static String mkError(Exception e, String url, String method) {
        String message = e.getMessage();
        String info = e.getClass().getName();
        if (StringUtils.isEmpty(message) && e.getCause() != null) {
            message = e.getCause().getMessage();
        }
        if (!StringUtils.isEmpty(message)) {
            info += ":" + message;
        }
        info += " -> url=" + url + ",method=" + method;
        return info;
    }


    /**
     * 构建URI对象
     *
     * @param host 主机地址
     * @param path 路径
     * @param querys 查询参数
     * @return URI对象
     * @throws URISyntaxException URI语法异常
     */
    public static URI buildUrI(String host, String path, Map<String, Object> querys) throws URISyntaxException {
        // 创建uri
        URIBuilder builder = new URIBuilder(StringUtils.isEmpty(path) ? host : (host + path));
        if (querys != null) {
            for (String key : querys.keySet()) {
                builder.addParameter(key, ValUtil.toStr(querys.get(key)));
            }
        }
        return builder.build();
    }

    /**
     * 构建URL字符串
     *
     * @param host 主机地址
     * @param path 路径
     * @param querys 查询参数
     * @param enc 编码格式
     * @return URL字符串
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static String buildUrl(String host, String path, Map<String, Object> querys, String enc) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isEmpty(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            String s = buildUrl(querys, enc);
            if (0 < s.length()) {
                sbUrl.append(sbUrl.indexOf("?") > -1 ? "&" : "?").append(s);
            }
        }
        return sbUrl.toString();
    }

    /**
     * 构建查询字符串
     *
     * @param querys 查询参数
     * @param enc 编码格式
     * @return 查询字符串
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static String buildUrl(Map<String, Object> querys, String enc) throws UnsupportedEncodingException {
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, Object> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isEmpty(query.getKey()) && !StringUtils.isEmpty(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isEmpty(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isEmpty(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(ValUtil.toStr(query.getValue()), enc));
                    }
                }
            }
            return sbQuery.toString();
        }
        return "";
    }
}