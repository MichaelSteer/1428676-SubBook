/*
  NegativeCostException.java
  @Author Michael Steer
 */

package com.steers.Subscription;
import java.lang.Exception;

/**
 * NegativeCostException. Thrown by the Subscription class when a cost is negative
 */
public class NegativeCostException extends Exception {
    NegativeCostException(String message) {
        super(message);
    }
    NegativeCostException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
