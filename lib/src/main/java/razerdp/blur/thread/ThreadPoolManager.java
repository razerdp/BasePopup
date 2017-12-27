package razerdp.blur.thread;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 线程池封装
 */
public class ThreadPoolManager {

    private static ExecutorService threadPool = null;

    static {
        /**
         corePoolSize  线程池中核心线程的数量
         maximumPoolSize  线程池中最大线程数量
         keepAliveTime 非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收。如果ThreadPoolExecutor的allowCoreThreadTimeOut属性设置为true，则该参数也表示核心线程的超时时长
         unit 第三个参数的单位，有纳秒、微秒、毫秒、秒、分、时、天等
         workQueue 线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务。存储在这里的任务是由ThreadPoolExecutor的execute方法提交来的。
         threadFactory  为线程池提供创建新线程的功能，这个我们一般使用默认即可
         handler 拒绝策略，当线程无法执行新任务时（一般是由于线程池中的线程数量已经达到最大数或者线程池关闭导致的），默认情况下，当线程池无法处理新线程时，会抛出一个RejectedExecutionException。
         */

        int processorNum = Runtime.getRuntime().availableProcessors(); //CPU数量
        threadPool = new ThreadPoolExecutor(processorNum, processorNum * 2 + 1, 20, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public static void execute(Runnable runnable) {
        try {
            threadPool.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对不同api的 asynctask处理
     * 3.0以后的asynctask被改为默认串行，使用自己的线程池实现并行
     */
    public static <Params, Progress, Result> void executeOnExecutor(AsyncTask<Params, Progress, Result> task, Params...
            params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(threadPool, params);
        } else {
            task.execute(params);
        }
    }
}
