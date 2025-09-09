package Atomic_vs_Synchronized;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVsSyn {

    private AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
        this.counter.incrementAndGet();
    }

    public int getCounter() {
        return this.counter.get();
    }



    private int counter2 = 0;

    public synchronized void increment2() {
        this.counter2 += 1;
    }

    public int getCounter2() {
        return this.counter2;
    }



    Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        }
    });


    Thread t3 = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        }
    });

    Thread t2 = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment2();
            }
        }
    });

    Thread t4 = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment2();
            }
        }
    });

    AtomicVsSyn(){
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }


    public static void main(String[] args) {
        AtomicVsSyn obj = new AtomicVsSyn();
        try {
            obj.t1.join();
            obj.t2.join();
            obj.t3.join();
            obj.t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter = " + obj.getCounter());
        System.out.println("Counter2 = " + obj.getCounter2());
    }
}
