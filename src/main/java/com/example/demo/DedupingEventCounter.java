package com.example.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DedupingEventCounter {

    private final Set<String> seenIds = new HashSet<>();
    private final Map<String, Integer> countsByUser = new HashMap<>();

    // Returns true if the event was counted, false if it was a duplicate.
    public boolean process(TrackedEvent event) {

        if(seenIds.contains(event.id())) {

            return false;

        }

        int current = countsByUser.getOrDefault(event.user(), 0);
        countsByUser.put(event.user(), current + 1);

        seenIds.add(event.id());
        return true;

    }

    public Map<String, Integer> getCountsByUser() {
        return countsByUser;
    }
}