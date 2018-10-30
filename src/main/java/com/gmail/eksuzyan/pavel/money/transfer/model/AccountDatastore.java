package com.gmail.eksuzyan.pavel.money.transfer.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;

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
     * Underlying storage.
     */
    private ConcurrentMap<String, Account> userAccounts;

    /**
     * Main constructor to build up datastore with passed storage.
     *
     * @param userAccounts user accounts storage
     * @throws NullPointerException if storage is null
     */
    @Inject
    public AccountDatastore(@Named("storage") ConcurrentMap<String, Account> userAccounts) {
        this.userAccounts = requireNonNull(userAccounts);
    }

    /**
     * Creates user account.
     *
     * @param newAcc user account
     * @throws DatastoreException if user account already exists
     */
    public void createAccount(Account newAcc) throws DatastoreException {
        String accountNum = newAcc.getNumber();

        Account account = userAccounts.putIfAbsent(accountNum, newAcc);

        if (account != null)
            throw new DatastoreException("Account '" + accountNum + "' already exists. ");
    }

    /**
     * Gets user account by its number.
     *
     * @param accNum user account number
     * @return user account
     * @throws DatastoreException if user account isn't found
     */
    public Account getAccount(String accNum) throws DatastoreException {
        Account account = userAccounts.get(accNum);

        if (account == null)
            throw new DatastoreException("Account '" + accNum + "' is not found. ");

        return account;
    }

    /**
     * Gets all user accounts.
     *
     * @return user account list
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(userAccounts.values());
    }

    /**
     * Deletes user account.
     *
     * @param acc user account
     * @throws DatastoreException if user account isn't found
     */
    public void deleteAccount(Account acc) throws DatastoreException {
        String accNum = acc.getNumber();

        boolean deleted = userAccounts.remove(accNum, acc);

        if (!deleted)
            throw new DatastoreException("Account '" + accNum + "' is not found. ");
    }

}
