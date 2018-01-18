package com.steers.Subscription;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class CommentTooLongException extends Exception {
    CommentTooLongException(String message) {
        super(message);
    }
    CommentTooLongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
