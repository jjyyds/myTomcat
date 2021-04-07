package com.yc.javax.servlet.http;

import com.yc.javax.servlet.ServletRequest;

import java.io.InputStream;
import java.util.Map;

/**
 * 针对http协议的   请求接口
 */
public interface HttpServletRequest extends ServletRequest {

    public Map<String, String[]> getParameterMap();

    public String[] getParameterValues(String key);

    public String getParameter(String key);

    public String getHeader(String headerName);

    public String getProtocol();

    public InputStream getInputStream();

    public String getMethod();

    public Map<String, String> getHeaders();

    public String getUri();

    public String getRealPath();

}
