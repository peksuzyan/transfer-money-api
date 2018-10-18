package com.gmail.eksuzyan.pavel.money.transfer.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountDatastore {

    private final ConcurrentMap<String, Account> userAccounts = new ConcurrentHashMap<>();

    public void createAccount(Account newAccount) throws DatastoreException {
        Account account = userAccounts.putIfAbsent(newAccount.getNumber(), newAccount);

        if (account != null)
            throw new DatastoreException("Account '" + newAccount.getNumber() + "' already exists. ");
    }

    public Account getAccount(String accountNum) throws DatastoreException {
        Account account = userAccounts.get(accountNum);

        if (account == null)
            throw new DatastoreException("Account '" + accountNum + "' is not found. ");

        return account;
    }

    public void deleteAccount(Account account) throws DatastoreException {
        boolean deleted = userAccounts.remove(account.getNumber(), account);

        if (!deleted)
            throw new DatastoreException("Account '" + account.getNumber() + "' is not found. ");
    }

}
