package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountWrapper {

    public AccountWrapper(String number, double amount) {
        this.number = number;
        this.amount = amount;
    }

    private String number;
    private double amount;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccountWrapper{" +
                "number='" + number + '\'' +
                ", amount=" + amount +
                '}';
    }
}
