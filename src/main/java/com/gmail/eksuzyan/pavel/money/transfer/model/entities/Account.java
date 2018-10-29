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
     * Withdraws <code>amount</code> from this user account and deposits in <code>other</code> one.
     *
     * @param other  user account where money is being transferred to
     * @param amount amount of money to transfer
     * @throws BusinessException if this user account doesn't have enough amount to transfer
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transferTo(Account other, Double amount) throws BusinessException {
        final int thisHash = identityHashCode(this);
        final int otherHash = identityHashCode(other);

        if (thisHash < otherHash) {
            synchronized (this) {
                synchronized (other) {
                    transfer(this, other, amount);
                }
            }
        } else if (thisHash > otherHash) {
            synchronized (other) {
                synchronized (this) {
                    transfer(this, other, amount);
                }
            }
        } else {
            synchronized (GLOBAL_LOCK) {
                synchronized (this) {
                    synchronized (other) {
                        transfer(this, other, amount);
                    }
                }
            }
        }
    }

    private static void transfer(Account from, Account to, Double amount) {
        if (from.amount < amount)
            throw new BusinessException(
                    "Could not transfer from '" + from.number + "' to '" + to.number + "'. Reason: Not enough money. ");

        from.amount -= amount;
        to.amount += amount;
    }
}
