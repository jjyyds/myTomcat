package com.yc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class IOUtil{

    /**
     * 读取方法
     * @param iis
     * @return
     */
    public static byte[] readFromInputStream(InputStream iis){
        try (ByteArrayOutputStream baos=new ByteArrayOutputStream();
            InputStream iis0=iis){
            int length=-1;
            byte []bs=new byte[1024*10];
            while((length=iis0.read(bs,0,bs.length))!=-1){
                baos.write(bs,0,length);
            }
            return baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
