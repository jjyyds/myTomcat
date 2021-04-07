package com.yc.myTomcat;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicProcessor implements Processor{
    //存放servlet实例
    static Map<String,Servlet> servletMap=new ConcurrentHashMap<>();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        //1.取出uri中servlet的名字 -> HelloServlet的字节码
        String uri=request.getUri();
        int slash=uri.lastIndexOf("/")+1;
        int dot=uri.lastIndexOf(".");
        String basePath=request.getRealPath();//D:\ideaworkspace\myTomcat\webapps\
        String servletName=uri.substring(slash,dot);
        try {
            Servlet servlet=null;
            HttpServlet ser=null;
            if(servletMap.containsKey(servletName+".action")){
                servlet=servletMap.get(servletName+".action");
            }else{
                //2.加载  URLClassLoader
                URL url=new URL("file",null,basePath);
                URL[] urls=new URL[]{url};
                URLClassLoader ucl=new URLClassLoader(urls);
                Class cls=ucl.loadClass(servletName);
                //3.反射  -> Class.newInstance
                servlet=(Servlet)cls.newInstance();
                ser=(HttpServlet)servlet;
                ser.init();
                servletMap.put(servletName+".action",servlet);
            }
            //4.按Servlet的生命周期调用
            if(servlet!=null && servlet instanceof HttpServlet){
                ser=(HttpServlet)servlet;
                ser.service(request,response);
            }
        }catch (Exception e){
            e.printStackTrace();
            String content=e.getMessage();
            String protocal=gen500(content.getBytes());
            try(OutputStream oos=response.getOutputStream();){
                oos.write(protocal.getBytes());
                oos.flush();
                oos.write(content.getBytes());
                oos.flush();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public String gen500(byte[] fileContent){
        String result=null;
        result="HTTP/1.1 500 Internal Server Error\r\nAccept-Ranges: bytes\r\nContent-Type: text/html;charset=UTF-8\r\nContent-Length: 0\r\n\r\n";
        return result;
    }
}
