package com.gmail.eksuzyan.pavel.money.transfer.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;

/**
 * Stores and provides ways to manipulate user accounts.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 * Created: 10/17/2018.
 */
public class AccountDatastore {

    /**
     * Underlying storage.
     */
    private final ConcurrentMap<String, Account> userAccounts;

    /**
     * Default constructor to build up datastore with {@link ConcurrentHashMap#ConcurrentHashMap()} as storage.
     */
    public AccountDatastore() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Main constructor to build up datastore with passed storage.
     *
     * @param userAccounts user accounts storage
     * @throws NullPointerException if storage is null
     */
    public AccountDatastore(ConcurrentMap<String, Account> userAccounts) {
        this.userAccounts = requireNonNull(userAccounts);
    }

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
