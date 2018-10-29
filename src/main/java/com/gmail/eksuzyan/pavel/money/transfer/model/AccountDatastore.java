package com.gmail.eksuzyan.pavel.money.transfer.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
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
    private ConcurrentHashMap<String, Account> userAccounts;

    /**
     * Main constructor to build up datastore with passed storage.
     *
     * @param userAccounts user accounts storage
     * @throws NullPointerException if storage is null
     */
    @Inject
    public AccountDatastore(@Named("storage") ConcurrentHashMap<String, Account> userAccounts) {
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
