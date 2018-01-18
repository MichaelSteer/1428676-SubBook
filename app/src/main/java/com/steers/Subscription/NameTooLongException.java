package com.steers.Subscription;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class NameTooLongException extends Exception {
    NameTooLongException(String message) {
        super(message);
    }
    NameTooLongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
