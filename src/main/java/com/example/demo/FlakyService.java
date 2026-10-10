package com.example.demo;

public class FlakyService {

    private final int failuresBeforeSuccess;
    private int calls = 0;

    public FlakyService(int failuresBeforeSuccess) {
        this.failuresBeforeSuccess = failuresBeforeSuccess;
    }

    public String call() {
        calls++;
        if (calls <= failuresBeforeSuccess) {
            throw new IllegalStateException("Service unavailable (call " + calls + ")");
        }
        return "OK on call " + calls;
    }
}