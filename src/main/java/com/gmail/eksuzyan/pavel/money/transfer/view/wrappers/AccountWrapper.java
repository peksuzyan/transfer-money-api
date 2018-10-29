package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User account wrapper in order to be represented in public API.
 * <p>
 * Not thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
@XmlRootElement
public class AccountWrapper {

    /**
     * User account number.
     */
    @XmlElement
    private String number;

    /**
     * User account amount.
     */
    @XmlElement
    private Double amount;

    /**
     * Default constructor.
     */
    public AccountWrapper() {
    }

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
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount amount
     */
    public void setAmount(Double amount) {
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
