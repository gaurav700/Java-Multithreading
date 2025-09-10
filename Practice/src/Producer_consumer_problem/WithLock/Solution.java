package Producer_consumer_problem.WithLock;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {

        List<Integer> simpleList = new ArrayList<>();
        int maxSize = 5;

        Producer producer = new Producer(simpleList, maxSize);
        Consumer consumer = new Consumer(simpleList);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);

        t1.start();
        t2.start();
    }
}
