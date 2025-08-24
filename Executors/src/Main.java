import java.sql.Time;
import java.util.List;
import java.util.concurrent.*;

class ColorThreadFactory implements ThreadFactory{
    private String threadName;
    private int colorvalue = 1;

    public ColorThreadFactory(ThreadColor color) {
        this.threadName = color.name();
    }

    public ColorThreadFactory(){}

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        String name = threadName;
        if(name==null){
            name = ThreadColor.values()[colorvalue].name();
        }

        if(++colorvalue> (ThreadColor.values().length-1)){
            colorvalue = 1;
        }
        thread.setName(name);
        return thread;
    }
}

public class Main {
    public static void main(String[] args){
        var multiExecutor = Executors.newCachedThreadPool();
        List<Callable<Integer>> taskList = List.of(
                ()-> Main.sum2(1, 10, 1, "red"),
                ()-> Main.sum2(10, 100, 10, "blue"),
                ()-> Main.sum2(2, 20, 2, "green")
        );

        try{
            var results = multiExecutor.invokeAll(taskList);
            for(var result: results){
                System.out.println(result.get(500, TimeUnit.SECONDS));
            }
        }
        catch (InterruptedException | TimeoutException | ExecutionException e){
            throw new RuntimeException(e);
        }finally {
            multiExecutor.shutdown();
        }
    }
    public static void cachedmain(String[] args){
        var multiExecutor = Executors.newCachedThreadPool();

        try{
            multiExecutor.execute(()-> Main.sum(1, 10, 1, "red"));
            multiExecutor.execute(()-> Main.sum(10, 100, 10, "blue"));
            multiExecutor.execute(()-> Main.sum(2, 20, 2, "green"));
            var yellowValue = multiExecutor.submit(()-> Main.sum2(1, 10, 1, "yellow"));
            var cyanValue = multiExecutor.submit(()-> Main.sum2(10, 100, 10, "cyan"));
            var purpleValue = multiExecutor.submit(()-> Main.sum2(2, 20, 2, "purple"));


            try{
                System.out.println(yellowValue.get(500, TimeUnit.SECONDS));
                System.out.println(cyanValue.get(500, TimeUnit.SECONDS));
                System.out.println(purpleValue.get(500, TimeUnit.SECONDS));

                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException | ExecutionException | TimeoutException ex){
                throw new RuntimeException(ex);
            }

            System.out.println("Next Task will get executed");
            for(var color: new String[]{"red", "blue", "green", "yellow"}){
                multiExecutor.execute(()-> Main.sum(1, 10, 1, color));
            }
        }finally {
            multiExecutor.shutdown();
        }
    }

    public static void fixedmain(String[] args){
        int count = 6;
        var multiExecutor = Executors.newFixedThreadPool(count, new ColorThreadFactory());
        for(int i=0;i<count;i++){
            multiExecutor.execute(Main::countDown);
        }
        multiExecutor.shutdown();
    }

    public static void singlemain(String[] args){
        var blueExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_BLUE));
        blueExecutor.execute(Main::countDown);
        blueExecutor.shutdown();

        boolean isDone = false;

        try {
            isDone = blueExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        if(isDone){
            System.out.println("Blue finished, starting yellow");
            var yellowExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_YELLOW));
            yellowExecutor.execute(Main::countDown);
            yellowExecutor.shutdown();

            isDone = false;

            try {
                isDone = yellowExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(isDone){
                System.out.println("Yellow finished, starting red");
                var redExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_RED));
                redExecutor.execute(Main::countDown);
                redExecutor.shutdown();
                try {
                    isDone = redExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(isDone){
                    System.out.println("All process completed");
                }
            }

        }

    }

    public static void notmain(String[] args) {
        Thread blue = new Thread(Main::countDown, ThreadColor.ANSI_BLUE.name());
        Thread yellow = new Thread(Main::countDown, ThreadColor.ANSI_YELLOW.name());
        Thread red = new Thread(Main::countDown, ThreadColor.ANSI_RED.name());

        blue.start();
        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        yellow.start();
        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        red.start();
        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void countDown(){
        String threadName = Thread.currentThread().getName();
        var threadColor = ThreadColor.ANSI_RESET;
        try{
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch (IllegalArgumentException e) {

        }

        String color = threadColor.color();

        for(int i=20; i>=0; i--){
            System.out.println(color+ "  "+threadName.replace("ANSI_", "")+" "+ i);
        }
    }

    private static void sum(int start, int end, int delta, String colorString){
        var threadColor = ThreadColor.ANSI_RESET;

        try{
            threadColor = ThreadColor.valueOf("ANSI_"+ colorString.toUpperCase());
        } catch (IllegalArgumentException e) {

        }

        String color = threadColor.color();
        int sum = 0;
        for(int i=start; i<=end; i+=delta){
            sum+=i;
        }

        System.out.println(color+Thread.currentThread().getName()+ ", "+ colorString + " "+ sum);
    }

    private static int sum2(int start, int end, int delta, String colorString){
        var threadColor = ThreadColor.ANSI_RESET;

        try{
            threadColor = ThreadColor.valueOf("ANSI_"+ colorString.toUpperCase());
        } catch (IllegalArgumentException e) {

        }

        String color = threadColor.color();
        int sum = 0;
        for(int i=start; i<=end; i+=delta){
            sum+=i;
        }

        System.out.println(color+Thread.currentThread().getName()+ ", "+ colorString + " "+ sum);
        return sum;
    }
}