package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RetrierTest {

    @Test
    public void throwsException() {
        //Arrange:
        int maxAttempts = 3;
        FlakyService service = new FlakyService(maxAttempts);
        //Act & Assert:
        assertThrows(IllegalStateException.class, () -> Retrier.callWithRetry(service, 3));
    }

    @Test
    public void succeedsAfterTwoRetries() {
        //Arrange:
        int maxAttempts = 2;
        FlakyService service = new FlakyService(maxAttempts);
        //Act & Assert:
        assertEquals("OK on call 3", Retrier.callWithRetry(service, 3));
    }
}
