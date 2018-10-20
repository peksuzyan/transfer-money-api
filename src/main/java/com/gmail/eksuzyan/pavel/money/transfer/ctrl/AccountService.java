package com.gmail.eksuzyan.pavel.money.transfer.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import static java.util.Objects.requireNonNull;

/**
 * Provides ways to manipulate user accounts from the business point of view.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 * Created: 10/17/2018.
 */
public class AccountService {

    /**
     * Underlying datastore.
     */
    private final AccountDatastore datastore;

    /**
     * Default constructor to build up service with in-memory datastore.
     */
    public AccountService() {
        this(new AccountDatastore());
    }

    /**
     * Main constructor to build up service with passed datastore.
     *
     * @param datastore datastore
     * @throws NullPointerException if datastore is null
     */
    AccountService(AccountDatastore datastore) {
        this.datastore = requireNonNull(datastore);
    }

    /**
     * Creates user account.
     *
     * @param accountNum    user account number
     * @param initialAmount user account initial amount
     * @throws BusinessException if user account cannot be created
     */
    public void createAccount(String accountNum, double initialAmount) {
        Account account = new Account(accountNum, initialAmount);

        try {
            datastore.createAccount(account);
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not create an account '" + accountNum + "'. Reason: " + e.getMessage(), e);
        }
    }

    /**
     * Get user account by its number.
     *
     * @param accountNum user account number
     * @return user account
     * @throws BusinessException if user account cannot be got
     */
    public Account getAccount(String accountNum) {
        try {
            return datastore.getAccount(accountNum);
        } catch (DatastoreException e) {
            throw new BusinessException(
                    "Could not get an account '" + accountNum + "'. Reason: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes user account.
     *
     * @param accountNum user account number
     * @return user account
     * @throws BusinessException if user account cannot be deleted
     */
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

    /**
     * Transfers amount of money between user accounts.
     *
     * @param fromAccountNum user account number where amount is withdrawn from
     * @param toAccountNum   user account number where amount is deposited in
     * @param amount         amount of money to transfer
     * @throws BusinessException if transfer cannot be performed
     */
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
