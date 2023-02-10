package co.yixiang.modules.device.watchuricdatarecords.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author 38294
 * @Date 2019/4/9
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
public class Constans {

    //核心线程10，最大100个，存活时间5分钟
    public static ExecutorService watchUricDataRecordHandlePool = new ThreadPoolExecutor(10,100,5, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>(),new TaskThreadFactory("WatchUricDataRecordHandleThread"));


    public static ExecutorService tumbleDataRecordHandlePool = new ThreadPoolExecutor(10,100,5, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>(),new TaskThreadFactory("TumbleDataRecordHandleThread"));


    //创建线程（并发）池，自动伸缩(自动条件线程池大小)
    public static ExecutorService threadPool =  Executors.newCachedThreadPool(new TaskThreadFactory("WatchUricDataRecordHandleThread"));


}
