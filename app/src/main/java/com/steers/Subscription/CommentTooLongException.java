/*
  CommentTooLongException.java
  @Author Michael Steer
 */
package com.steers.Subscription;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

/**
 * CommentTooLongException. Thrown when a comment for a subscription is too long
 */
public class CommentTooLongException extends Exception {
    CommentTooLongException(String message) {
        super(message);
    }
    CommentTooLongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
