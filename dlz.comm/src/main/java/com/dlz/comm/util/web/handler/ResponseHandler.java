package com.dlz.comm.util.web.handler;

import com.dlz.comm.exception.BussinessException;
import com.dlz.comm.exception.HttpException;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.web.HttpRequestParam;
import com.dlz.comm.util.web.reader.IResponseReader;
import com.dlz.comm.util.web.reader.ResponseStringReader;
import com.dlz.comm.util.web.reader.ResponseXmlReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.dom4j.Document;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求结果处理
 *
 * @author dk
 */
@Slf4j
public class ResponseHandler<T> {
    protected static final Map<Class, IResponseReader> CLASS_READERS = new HashMap<>();
    protected static final IResponseReader<String> DEFAULT_READER=ResponseStringReader.getInstance();
    static {
        CLASS_READERS.put(String.class, DEFAULT_READER);
        CLASS_READERS.put(Document.class,ResponseXmlReader.getInstance());
    }

    protected T getOkResult(InputStream content,HttpRequestParam<T> param){
        Class<T> tClass = param.getTClass();
        IResponseReader iResponseReader = CLASS_READERS.get(tClass);
        if(iResponseReader==null){
            iResponseReader = DEFAULT_READER;
        }
        Object result = iResponseReader.read(content, param.getCharsetNameResponse());
        return ValUtil.toObj(result,tClass);
    }

    protected String getNgResult(InputStream content,HttpRequestParam param){
        return DEFAULT_READER.read(content, param.getCharsetNameResponse());
    }
    public T handle(HttpRequestParam<T> param,
                        int statusCode,
                        HttpResponse response) {
        Object result = null;
        try {
            switch (statusCode) {
                case HttpStatus.SC_OK:
                case HttpStatus.SC_CREATED:
                case HttpStatus.SC_ACCEPTED:
                    result = getOkResult(response.getEntity().getContent(),param);
                    return (T)result;
                case HttpStatus.SC_NOT_FOUND:
                    throw new HttpException("地址无效:" + param.getUrl(), statusCode);
                case HttpStatus.SC_UNAUTHORIZED:
                case HttpStatus.SC_FORBIDDEN:
                    throw new HttpException("无访问权限:" + param.getUrl(), statusCode);
                case HttpStatus.SC_MOVED_TEMPORARILY:
                    String msg = getNgResult(response.getEntity().getContent(),param);
                    throw new BussinessException("访问错误:" + msg);
                default:
                    msg = getNgResult(response.getEntity().getContent(),param);
                    if (statusCode > 3000 && statusCode < 3100) {
                        throw new BussinessException(statusCode, msg, null);
                    } else {
                        log.error("访问异常:" + param.getUrl() + " 返回码:" + statusCode + " msg:" + msg);
                        throw new HttpException(msg, statusCode);
                    }
            }
        } catch (BussinessException | HttpException e) {
            throw e;
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            String info = "handle response Exception:" + e.getMessage() + " re="+result;
            throw new SystemException(info, e);
        }
    }

}
