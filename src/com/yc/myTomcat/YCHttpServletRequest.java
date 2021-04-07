package com.yc.myTomcat;

import com.yc.javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class YCHttpServletRequest implements HttpServletRequest {
    private Socket socket;
    private InputStream iis;
    private String realPath;
    private String requestURI;
    private String requestURL;//
    private String queryString;// name=zy&age=20
    private String method;//方法
    private Map<String,String> headers=new ConcurrentHashMap<>();//头域
    private String uri;//请求的资源地址
    private String protocol;//协议版本
    private Map<String,String[]> parameterMap=new ConcurrentHashMap<>();

    public YCHttpServletRequest(InputStream iis, Socket socket){
        this.socket=socket;
        this.iis=iis;
    }

    public void parse(){
        String protocolContent=readProtocolFromInputStream();
        parseProtocol(protocolContent);
     }

    //解析：请求行，请求头域，实体，参数
    private void parseProtocol(String protocol) {
        if(protocol==null || "".equals(protocol)){
            return; //应通过response生成404
        }
        //字符串分割类
        StringTokenizer st=new StringTokenizer(protocol,"\r\n");//指定以\r\n分割
        int index=0;//标识第一行
        while(st.hasMoreElements()){//按行循环
            String line=st.nextToken();//取每一行
            if(index==0){//如果时第一行，则解析第一行
                String []first=line.split(" ");
                this.method=first[0];
                this.uri=first[1];
                this.protocol=first[2];
                //解析realPath
                this.realPath=System.getProperty("user.dir")+ File.separator+"webapps"+File.separator+""+this.uri.split("/")[0];
                this.requestURI=this.uri.split("\\?")[0];
                if("HTTP/1.1".equals(this.protocol) || "HTTP/1.0".equals(this.protocol)){
                    this.requestURL="http://"+this.socket.getRemoteSocketAddress()+this.requestURI;
                }
                if(this.uri.indexOf("?")>=0){
                    this.queryString=this.uri.split("\\?")[1];
                    String []params=this.queryString.split("&");
                    for(int i=0;i<params.length;i++){
                        String []pv=params[i].split("=");
                        if(pv[1].indexOf(",")>=0){
                            String []values=pv[1].split(",");
                            this.parameterMap.put(pv[0],values);
                        }else{
                            this.parameterMap.put(pv[0],new String[]{pv[1]});
                        }
                    }
                }
            }else if("".equals(line) || line==""){
                if("POST".equals(this.method)){
                    //以下的数据都是请求实体部分的数据了，比如post的参数
                    parseParams(st);
                }
                break;
            }else{
                String []heads=line.split(": ");
                headers.put(heads[0],heads[1]);
            }
            index++;
        }
    }

    //解析请求参数
    private void parseParams(StringTokenizer st) {
        while(st.hasMoreElements()){
            String line=st.nextToken();
            String []params=line.split("&");
            for(int i=0;i<params.length;i++){
                String []pv=params[i].split("=");
                if(pv[1].indexOf(",")>=0){
                    String[] vals = pv[1].split(",");
                    this.parameterMap.put(pv[0],vals);
                }else{
                    this.parameterMap.put(pv[0],new String[]{pv[1]});
                }
            }
        }
    }

    //1.从iis中取出协议
    private String readProtocolFromInputStream() {
        String protocolContent = null;
        StringBuffer sb=new StringBuffer(1024*30);
        int length=-1;
        byte []bs=new byte[1024*30];
        try{
            length=this.iis.read(bs);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i=0;i<length;i++){
            sb.append((char)bs[i]);
        }
        protocolContent=sb.toString();
        return protocolContent;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public String[] getParameterValues(String key){
        return this.parameterMap.get(key);
    }

    public String getParameter(String key){
        String []values=getParameterValues(key);
        if(values!=null && values.length>0){
            return values[0];
        }
        return null;
    }

    public String getHeader(String headerName) {
        if(null!=headers){
            return headers.get(headerName);
        }
        return null;
    }

    public String getProtocol() {
        return protocol;
    }

    public InputStream getInputStream(){
        return this.iis;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod(){
        return this.method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRealPath() {
        return realPath;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
