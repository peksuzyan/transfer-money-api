package com.gmail.eksuzyan.pavel.money.transfer.model.entities;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.System.identityHashCode;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class Account {

    private static final ReentrantReadWriteLock tieLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
        lock.readLock().lock();
        try {
            return amount;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void transferTo(Account other, double amount) {
        final int thisHash = identityHashCode(this);
        final int otherHash = identityHashCode(other);

        if (thisHash < otherHash) {
            this.lock.writeLock().lock();
            other.lock.writeLock().lock();
            try {
                depositIn(other, amount);
            } finally {
                other.lock.writeLock().unlock();
                this.lock.writeLock().unlock();
            }
        } else if (thisHash > otherHash) {
            other.lock.writeLock().lock();
            this.lock.writeLock().lock();
            try {
                depositIn(other, amount);
            } finally {
                this.lock.writeLock().unlock();
                other.lock.writeLock().unlock();
            }
        } else {
            tieLock.writeLock().lock();
            this.lock.writeLock().lock();
            other.lock.writeLock().lock();
            try {
                depositIn(other, amount);
            } finally {
                other.lock.writeLock().unlock();
                this.lock.writeLock().unlock();
                tieLock.writeLock().unlock();
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
