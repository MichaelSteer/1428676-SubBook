/*
 NameTooLongException.java
 @Author Michael Steer
 */
package com.steers.Subscription;

/**
 * NameTooLongException. Thrown by the Subscription class when the name is too long
 */
public class NameTooLongException extends Exception {
    NameTooLongException(String message) {
        super(message);
    }
    NameTooLongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
