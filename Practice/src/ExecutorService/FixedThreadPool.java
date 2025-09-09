package ExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);


    public void execute(Runnable runnable) {
        fixedThreadPool.execute(runnable);
    }

    public void shutdown() {
        fixedThreadPool.shutdown();
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
        FixedThreadPool obj = new FixedThreadPool();
        obj.callingRunnable();
        obj.shutdown();

    }
}
