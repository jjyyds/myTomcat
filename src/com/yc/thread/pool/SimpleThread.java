package com.yc.thread.pool;

public class SimpleThread extends Thread{
    private boolean runningFlag;//线程的运行状态
    private Taskable task;

    public SimpleThread(){
        this.runningFlag=false;
        //System.out.println("线程:"+this.getName()+"实例化完成，进入创建态");
    }

    public void setTask(Taskable task) {
        this.task = task;//绑定任务给当前线程
    }

    //获取线程的运行状态
    public boolean isRunning(){
        return runningFlag;
    }

    public synchronized void setRunningFlag(boolean runningFlag) {
        this.runningFlag = runningFlag;
        if(this.runningFlag){
            this.notify();
            //System.out.println(this.getName()+"进入 active");
        }
    }


    @Override
    public synchronized void run() {//jvm调度到run  运行态
        while (true){//死循环是为了让线程池中的线程不会结束销毁
            if(runningFlag==false){
                try {
                    //如果报出 java.lang.IllegalMonitorStateException异常 ，表明:
                    //wait要释放对象锁，所以wait()所在的方法上要加锁
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                if(this.task!=null){
                    this.task.doTask();//执行任务
                    //break return 不能写，因为会将当前线程结束
                    setRunningFlag(false);//任务执行完后，将运行态改为false
                    //System.out.println(Thread.currentThread().getName()+"进入wait");
                    //其实任务完成后，最终的目标都是将当前的线程设置wait
                }
            }
        }
    }
}
