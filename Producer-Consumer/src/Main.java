import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class MessageRepository{
    private String message;
    private boolean hasMessage = false;

    public synchronized String read() throws InterruptedException {
        while(!hasMessage){
            wait();
        }
        hasMessage = false;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) throws InterruptedException {
        while(hasMessage){
            wait();
        }
        hasMessage = true;
        notifyAll();
        this.message = message;
    }
}


class MessageWriter implements Runnable{
    private MessageRepository outgoingMessage;

    private final String text = """
                                Twinkle Twinkle little stars,
                                How i wonder what you are,
                                Up above the worlds so high,
                                Like a diamond in the sky.
                                """;

    public MessageWriter(MessageRepository outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }

    @Override
    public void run() {

        Random random = new Random();
        String[] lines = text.split("\n");

        for(int i=0;i<lines.length;i++){
            try {
                outgoingMessage.write(lines[i]);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try{
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            outgoingMessage.write("Finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class MessageReader implements Runnable{
    private MessageRepository incomingMessage;

    public MessageReader(MessageRepository incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    @Override
    public void run() {
            Random random = new Random();
            String latestMessage = "";

            do{
                try{
                    Thread.sleep(random.nextInt(500, 2000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    latestMessage = incomingMessage.read();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(latestMessage);
            }while (!latestMessage.equals("Finished"));
    }
}

public class Main {
    public static void main(String[] args) {
        MessageRepository messageRepository = new MessageRepository();

        Thread reader = new Thread(new MessageReader(messageRepository));
        Thread writer = new Thread(new MessageWriter(messageRepository));

        reader.start();
        writer.start();
    }
}