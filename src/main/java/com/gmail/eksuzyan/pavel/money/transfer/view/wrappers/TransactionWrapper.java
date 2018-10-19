package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

/**
 * Transaction wrapper in order to be represented in public API.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/18/2018.
 */
@SuppressWarnings("unused")
public class TransactionWrapper {

    /**
     * User account number where amount is withdrawn from.
     */
    private String fromAccountNum;

    /**
     * User account number where amount is deposited in.
     */
    private String toAccountNum;

    /**
     * Amount of money to transfer.
     */
    private double amount;

    /**
     * Single constructor.
     *
     * @param fromAccountNum user account number where amount is withdrawn from
     * @param toAccountNum   user account number where amount is deposited in
     * @param amount         amount of money to transfer
     */
    public TransactionWrapper(String fromAccountNum, String toAccountNum, double amount) {
        this.fromAccountNum = fromAccountNum;
        this.toAccountNum = toAccountNum;
        this.amount = amount;
    }

    /**
     * Gets fromAccountNum.
     *
     * @return fromAccountNum
     */
    public String getFromAccountNum() {
        return fromAccountNum;
    }

    /**
     * Sets fromAccountNum.
     *
     * @param fromAccountNum fromAccountNum
     */
    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }

    /**
     * Gets toAccountNum.
     *
     * @return toAccountNum
     */
    public String getToAccountNum() {
        return toAccountNum;
    }

    /**
     * Sets toAccountNum.
     *
     * @param toAccountNum toAccountNum
     */
    public void setToAccountNum(String toAccountNum) {
        this.toAccountNum = toAccountNum;
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
        return "TransactionWrapper{" +
                "fromAccountNum='" + fromAccountNum + '\'' +
                ", toAccountNum='" + toAccountNum + '\'' +
                ", amount=" + amount +
                '}';
    }
}
