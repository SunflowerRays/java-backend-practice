package com.example.demo;

import java.time.Instant;
import java.util.List;

public class EventCounterDemo {

    public static void main(String[] args) {
        List<Event> events = List.of(
                new Event("anna", "login", Instant.now()),
                new Event("anna", "click", Instant.now()),
                new Event("ben", "login", Instant.now()),
                new Event("anna", "click", Instant.now()),
                new Event("ben", "logout", Instant.now())
        );

        EventCounter counter = new EventCounter();
        System.out.println("By user: " + counter.countByUser(events));
        System.out.println("By type: " + counter.countByType(events));
    }
}