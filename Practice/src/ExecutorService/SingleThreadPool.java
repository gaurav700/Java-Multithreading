package ExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPool {

    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    public void execute(Runnable runnable) {
        singleThreadPool.execute(runnable);
    }

    public void shutdown() {
        singleThreadPool.shutdown();
    }


    public void callingRunnable(){
        for(int i = 0; i < 10; i++) {
            int taskID =i;
            execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is running for task: "+ taskID);
                }
            });
        }
    }



    public static void main(String[] args) {
        SingleThreadPool obj = new SingleThreadPool();
        obj.callingRunnable();
        obj.shutdown();
    }
}
