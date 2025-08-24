import java.util.ArrayList;
import java.util.List;
import java.util.Random;

record Order(long orderId, String item, int qty){};

class ShoeWarehouse{
    private List<Order> shippingItems;
    final static String[] PRODUCT_LIST = {"Running shoes", "Sandles", "Boots", "Slippers", "High Tops"};

    public ShoeWarehouse() {
        this.shippingItems = new ArrayList<>();
    }

    public synchronized void receiveOrder(Order item){
        while(shippingItems.size()>20) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

            shippingItems.add(item);
            System.out.println("Incoming: "+item);
            notifyAll();
    }

    public synchronized Order fulfillOrder(){
        while(shippingItems.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

            Order item = shippingItems.remove(0);
            System.out.println(Thread.currentThread().getName() + " Fulfilled: "+item);
            notifyAll();
            return item;
    }
}


public class Main {
    private static final Random random = new Random();
    public static void main(String[] args) {
        ShoeWarehouse warehouse = new ShoeWarehouse();
        Thread producerThread = new Thread(()->{
            for(int j=0;j<10;j++){
                warehouse.receiveOrder(new Order(
                        random.nextLong(1000000, 9000000),
                        ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                        random.nextInt(1, 4)));
            }
        });

        producerThread.start();


        for(int i=0;i<2;i++){
            Thread consumerThread = new Thread(()->{
                for(int j=0;j<5;j++){
                    Order item = warehouse.fulfillOrder();
                }
            });
            consumerThread.start();
        }


    }
}