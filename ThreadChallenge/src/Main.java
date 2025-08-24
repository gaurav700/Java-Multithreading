class OddThread extends Thread{
    @Override
    public void run() {
        for (int i = 1; i <= 10; i += 2) {
            System.out.println("Odd Thread: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Odd Thread Interrupted!");
                break;
            }
        }
    }
}

class EvenRunnable implements Runnable{
    @Override
    public void run(){
        for (int i = 2; i <= 10; i += 2) {
            System.out.println("Even Thread: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Even Thread Interrupted!");
                break;
            }
        }
    }

}

public class Main {
    public static void main(String[] args) {
        OddThread oddThread = new OddThread();

        Runnable runnable = () ->{
            for (int i = 2; i <= 10; i += 2) {
                System.out.println("Even Thread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Even Thread Interrupted!");
                    break;
                }
            }
        };
        Thread evenThread = new Thread(runnable);
//        Thread evenThread = new Thread(EvenRunnable())

        oddThread.start();
        evenThread.start();

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        oddThread.interrupt();
    }
}