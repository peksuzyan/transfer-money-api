package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/18/2018.
 */
public class TransactionWrapper {

    private String fromAccountNum;
    private String toAccountNum;
    private double amount;

    public TransactionWrapper(String fromAccountNum, String toAccountNum, double amount) {
        this.fromAccountNum = fromAccountNum;
        this.toAccountNum = toAccountNum;
        this.amount = amount;
    }

    public String getFromAccountNum() {
        return fromAccountNum;
    }

    public void setFromAccountNum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }

    public String getToAccountNum() {
        return toAccountNum;
    }

    public void setToAccountNum(String toAccountNum) {
        this.toAccountNum = toAccountNum;
    }

    public double getAmount() {
        return amount;
    }

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
