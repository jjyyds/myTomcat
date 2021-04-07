package com.yc.javax.servlet.http;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

public class HttpServlet implements Servlet {

    @Override
    public void destroy() {
    }

    public void init(){}

    @Override
    public void service(ServletRequest req, ServletResponse res) {
        service(  (HttpServletRequest)req,   (HttpServletResponse)res);
    }
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        //从request中取出method,再调用  对应  doXXx()   -> 分发请求
        if(   "GET".equalsIgnoreCase( req.getMethod() )   ){
            doGet(  req, resp );
        }else if(  "POST".equalsIgnoreCase(  req.getMethod())){
            doPost(  req,resp);
        }else if(  "DELETE".equalsIgnoreCase(  req.getMethod())){
            doDelete(  req,resp);
        }else if(  "OPTION".equalsIgnoreCase(  req.getMethod())){
            doOption(  req,resp);
        }else if(  "TRACE".equalsIgnoreCase(  req.getMethod())){
            doTrace(  req,resp);
        }else if(  "PUT".equalsIgnoreCase(  req.getMethod())){
            doPut(  req,resp);
        }else if(  "HEAD".equalsIgnoreCase(  req.getMethod())){
            doHead(  req,resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doPut(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doHead(HttpServletRequest req, HttpServletResponse resp){
    }
    protected void doOption(HttpServletRequest req, HttpServletResponse resp){
    }

}
