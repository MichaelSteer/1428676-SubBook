package com.steers.Subscription;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class Subscription {
    public static final int NAME_LENGTH = 20;
    public static final int COMMENT_LENGTH = 30;

    private String name, comment;
    private Date date;
    private double cost;

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

    public String getName() {
        return name;
    }

    public void setName(String name) throws NameTooLongException {
        if(name.length() > NAME_LENGTH)
            throw new NameTooLongException("Name length is: " + String.valueOf(name.length()));
        else this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if(comment.length() > NAME_LENGTH)
            throw new CommentTooLongException("Comment length is: " + String.valueOf(name.length()));
        else this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) throws NegativeCostException {
        if (cost < 0)
            throw new NegativeCostException("Cost is negative.");
        else this.cost = cost;
    }
}
