package com.gmail.eksuzyan.pavel.transfer.money.server.model;

import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.exceptions.DatastoreException;

import java.util.List;

/**
 * Describes the ways to manipulate user accounts.
 *
 * @author Pavel Eksuzian.
 *         Created: 11/13/2018.
 */
public interface AccountDatastore {

    /**
     * Creates user account.
     *
     * @param newAcc user account
     * @throws DatastoreException if user account already exists
     */
    void createAccount(Account newAcc) throws DatastoreException;

    /**
     * Gets user account by its number.
     *
     * @param accNum user account number
     * @return user account
     * @throws DatastoreException if user account isn't found
     */
    Account getAccount(String accNum) throws DatastoreException;

    /**
     * Gets all user accounts.
     *
     * @return user account list
     */
    List<Account> getAllAccounts();

    /**
     * Deletes user account.
     *
     * @param acc user account
     * @throws DatastoreException if user account isn't found
     */
    void deleteAccount(Account acc) throws DatastoreException;

}
