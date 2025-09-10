package Producer_consumer_problem.WithLock;

import java.util.List;

public class Producer implements Runnable{

    final List<Integer> simpleList;
    int maxSize;

    public Producer(List<Integer> simpleList, int maxSize) {
        this.simpleList = simpleList;
        this.maxSize = maxSize;
    }

    @Override
    public void run() {
        try{
            for(int i=1;i<=50;i++){
                Thread.sleep(100);
                synchronized (simpleList) {
                    while(simpleList.size() == maxSize){
                        System.out.println("List is full");
                        simpleList.wait();
                    }
                    simpleList.add(i);
                    System.out.println("Produced: "+i);
                    simpleList.notifyAll();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
