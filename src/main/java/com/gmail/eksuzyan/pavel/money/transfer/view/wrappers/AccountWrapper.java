package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

/**
 * User account wrapper in order to be represented in public API.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
@SuppressWarnings("unused")
public class AccountWrapper {

    /**
     * Single constructor.
     *
     * @param number user account number
     * @param amount user account amount
     */
    public AccountWrapper(String number, double amount) {
        this.number = number;
        this.amount = amount;
    }

    /**
     * User account number.
     */
    private String number;

    /**
     * User account amount.
     */
    private double amount;

    /**
     * Gets number.
     *
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets amount.
     *
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount amount
     */
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
