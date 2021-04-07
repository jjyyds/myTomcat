package com.yc.myTomcat;

import com.yc.thread.pool.ThreadPool;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Logger log = Logger.getLogger(Main.class);
    private static String serverConf;
    private static Map<String,String> xmlMap;

    static {
        String userDir=System.getProperty("user.dir");
        //设置log4j.properties文件路径和日志文件输出位置
        System.setProperty("WORKDIR",userDir);
        serverConf=userDir+File.separator+"conf"+File.separator+"server.xml";
        PropertyConfigurator.configure(userDir+File.separator+"conf"+File.separator+"log4j.properties");
    }

    public static void main(String[] args) {
        ThreadPool pool=new ThreadPool();
        parseXml(serverConf);
        try (ServerSocket ss=new ServerSocket(Integer.parseInt(xmlMap.get("port")));){
            //项目webapps路径
            //String realPath=System.getProperty("user.dir")+File.separator+"webapps"+ File.separator;
            log.info(ss.getInetAddress()+"正常启动,监听"+ss.getLocalPort()+"端口");
            while (true){
                Socket s=ss.accept();
                log.info(s.getRemoteSocketAddress()+"连接到服务器");
                pool.process(new NetTask(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    //解析配置文件获取端口
    public static void parseXml(String path){
        File file=new File(path);
        try {
            SAXBuilder builder=new SAXBuilder();
            Document doc=builder.build(file);
            Element root = doc.getRootElement();//获取根元素
            String shutdownport = root.getAttributeValue("port");
            Element connector = root.getChild("Service").getChild("Connector");
            String port=connector.getAttributeValue("port");
            String threadpool = connector.getAttributeValue("threadpool");
            xmlMap=new HashMap<>();
            xmlMap.put("port",port);
            xmlMap.put("shutdownport",shutdownport);
            xmlMap.put("threadpool",threadpool);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
