package com.gmail.eksuzyan.pavel.money.transfer.model.entities;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.System.identityHashCode;

/**
 * Describes user account.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 * Created: 10/17/2018.
 */
public class Account {

    /**
     * Tie lock is used every time when the locking order cannot be determined based
     * on {@link System#identityHashCode(Object)}.
     */
    private static final Lock TIE_LOCK = new ReentrantLock();

    /**
     * Internal user account lock.
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * User account number.
     */
    private final String number;

    /**
     * User account current amount.
     */
    private double amount;

    /**
     * Single constructor.
     *
     * @param number        user account number
     * @param initialAmount user account initial amount
     */
    public Account(String number, double initialAmount) {
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
    public double getAmount() {
        lock.readLock().lock();
        try {
            return amount;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Withdraws <code>amount</code> from this user account and deposits in <code>other</code> one.
     *
     * @param other  user account where money is being transferred to
     * @param amount amount of money to transfer
     * @throws BusinessException if this user account doesn't have enough amount to transfer
     */
    @SuppressWarnings("Duplicates")
    public void transferTo(Account other, double amount) throws BusinessException {
        final int thisHash = identityHashCode(this);
        final int otherHash = identityHashCode(other);

        if (thisHash < otherHash) {
            this.lock.writeLock().lock();
            try {
                other.lock.writeLock().lock();
                try {
                    depositIn(other, amount);
                } finally {
                    other.lock.writeLock().unlock();
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } else if (thisHash > otherHash) {
            other.lock.writeLock().lock();
            try {
                this.lock.writeLock().lock();
                try {
                    depositIn(other, amount);
                } finally {
                    this.lock.writeLock().unlock();
                }
            } finally {
                other.lock.writeLock().unlock();
            }
        } else {
            TIE_LOCK.lock();
            try {
                this.lock.writeLock().lock();
                try {
                    other.lock.writeLock().lock();
                    try {
                        depositIn(other, amount);
                    } finally {
                        other.lock.writeLock().unlock();
                    }
                } finally {
                    this.lock.writeLock().unlock();
                }
            } finally {
                TIE_LOCK.unlock();
            }
        }
    }

    private void depositIn(Account other, double amount) {
        if (this.amount < amount)
            throw new BusinessException(
                    "Could not transfer from '" + this.number + "' to '" +
                            other.number + "'. Reason: Not enough money. ");

        this.amount -= amount;
        other.amount += amount;
    }
}
