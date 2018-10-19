package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import java.util.Objects;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountEndpoint {

    private final AccountService service;

    public AccountEndpoint() {
        this(new AccountService());
    }

    @SuppressWarnings("WeakerAccess")
    public AccountEndpoint(AccountService service) {
        this.service = service;
    }

    public AccountWrapper createAccount(String accountNum, double initialAmount) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (initialAmount < 0)
            throw new IllegalArgumentException("Initial amount cannot be negative. ");

        service.createAccount(accountNum, initialAmount);

        return new AccountWrapper(accountNum, initialAmount);
    }

    public AccountWrapper getAccount(String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.getAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    public AccountWrapper deleteAccount(String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.deleteAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

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
