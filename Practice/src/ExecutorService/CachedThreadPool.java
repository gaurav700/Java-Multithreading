package ExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPool {

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void execute(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }

    public void shutdown() {
        cachedThreadPool.shutdown();
    }

    public void callingRunnable(){
        for(int i = 0; i < 10; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is running for task: "+ finalI);
                }
            };
            execute(runnable);
        }
    }

    public static void main(String[] args) {
        CachedThreadPool obj = new CachedThreadPool();
        obj.callingRunnable();
        obj.shutdown();
    }
}
