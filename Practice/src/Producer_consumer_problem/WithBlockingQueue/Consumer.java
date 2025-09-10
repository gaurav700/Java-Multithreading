package Producer_consumer_problem.WithBlockingQueue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    BlockingQueue<Integer> blockingQueue;

    public Consumer(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try{
            for(int i=1;i<=50;i++){
                Thread.sleep(100);
                System.out.println("Consumed: "+blockingQueue.take());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
