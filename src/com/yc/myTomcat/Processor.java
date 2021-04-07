package com.yc.myTomcat;

import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

/**
 * 资源处理接口
 */
public interface Processor {
    //处理方法
    public void process(HttpServletRequest request, HttpServletResponse response);
}
