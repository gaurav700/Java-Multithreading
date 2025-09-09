package Synchronized;

public class OddEven {

    private int counter = 0;
    private volatile boolean isOdd = true;

    public synchronized void increment() {
        this.counter++;
    }

    public int getCounter() {
        return this.counter;
    }


    Runnable runnable1 = () -> {
        while (counter < 100) {
            synchronized (this) {
                if (isOdd && counter % 2 == 0) {
                    try {
                        Thread.sleep(100);
                        increment();
                        System.out.println("Odd = " + getCounter());
                        isOdd = false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    };

    Runnable runnable2 = () -> {
        while (counter < 100) {
            synchronized (this) {
                if (!isOdd && counter % 2 != 0) {
                    try {
                        Thread.sleep(100);
                        increment();
                        System.out.println("Even = " + getCounter());
                        isOdd = true;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    };


    public OddEven() {
        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);
        t1.start();
        t2.start();
    }


    public static void main(String[] args) {
        new OddEven();
    }

}