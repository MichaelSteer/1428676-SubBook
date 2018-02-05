/*
 Subscription.java
 @author Michael Steer
 */
package com.steers.Subscription;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Subscription class. This class contained a name, date, comment, and dollar amount
 * for monthly subscriptions
 */
public class Subscription {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int NAME_LENGTH = 20;
    public static final int COMMENT_LENGTH = 30;

    @Expose
    private String name, comment;
    @Expose
    private Date date;
    @Expose
    private double cost;

    /**
     * Builds the class from a specified bundle.
     * Returns null if the bundle is invalid
     *
     * @param bundle The bundle to build the Subscription
     *               from
     *
     * @return Returns a valid subscription from the bundle,
     *         otherwise returns null
     */
    public static Subscription fromBundle(Bundle bundle) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        String name = bundle.getString("Name");
        String comment = bundle.getString("Comment");
        Date date;
        try {
            date = formatter.parse(bundle.getString("Date"));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        double cost = bundle.getDouble("Amount");

        try {
            return new Subscription(name, cost, date, comment);
        } catch (NameTooLongException | NegativeCostException | CommentTooLongException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Builds the subscription
     *
     * @param name The name of the Subscription
     * @param cost The monthly cost of the Subscription
     * @param date The date the subscription was added
     *
     * @throws NameTooLongException If the Name is too long
     * @throws NegativeCostException If the cost is Negative
     */
    public Subscription(String name, double cost, Date date) throws NameTooLongException, NegativeCostException {
        if(name.length() > NAME_LENGTH)
            throw new NameTooLongException("Name length is: " + String.valueOf(name.length()));
        if (cost < 0)
            throw new NegativeCostException("Cost is negative.");

        this.date = date;
        this.name = name;
        this.cost = cost;
        this.comment = "";
    }

    /**
     * Builds the subscription
     *
     * @param name The name of the Subscription
     * @param cost The monthly cost of the Subscription
     * @param date The date the subscription was added
     *
     * @throws NameTooLongException If the Name is too long
     * @throws NegativeCostException If the cost is Negative
     * @throws CommentTooLongException If the Comment is too long
     */
    public Subscription(String name, double cost, Date date, String comment) throws NameTooLongException, NegativeCostException, CommentTooLongException {
        if(name.length() > NAME_LENGTH)
            throw new NameTooLongException("Name length is: " + String.valueOf(name.length()));
        if (cost < 0)
            throw new NegativeCostException("Cost is negative.");
        if(comment.length() > COMMENT_LENGTH)
            throw new CommentTooLongException("Comment length is: " + String.valueOf(comment.length()));

        this.date = date;
        this.name = name;
        this.cost = cost;
        this.comment = comment;
    }

    /**
     * Get the name of the Subscription
     * @return The name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) throws NameTooLongException {
        if(name.length() > NAME_LENGTH) {
            throw new NameTooLongException("Name length is: " + String.valueOf(name.length()));
        }
        else this.name = name;
    }

    /**
     * Get the comment for the Subscription
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if(comment.length() > NAME_LENGTH) {
            throw new CommentTooLongException("Comment length is: " + String.valueOf(name.length()));
        }
        else this.comment = comment;
    }

    /**
     * Get the date the Subscription was generated
     * @return The date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get  the date the Subscription was generated in String form
     * @return The date
     */
    public String getDateString() {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        return sf.format(getDate());
    }

    /**
     * Set the date the Subscription was generated
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the monthly cost of the Subscription
     * @return The cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Get the monthly cost of the subscription
     * @param cost The cost to set the subscription at
     * @throws NegativeCostException If the cost is negative
     */
    public void setCost(double cost) throws NegativeCostException {
        if (cost < 0) {
            throw new NegativeCostException("Cost is negative.");
        }
        else this.cost = cost;
    }

    /**
     * Return a newline split representation of the Subscription
     * for debug purposes
     *
     * @return The Subscription
     */
    public String toString() {
        String out;
        out =  "Name:    "  + getName() + "\n";
        out += "Comment: " + getComment() + "\n";
        out += "Amount:  " + getCost() + "\n";

        return out;
    }

    /**
     * Return a colon (:) seperated string representing the Subscription
     * @return The Subscription in string form
     */
    public String save() {
        return getName() + " : " + getDateString() + " : " + getComment() + " : " + getCost() + "\n";
    }

    /**
     * Set the parameters of the string from a String
     * @param name The string representation of the Subscription
     */
    public static Subscription Load(String name) {
        Subscription sub;
        SimpleDateFormat sm = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String[] tokens = name.split(":");
        String sname = tokens[0];
        Date sdate = null;
        try {
            sdate = sm.parse(tokens[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String scomment = tokens[2];
        double scost = Double.valueOf(tokens[3]);

        try {
            sub = new Subscription(sname, scost, sdate, scomment);
            return sub;
        } catch (NameTooLongException | NegativeCostException | CommentTooLongException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Returns a Bundle Representation of the Subscription for passing between
     * Android Activities
     *
     * @return The Bundle Representation
     */
    public Bundle toBundle() {
        SimpleDateFormat sm = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Bundle bundle = new Bundle();
        bundle.putString("Name", getName());
        bundle.putString("Comment", getComment());
        bundle.putString("Date", sm.format(getDate()));
        bundle.putDouble("Amount", getCost());

        return bundle;
    }
}
