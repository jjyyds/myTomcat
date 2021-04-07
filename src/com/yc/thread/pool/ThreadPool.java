package com.yc.thread.pool;

import java.util.Vector;

/**
 * 线程池管理器
 *   1.初始化线程池容器
 *   2.执行任务
 */
public class ThreadPool {
    //线程池容器
    private Vector<SimpleThread> vector;
    //最大线程数
    private int maxThread;
    private static int coreCounts;

    static{
        //1.获取系统核数
        coreCounts=Runtime.getRuntime().availableProcessors();
        System.out.println("系统核数:"+coreCounts);
        System.out.println("线程池开始初始化");
    }

    public ThreadPool(){
        this(coreCounts);//调用当前类的构造方法

    }

    public ThreadPool(int threadCount){
        vector=new Vector<SimpleThread>();
        //2.根据核数创建simplethread对象存到vector中
        for (int i = 0; i < threadCount; i++) {
            SimpleThread st=new SimpleThread();
            st.setName("线程"+(i+1));
            st.setDaemon(true);
            vector.add(st);
            st.start();
        }
        //3.启动simplethread对象，只有start -》进入到就绪状态-》 jvm来调用-》run
        //注意点：新线程要进入到wait
    }

    public void process(Taskable task){
        //1.task不能为空
        if(task==null){
            return;
        }
        //2.从vector取出一个线程 (循环/随机/hash/带权)
        SimpleThread st=getFreeSimpleThread();
        //3.将task绑定到 simplethread
        st.setTask(task);
        //4.设置这个线程的状态为运行态
        st.setRunningFlag(true);
    }

    //获取一个空闲的线程
    private synchronized SimpleThread getFreeSimpleThread(){
        int j=0;
        for (int i = 0; i < vector.size(); i++) {
            j++;
            SimpleThread stt=vector.get(i);
            if(stt.isRunning()==false){
                return stt;
            }
        }
        //线程数不够，则产生新的线程存到vector中
        System.out.println("线程池没有空线程，扩容"+coreCounts+"个新线程");
        for (int i = 0; i < coreCounts; i++) {
            SimpleThread st=new SimpleThread();
            st.setName("线程"+(i+1));
            st.setDaemon(true);
            vector.add(st);
            st.start();
        }
        return vector.get(j);
    }
}
