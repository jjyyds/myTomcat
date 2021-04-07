package com.yc.myTomcat;

import com.yc.IOUtil;
import com.yc.javax.servlet.http.HttpServletResponse;

import java.io.*;

public class YCHttpServletResponse implements HttpServletResponse {
    private OutputStream oos;
    private YCHttpServletRequest request;

    public YCHttpServletResponse(OutputStream oos, YCHttpServletRequest request){
        this.oos=oos;
        this.request=request;
    }

    public void sendRedirect() {
        String responseprotocol=null;//响应协议
        byte []fileContent=null;//响应的资源的内容
        String uri=request.getRequestURI();//请求的资源路径
        File f=new File(request.getRealPath(),uri);
        if(!f.exists()){
            //文件不存在，404
            File file404=new File(request.getRealPath(),"404.html");
            fileContent=readFile(file404);
            responseprotocol=gen404(file404,fileContent);
        }else{
            //存在文件，则读取文件
            fileContent=readFile(f);
            responseprotocol=gen200(f,fileContent);
        }
        try{
            //以输出流输出到客户端
            this.oos.write(responseprotocol.getBytes());//先输出响应协议的头部
            this.oos.flush();
            this.oos.write(fileContent);
            this.oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(this.oos!=null){
                try {
                    this.oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] readFile(File f) {
        byte []bs=null;
        try (InputStream is=new FileInputStream(f);){
            bs=IOUtil.readFromInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }

    private String gen200(File f, byte[] fileContent) {
        String result=null;
        String uri=this.request.getRequestURI();// /kaw/index.html
        int index=uri.lastIndexOf(".");
        if(index>=0){
            index+=1;
        }
        String fileExtension=uri.substring(index);
        if("JPG".equalsIgnoreCase(fileExtension) || "JPEG".equalsIgnoreCase(fileExtension)){
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: image/jpeg\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }else if("PNG".equalsIgnoreCase(fileExtension)){
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: image/png\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }else if("json".equalsIgnoreCase(fileExtension)){
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: application/json\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }else if("css".equalsIgnoreCase(fileExtension)){
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: text/css\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }else if("js".equalsIgnoreCase(fileExtension)){
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: application/javascript\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }else{
            result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: text/html;charset=UTF-8\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        }
        return result;
    }

    private String gen404(File file404, byte[] fileContent) {
        String result=null;
        result="HTTP/1.1 404\r\nAccept-Ranges: bytes\r\nContent-Type: text/html;charset=UTF-8\r\nContent-Length: "+fileContent.length+"\r\n\r\n";
        return result;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.oos;
    }
}
