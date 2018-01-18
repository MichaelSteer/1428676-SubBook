package com.steers.Subscription;
import java.lang.Exception;
/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class NegativeCostException extends Exception {
    NegativeCostException(String message) {
        super(message);
    }
    NegativeCostException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
