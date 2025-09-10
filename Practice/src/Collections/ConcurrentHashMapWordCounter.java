package Collections;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapWordCounter {

    private final ConcurrentHashMap<String, AtomicInteger> wordCountMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    // Increment word count atomically
    public void increment(String word) {
        if (word.isEmpty()) return; // Skip empty words
        wordCountMap.computeIfAbsent(word, k -> new AtomicInteger(0)).incrementAndGet();
    }

    // Submit task to executor
    public void submitTask(Runnable task) {
        executorService.submit(task);
    }

    // Shutdown executor gracefully
    public void shutdownAndAwaitTermination() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Process each file content in a separate thread
    public void processFiles(String[] files) {
        for (int i = 0; i < files.length; i++) {
            String fileContent = files[i];
            submitTask(() -> {
                // Clean text and split into words
                String[] words = fileContent.replaceAll("[:#\\-]", "")
                        .split("\\s+");
                Arrays.stream(words).forEach(this::increment);
            });
        }
    }

    public void printTopNWords(int n) {
        System.out.println("Total unique words: " + wordCountMap.size());
        wordCountMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().get() - e1.getValue().get())
                .limit(n)
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue().get()));
    }

    public static void main(String[] args) {
        String[] files = {
                "2025-09-10 10:01:12 INFO  Server started successfully on port 8080 2025-09-10 10:02:15 WARN  High memory usage detected: 85% 2025-09-10 10:03:20 INFO  User login: user123",
                "2025-09-10 11:10:05 ERROR Database connection failed at db-server-1 2025-09-10 11:12:30 INFO  Retrying connection in 5 seconds 2025-09-10 11:13:40 INFO  Connection established successfully",
                "2025-09-10 12:20:40 DEBUG Cache miss for key: session_456 2025-09-10 12:21:10 INFO  Fetched data from database for key: session_456 2025-09-10 12:22:00 DEBUG Cache updated with key: session_456",
                "2025-09-10 13:30:05 INFO  Payment processed for order #98765 2025-09-10 13:31:10 ERROR Payment gateway timeout for order #98766 2025-09-10 13:32:20 INFO  Retrying payment for order #98766",
                "2025-09-10 14:40:00 INFO  Service shutdown initiated by admin 2025-09-10 14:41:10 INFO  Saving all in-memory sessions to disk 2025-09-10 14:42:00 INFO  Server stopped gracefully"
        };

        ConcurrentHashMapWordCounter counter = new ConcurrentHashMapWordCounter();
        counter.processFiles(files);
        counter.shutdownAndAwaitTermination();
        counter.printTopNWords(10);
    }
}
