package Producer_consumer_problem.WithLock;

import java.util.List;

public class Consumer implements Runnable{

    final List<Integer> simpleList;

    public Consumer(List<Integer> simpleList) {
        this.simpleList = simpleList;
    }

    @Override
    public void run() {
        try{
            for(int i=1;i<=50;i++){
                Thread.sleep(100);
                synchronized (simpleList) {
                    while(simpleList.isEmpty()){
                        System.out.println("List is empty");
                        simpleList.wait();
                    }
                    System.out.println("Consumed: "+simpleList.removeFirst());
                    simpleList.notifyAll();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
