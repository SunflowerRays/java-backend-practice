package com.example.demo;

import java.time.Instant;
import java.util.List;

public class DedupDemo {

    public static void main(String[] args) {
        List<TrackedEvent> events = List.of(
                new TrackedEvent("e1", "anna", "login", Instant.now()),
                new TrackedEvent("e2", "anna", "click", Instant.now()),
                new TrackedEvent("e1", "anna", "login", Instant.now()),
                new TrackedEvent("e3", "ben", "login", Instant.now()),
                new TrackedEvent("e2", "anna", "click", Instant.now())
        );

        DedupingEventCounter counter = new DedupingEventCounter();
        for (TrackedEvent event : events) {
            System.out.println(event.id() + " counted: " + counter.process(event));
        }
        System.out.println("Counts: " + counter.getCountsByUser());
    }
}