package com.yc.myTomcat;

import com.yc.thread.pool.Taskable;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetTask implements Taskable{
    private Socket s;
    private InputStream iis;
    private OutputStream oos;

    public NetTask(Socket s){
        this.s=s;
    }

    @Override
    public void doTask() {
        try {
            this.iis=this.s.getInputStream();
            this.oos=this.s.getOutputStream();
            YCHttpServletRequest request=new YCHttpServletRequest(this.iis,this.s);
            request.parse();
            Processor processor=null;
            //判断request中资源是静态还是动态
            if(request.getRequestURI().endsWith(".action")){
                //动态
                processor=new DynamicProcessor();
            }else{
                //静态
                processor=new StaticProcessor();
            }
            YCHttpServletResponse response=new YCHttpServletResponse(this.oos,request);
            processor.process(request,response);
            this.s.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
