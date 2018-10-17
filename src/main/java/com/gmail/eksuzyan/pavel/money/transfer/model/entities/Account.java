package com.gmail.eksuzyan.pavel.money.transfer.model.entities;

public class Account {

    private final String number;

    private double amount;

    public Account(String number, double initialAmount) {
        this.number = number;
        this.amount = initialAmount;
    }

    public String getNumber() {
        return number;
    }

    public double getAmount() {
        return amount;
    }

    public void withdraw(double amount) {
        this.amount -= amount;
    }

    public void deposit(double amount) {
        this.amount += amount;
    }
}
