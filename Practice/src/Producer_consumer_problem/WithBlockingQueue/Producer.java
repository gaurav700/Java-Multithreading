package Producer_consumer_problem.WithBlockingQueue;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{

    public BlockingQueue<Integer> blockingQueue;

    public Producer(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try{
            for(int i=1;i<=50;i++){
                Thread.sleep(100);
                blockingQueue.put(i);
                System.out.println("Produced: "+i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
