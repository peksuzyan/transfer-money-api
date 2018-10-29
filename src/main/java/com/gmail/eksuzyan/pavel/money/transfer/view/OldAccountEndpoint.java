package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Publishes API to access and manipulate user accounts.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class OldAccountEndpoint {

    /**
     * Underlying service.
     */
    private final AccountService service;

    /**
     * Default constructor to build up endpoint with default service.
     */
    public OldAccountEndpoint() {
        this(new AccountService(new AccountDatastore(null)));
    }

    public String test() {
        return "Test";
    }

    /**
     * Main constructor to build up endpoint with passed service.
     *
     * @param service service
     * @throws NullPointerException if service is null
     */
    @SuppressWarnings("WeakerAccess")
    public OldAccountEndpoint(AccountService service) {
        this.service = requireNonNull(service);
    }

    /**
     * Creates user account.
     *
     * @param accountNum    user account number
     * @param initialAmount user account initial amount
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    public AccountWrapper createAccount(String accountNum, double initialAmount) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (initialAmount < 0)
            throw new IllegalArgumentException("Initial amount cannot be negative. ");

        service.createAccount(accountNum, initialAmount);

        return new AccountWrapper(accountNum, initialAmount);
    }

    /**
     * Gets user account by its number.
     *
     * @param accountNum user account number
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    public AccountWrapper getAccount(String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.getAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    /**
     * Deletes user account.
     *
     * @param accountNum user account number
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    public AccountWrapper deleteAccount(String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.deleteAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    /**
     * Transfers amount of money between user accounts.
     *
     * @param fromAccountNum user account number where amount is withdrawn from
     * @param toAccountNum   user account number where amount is deposited in
     * @param amount         amount of money to transfer
     * @return transaction representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    public TransactionWrapper transferMoney(String fromAccountNum, String toAccountNum, double amount) {
        if (fromAccountNum == null || fromAccountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (toAccountNum == null || toAccountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (Objects.equals(fromAccountNum, toAccountNum))
            throw new IllegalArgumentException("Account numbers are the same. ");

        if (amount <= 0)
            throw new IllegalArgumentException("Transfer amount cannot be negative or zero. ");

        service.transferMoney(fromAccountNum, toAccountNum, amount);

        return new TransactionWrapper(fromAccountNum, toAccountNum, amount);
    }

}
