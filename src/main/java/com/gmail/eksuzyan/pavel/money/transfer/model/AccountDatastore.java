package com.gmail.eksuzyan.pavel.money.transfer.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Stores and provides ways to manipulate user accounts.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountDatastore {

    /**
     * Provides common place to store and get user accounts associating ones with their numbers.
     */
    private final ConcurrentMap<String, Account> userAccounts = new ConcurrentHashMap<>();

    /**
     * Creates user account.
     *
     * @param newAccount user account
     * @throws DatastoreException if user account already exists
     */
    public void createAccount(Account newAccount) throws DatastoreException {
        String accountNum = newAccount.getNumber();

        Account account = userAccounts.putIfAbsent(accountNum, newAccount);

        if (account != null)
            throw new DatastoreException("Account '" + accountNum + "' already exists. ");
    }

    /**
     * Gets user account by its number.
     *
     * @param accountNum user account number
     * @return user account
     * @throws DatastoreException if user account isn't found
     */
    public Account getAccount(String accountNum) throws DatastoreException {
        Account account = userAccounts.get(accountNum);

        if (account == null)
            throw new DatastoreException("Account '" + accountNum + "' is not found. ");

        return account;
    }

    /**
     * Deletes user account.
     *
     * @param account user account
     * @throws DatastoreException if user account isn't found
     */
    public void deleteAccount(Account account) throws DatastoreException {
        String accountNum = account.getNumber();

        boolean deleted = userAccounts.remove(accountNum, account);

        if (!deleted)
            throw new DatastoreException("Account '" + accountNum + "' is not found. ");
    }

}
