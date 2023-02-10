package co.yixiang.modules.device.watchuricdatarecords.util;


import java.util.concurrent.ThreadFactory;

/**
 * @author 38294
 * @Date 2019/4/9
 * @license 版权所有，非经许可请勿使用本代码
 */
public class TaskThreadFactory implements ThreadFactory {

    private String ThreadName="Thread";

    public TaskThreadFactory(String tname) {
        ThreadName = tname;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName(ThreadName);
        return thread;
    }

}
