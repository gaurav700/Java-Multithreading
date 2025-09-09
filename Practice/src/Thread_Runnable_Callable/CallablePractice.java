package Thread_Runnable_Callable;

import java.util.concurrent.*;

public class CallablePractice implements Runnable{
    private volatile boolean cancelled = false;
    private volatile boolean done = false;
    private volatile Integer i = 0;
    
    Future<Integer> future = new Future<Integer>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (done) {
                return false; // Can't cancel if already done
            }
            cancelled = true;
            done = true;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public Integer get() throws InterruptedException, ExecutionException {
            if (cancelled) {
                throw new CancellationException("Task was cancelled");
            }
            if (!done) {
                // In a real implementation, this would block until completion
                // For this example, we'll just return the current value
                return i;
            }
            return i != null ? i : 0;
        }

        @Override
        public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return get(); // Simplified implementation
        }
    };

    @Override
    public void run() {
        while(i<1000 && !cancelled){
            i++;

            if(i == 999){
                try {
                    Thread.sleep(1000);
                    cancelled = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (!cancelled) {
            done = true;
        }
        
        System.out.println("Callable Practice");
        future.cancel(true);
        System.out.println(future.isCancelled());
        System.out.println(future.isDone());
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException e) {
            System.out.println("Task was cancelled: " + e.getMessage());
        }

    }
}
