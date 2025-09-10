package Fork_and_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class SumOfArray extends RecursiveTask<Long> {

    private final Long[] numbers;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10;

    public SumOfArray(Long[] numbers, int i, int length) {
        this.numbers = numbers;
        this.start = i;
        this.end = length;
    }

    @Override
    protected Long compute() {
        int lenght = end - start;
        if(lenght <= THRESHOLD){
            long sum = 0;
            for(int i=start;i<end;i++){
                sum += numbers[i];
            }
            return sum;
        }else{
            int middle = (start + end)/2;
            SumOfArray left = new SumOfArray(numbers, start, middle);
            SumOfArray right = new SumOfArray(numbers, middle, end);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }

    public static void main(String[] args) {

        Long[] numbers = new Long[10000000];
        for(int i=0;i<numbers.length;i++){
            numbers[i] = (long)i+1;
        }
        long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime));
        System.out.println("Sum of array: "+pool.invoke(new SumOfArray(numbers, 0, numbers.length)));


        long startTime2 = System.currentTimeMillis();
        Long sum = 0L;
        for(Long i = 0L; i<numbers.length; i++){
            sum += numbers[Math.toIntExact(i)];
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime2));
        System.out.println("Sum of array: "+sum);

    }
}
