package com.gmail.eksuzyan.pavel.money.transfer.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import static java.lang.System.identityHashCode;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountService {

    private final AccountDatastore datastore;
    private final Object tieLock = new Object();

    public AccountService() {
        this(new AccountDatastore());
    }

    @SuppressWarnings("WeakerAccess")
    public AccountService(AccountDatastore datastore) {
        this.datastore = datastore;
    }

    public Account createAccount(String accountNum, double initialAmount) {
        Account account = new Account(accountNum, initialAmount);

        try {
            datastore.createAccount(account);
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not create an account '" + accountNum + "'. Reason: " + e.getMessage(), e);
        }

        return account;
    }

    public Account getAccount(String accountNum) {
        try {
            return datastore.getAccount(accountNum);
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not get an account '" + accountNum + "'. Reason: " + e.getMessage(), e);
        }
    }

    public Account deleteAccount(String accountNum) {
        try {
            Account account = datastore.getAccount(accountNum);

            datastore.deleteAccount(account);

            return account;
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not delete an account '" + accountNum + "'. Reason: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transferMoney(String fromAccountNum, String toAccountNum, double amount) {
        Account fromAccount, toAccount;
        try {
            fromAccount = datastore.getAccount(fromAccountNum);
            toAccount = datastore.getAccount(toAccountNum);
        } catch (DatastoreException e) {
            throw new BusinessException("Could not transfer money from '" + fromAccountNum + "' to '" +
                    toAccountNum + "'. Reason: " + e.getMessage(), e);
        }

        final int fromAccountHash = identityHashCode(fromAccount);
        final int toAccountHash = identityHashCode(toAccount);

        if (fromAccountHash < toAccountHash) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    transferMoneyWithoutBlock(fromAccount, toAccount, amount);
                }
            }
        } else if (fromAccountHash > toAccountHash) {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    transferMoneyWithoutBlock(fromAccount, toAccount, amount);
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        transferMoneyWithoutBlock(fromAccount, toAccount, amount);
                    }
                }
            }
        }
    }

    private static void transferMoneyWithoutBlock(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount.getAmount() < amount)
            throw new BusinessException("Could not transfer from '" + fromAccount.getNumber() + "' to '" +
                    toAccount.getNumber() + "'. Reason: Not enough money. ");

        toAccount.withdraw(amount);
        fromAccount.deposit(amount);
    }

}
