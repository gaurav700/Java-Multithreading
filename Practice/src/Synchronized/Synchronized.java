package Synchronized;

public class Synchronized {

    // race condition
//    private int counter = 0;
//    public void increment() {
//        this.counter += 1;
//    }

    // resolve this condition
    private int counter = 0;
    public synchronized void increment() {
        this.counter += 1;
    }

    public int getCounter() {
        return this.counter;
    }

    Thread t1 = new Thread(new Runnable() {
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

    Thread t4 = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        }
    });


    public static void main(String[] args) {
        Synchronized obj = new Synchronized();
        obj.t1.start();
        obj.t2.start();
        obj.t3.start();
        obj.t4.start();
        try {
            obj.t1.join();
            obj.t2.join();
            obj.t3.join();
            obj.t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter = " + obj.getCounter());
    }
}