package com.example.demo;

public class Retrier {
    public static void main(String[] args) {
        int maxAttempts = 3;
        FlakyService service = new FlakyService(maxAttempts);
        String answer = callWithRetry(service, maxAttempts);
        System.out.println("Answer: " + answer);

    }
    public static String callWithRetry(FlakyService service, int maxAttempts) {

        long delayMs = 100;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                return service.call();
            } catch (IllegalStateException e) {
                System.out.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
                if (i < maxAttempts - 1) {
                    try {
                        Thread.sleep(delayMs);
                        delayMs *= 2;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Interrupted while waiting to retry");
                    }
                }
            }
        }
        throw new IllegalStateException("Gave up after " + maxAttempts + " attempts");
    }
}