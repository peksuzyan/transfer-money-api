package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/25/2018.
 */
@Path("/accounts")
public class AccountEndpoint {

    /**
     * Underlying service.
     */
    private final AccountService service;

    /**
     * Default constructor to build up endpoint with default service.
     */
    public AccountEndpoint() {
        this(new AccountService());
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test";
    }

    public AccountEndpoint(AccountService service) {
        this.service = requireNonNull(service);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountWrapper createAccount(AccountWrapper account) {
        if (account.getNumber() == null || account.getNumber().trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (account.getAmount() < 0)
            throw new IllegalArgumentException("Initial amount cannot be negative. ");

        service.createAccount(account.getNumber(), account.getAmount());

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    @GET
    @Path("/{accountNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountWrapper getAccount(@PathParam("accountNum") String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.getAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    @DELETE
    @Path("/{accountNum}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountWrapper deleteAccount(@PathParam("accountNum") String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.deleteAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionWrapper transferMoney(TransactionWrapper tx) {
        if (tx.getFromAccountNum() == null || tx.getFromAccountNum().trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (tx.getToAccountNum() == null || tx.getToAccountNum().trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (Objects.equals(tx.getFromAccountNum(), tx.getToAccountNum()))
            throw new IllegalArgumentException("Account numbers are the same. ");

        if (tx.getAmount() <= 0)
            throw new IllegalArgumentException("Transfer amount cannot be negative or zero. ");

        service.transferMoney(tx.getFromAccountNum(), tx.getToAccountNum(), tx.getAmount());

        return new TransactionWrapper(tx.getFromAccountNum(), tx.getToAccountNum(), tx.getAmount());
    }
}
