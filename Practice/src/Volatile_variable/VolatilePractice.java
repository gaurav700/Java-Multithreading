package Volatile_variable;

public class VolatilePractice {

    // First try without volatile - may cause issues
    private static int counter = 0;
    
    // Then try with volatile - fixes the visibility issue
    // private static volatile int counter = 0;

    public static void main(String[] args) {
        
        // Thread-1: Increments counter in a loop
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread-1: Starting counter increment");
            while (counter < 10) {
                counter++;
                System.out.println("Thread-1: Counter = " + counter);
                try {
                    Thread.sleep(500); // Small delay to observe behavior
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Thread-1: Finished incrementing. Final counter = " + counter);
        }, "Incrementer");

        // Thread-2: Monitors counter and stops when it reaches 10
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread-2: Starting to monitor counter");
            while (counter < 10) {
                // Continuously monitor the counter
                // Without volatile, this thread might not see updates from Thread-1
                System.out.println("Thread-2: Monitoring... counter = " + counter);
                try {
                    Thread.sleep(100); // Check frequently
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Thread-2: Counter reached 10! Stopping monitor.");
        }, "Monitor");

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Main: Both threads completed. Final counter = " + counter);
    }
}
