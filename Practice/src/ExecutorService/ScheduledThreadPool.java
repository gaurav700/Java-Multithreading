package ExecutorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledThreadPool {

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

    public void execute(Runnable runnable) {
        scheduledThreadPool.execute(runnable);
    }

    public void shutdown() {
        scheduledThreadPool.shutdown();
    }


    public void callingRunnable(){
        for(int i = 0; i < 10; i++) {
            int finalI = i;
            scheduledThreadPool.schedule(() -> {
                System.out.println("I am running " + finalI+ " Thread id is : "+ Thread.currentThread().getName());
            }, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
    }


    public static void main(String[] args) {
        ScheduledThreadPool obj = new ScheduledThreadPool();
        obj.callingRunnable();
        obj.shutdown();
    }
}
