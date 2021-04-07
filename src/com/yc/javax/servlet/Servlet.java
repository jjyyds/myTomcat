package com.yc.javax.servlet;

public interface Servlet {
    public void destroy();
    public void service(ServletRequest req,
                        ServletResponse res);

}
