package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCounter {

    // the list of events is gotten, and the count of events for each user is passed to a hashmap as an integer with the username.
    // getOrDefault, attempts to get the count of events for a user, and defaults to a value if the count isn't known.
    // here the default is 0 events counted for the user so far
    public Map<String, Integer> countByUser(List<Event> events) {
        Map<String, Integer> counts = new HashMap<>();
        for (Event event : events) {
            int current = counts.getOrDefault(event.user(), 0);
            counts.put(event.user(), current + 1);
        }
        return counts;
    }

    // the list of events is gotten, and the count of events for each type is passed to a hashmap as an integer with the username.
    // getOrDefault, attempts to get the count of events for a type, and defaults to a value if the count isn't known.
    // here the default is 0 events counted for the type so far
    public Map<String, Integer> countByType(List<Event> events) {
        Map<String, Integer> counts = new HashMap<>();
        for (Event event : events) {
            int current = counts.getOrDefault(event.type(), 0);
            counts.put(event.type(), current + 1);
        }
        return counts;
    }

}