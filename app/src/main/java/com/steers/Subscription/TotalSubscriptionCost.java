/*
 TotalSubscriptionCost.java
 @author Michael Steer
 */

package com.steers.Subscription;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * TotalSubscriptionCost class
 * Calculates the sum of the amount values inside the subscriptions
 */
public class TotalSubscriptionCost {
    private ArrayList<Subscription> subscriptions;
    private double totalCost;

    /**
     * Constructor
     * @param subscriptions {@code ArrayList<Subscription>} the list of subscriptions to sum
     */
    public TotalSubscriptionCost(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        for (Subscription subscription: subscriptions) {
            totalCost += subscription.getCost();
        }
    }

    /**
     * Returns the total cost of the arraylist passed into the constructor
     * @return {@code double} the total cost
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Returns the total cost as a currency formatted string
     * @return {@code String} the total cost as a string
     */
    public String getTotalCostString() {
        NumberFormat costFormatter = NumberFormat.getCurrencyInstance();
        String out = costFormatter.format(totalCost);
        return out;
    }
}
