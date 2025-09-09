package ThreadPoolExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Thread_pool_executor {
    private ThreadPoolExecutor threadPoolExecutor;

    public Thread_pool_executor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                1,                  // core pool size
                5,                  // maximum pool size
                0L,               // keep alive time
                TimeUnit.MILLISECONDS,   // time unit
                new LinkedBlockingQueue<>(5)  // work queue
        );
    }

    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public void callingRunnable() {
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            Runnable runnable = () -> {
                try {
                    System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());
                    Thread.sleep(100); // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            execute(runnable);
        }
    }

    public static void main(String[] args) {
        Thread_pool_executor executor = new Thread_pool_executor();
        executor.callingRunnable();
        executor.shutdown();
    }
}