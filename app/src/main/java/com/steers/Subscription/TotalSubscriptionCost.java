package com.steers.Subscription;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class TotalSubscriptionCost {
    private ArrayList<Subscription> subscriptions;
    private double totalCost;

    public TotalSubscriptionCost(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        for (Subscription subscription: subscriptions) {
            totalCost += subscription.getCost();
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getTotalCostString() {
        NumberFormat costFormatter = NumberFormat.getCurrencyInstance();
        String out = costFormatter.format(totalCost);
        return out;
    }
}
