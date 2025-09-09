package ThreadLocal_vs_InheritedThreadLocal;

public class ThreadLocal_vs_Inherited {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    public String getThreadLocal() {
        return threadLocal.get();
    }

    public void setThreadLocal(String threadLocal) {
        this.threadLocal.set(threadLocal);
    }

    public String getInheritableThreadLocal() {
        return inheritableThreadLocal.get();
    }

    public void setInheritableThreadLocal(String inheritableThreadLocal) {
        this.inheritableThreadLocal.set(inheritableThreadLocal);
    }


    Runnable runnable1 = () -> {
        System.out.println("Entered into runnable1");
        threadLocal.set("Runnable1");
        System.out.println("ThreadLocal value in runnable1 = " + getThreadLocal());
    };


    Runnable runnable2 = () -> {
        System.out.println("Entered into runnable2");
        threadLocal.set("Runnable2");
        System.out.println("ThreadLocal value in runnable2 = " + getThreadLocal());
    };

    Runnable runnable3 = () -> {
        System.out.println("Entered into runnable3 for InheritableThreadLocal");

        inheritableThreadLocal.set("Runnable3");
        System.out.println("InheritableThreadLocal value in runnable3 = " + getInheritableThreadLocal());

        Runnable runnable4 = () -> {
            System.out.println("Entered into runnable4 for InheritableThreadLocal");
            System.out.println("InheritableThreadLocal value in runnable4 = " + getInheritableThreadLocal());
        };

        Thread thread = new Thread(runnable4);
        thread.start();
    };


    Thread t1 = new Thread(runnable1);
    Thread t2 = new Thread(runnable2);
    Thread t3 = new Thread(runnable3);

    ThreadLocal_vs_Inherited(){
        t1.start();
        t2.start();
        t3.start();
    }

    public static void main(String[] args) {
        ThreadLocal_vs_Inherited obj = new ThreadLocal_vs_Inherited();
    }

}
