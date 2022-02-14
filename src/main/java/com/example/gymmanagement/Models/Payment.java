package com.example.gymmanagement.Models;

public class Payment {

    private String time;
    private String subscription;
    private int amount;

    public Payment(String time, String subscription, int amount) {
        this.time = time;
        this.subscription = subscription;
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
