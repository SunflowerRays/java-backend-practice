package com.example.demo;

public class InvalidLlmReplyException extends RuntimeException {

    public InvalidLlmReplyException(String message, Throwable cause) {
        super(message, cause);
    }
}
