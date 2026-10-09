package com.example.demo;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueDemo {

    // A special "stop" message that tells the consumer to finish.
    private static final TrackedEvent STOP =
            new TrackedEvent("STOP", "", "", Instant.EPOCH);

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<TrackedEvent> queue = new LinkedBlockingQueue<>();
        DedupingEventCounter counter = new DedupingEventCounter();

        // Consumer: runs in its own thread, takes events off the queue.
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    TrackedEvent event = queue.take(); // waits if the queue is empty
                    if (event == STOP) {
                        break;
                    }
                    counter.process(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();

        // Producer: the main thread puts events on the queue.
        queue.put(new TrackedEvent("e1", "anna", "login", Instant.now()));
        queue.put(new TrackedEvent("e2", "anna", "click", Instant.now()));
        queue.put(new TrackedEvent("e1", "anna", "login", Instant.now())); // duplicate
        queue.put(new TrackedEvent("e3", "ben", "login", Instant.now()));
        queue.put(STOP);

        consumer.join(); // wait for the consumer to finish
        System.out.println("Counts: " + counter.getCountsByUser());
    }
}