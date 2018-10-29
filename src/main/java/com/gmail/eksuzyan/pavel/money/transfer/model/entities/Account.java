package com.gmail.eksuzyan.pavel.money.transfer.model.entities;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;

import static java.lang.System.identityHashCode;

/**
 * Describes user account.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class Account {

    /**
     * Locking on this object is used every time when the locking order cannot be determined based
     * on {@link System#identityHashCode(Object)}.
     */
    private static final Object GLOBAL_LOCK = new Object();

    /**
     * User account number.
     */
    private final String number;

    /**
     * User account current amount.
     */
    private Double amount;

    /**
     * Single constructor.
     *
     * @param number        user account number
     * @param initialAmount user account initial amount
     */
    public Account(String number, Double initialAmount) {
        this.number = number;
        this.amount = initialAmount;
    }

    /**
     * Gets user account number.
     *
     * @return user account number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gets user account amount.
     *
     * @return user account amount
     */
    public synchronized Double getAmount() {
        return amount;
    }

    /**
     * Sets user account amount.
     *
     * @param amount user account amount
     */
    public synchronized void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Withdraws <code>amount</code> from <code>srcAcc</code> user account and deposits in <code>destAcc</code> one.
     *
     * @param srcAcc  user account where money is being transferred from
     * @param destAcc user account where money is being transferred to
     * @param amount  amount of money to transfer
     * @throws BusinessException if this user account doesn't have enough amount to transfer
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static void transferAmount(Account srcAcc, Account destAcc, Double amount) throws BusinessException {
        final int srcHash = identityHashCode(srcAcc);
        final int destHash = identityHashCode(destAcc);

        if (srcHash < destHash) {
            synchronized (srcAcc) {
                synchronized (destAcc) {
                    transfer(srcAcc, destAcc, amount);
                }
            }
        } else if (srcHash > destHash) {
            synchronized (destAcc) {
                synchronized (srcAcc) {
                    transfer(srcAcc, destAcc, amount);
                }
            }
        } else {
            synchronized (GLOBAL_LOCK) {
                synchronized (srcAcc) {
                    synchronized (destAcc) {
                        transfer(srcAcc, destAcc, amount);
                    }
                }
            }
        }
    }

    private static void transfer(Account srcAcc, Account destAcc, Double amount) {
        if (srcAcc.getAmount() < amount)
            throw new BusinessException(
                    "Could not transfer from '" + srcAcc.number + "' to '" +
                            destAcc.number + "'. Reason: Not enough money. ");

        srcAcc.amount -= amount;
        destAcc.amount += amount;
    }

}
