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

    public void transferMoney(String fromAccountNum, String toAccountNum, double amount) {
        Account fromAccount, toAccount;
        try {
            fromAccount = datastore.getAccount(fromAccountNum);
            toAccount = datastore.getAccount(toAccountNum);
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not transfer money from '" + fromAccountNum + "' to '" +
                            toAccountNum + "'. Reason: " + e.getMessage(), e);
        }

        fromAccount.transferTo(toAccount, amount);
    }

}
