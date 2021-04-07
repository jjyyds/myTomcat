package com.yc.myTomcat;

import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

//静态处理
public class StaticProcessor implements Processor{
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        //静态资源直接调用重定向
        response.sendRedirect();
    }
}
