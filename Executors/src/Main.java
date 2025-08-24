import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

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
}