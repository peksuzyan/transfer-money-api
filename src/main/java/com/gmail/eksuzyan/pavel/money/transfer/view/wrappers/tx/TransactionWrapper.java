package com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.tx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transaction wrapper in order to be represented in public API.
 * <p>
 * Not thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/18/2018.
 */
@XmlRootElement
public class TransactionWrapper {

    /**
     * User account number where amount is withdrawn from.
     */
    @XmlElement(name = "src")
    private String srcNum;

    /**
     * User account number where amount is deposited in.
     */
    @XmlElement(name = "dest")
    private String destNum;

    /**
     * Amount of money to transfer.
     */
    @XmlElement
    private Double amount;

    /**
     * Default constructor.
     */
    public TransactionWrapper() {
    }

    /**
     * Single constructor.
     *
     * @param srcNum  user account number where amount is withdrawn from
     * @param destNum user account number where amount is deposited in
     * @param amount  amount of money to transfer
     */
    public TransactionWrapper(String srcNum, String destNum, Double amount) {
        this.srcNum = srcNum;
        this.destNum = destNum;
        this.amount = amount;
    }

    /**
     * Gets srcNum.
     *
     * @return srcNum
     */
    public String getSrcNum() {
        return srcNum;
    }

    /**
     * Sets srcNum.
     *
     * @param srcNum srcNum
     */
    public void setSrcNum(String srcNum) {
        this.srcNum = srcNum;
    }

    /**
     * Gets destNum.
     *
     * @return destNum
     */
    public String getDestNum() {
        return destNum;
    }

    /**
     * Sets destNum.
     *
     * @param destNum destNum
     */
    public void setDestNum(String destNum) {
        this.destNum = destNum;
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
        return "TransactionWrapper{" +
                "srcNum='" + srcNum + '\'' +
                ", destNum='" + destNum + '\'' +
                ", amount=" + amount +
                '}';
    }
}
