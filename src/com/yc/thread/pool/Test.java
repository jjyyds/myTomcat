package com.yc.thread.pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws IOException {
        ThreadPool pool=new ThreadPool();
        InputStream is=System.in;//键盘输入流
        //将这个字节流转为字符
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String str=null;
        while ((str= br.readLine())!=null){
            pool.process(new Task(str));
        }
        for (int i = 0; i < 16; i++) {
            pool.process(new Task(str));
        }
        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Task implements Taskable{
    private String content;
    public Task(String content){
        this.content=content;
    }

    @Override
    public void doTask() {
        System.out.println(Thread.currentThread().getName()+"执行了任务:"+content);
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

