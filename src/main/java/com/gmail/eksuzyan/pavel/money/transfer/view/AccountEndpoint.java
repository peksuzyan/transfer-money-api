package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountsWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Pavel Eksuzian.
 * Created: 10/25/2018.
 */
@Singleton
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

    /**
     * Main constructor to build up endpoint with passed service.
     *
     * @param service service
     * @throws NullPointerException if service is null
     */
    public AccountEndpoint(AccountService service) {
        this.service = requireNonNull(service);
    }

    /**
     * Creates user account.
     *
     * @param account user account
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @POST
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public AccountWrapper createAccount(AccountWrapper account) {
        if (account.getNumber() == null || account.getNumber().trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (account.getAmount() < 0)
            throw new IllegalArgumentException("Initial amount cannot be negative. ");

        service.createAccount(account.getNumber(), account.getAmount());

        return account;
    }

    /**
     * Gets user account by its number.
     *
     * @param accountNum user account number
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @GET
    @Path("/{accountNum}")
    @Produces({APPLICATION_JSON})
    public AccountWrapper getAccount(@PathParam("accountNum") String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.getAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    /**
     * Gets user accounts.
     *
     * @return user accounts representation
     */
    @GET
    @Produces({APPLICATION_JSON})
    public AccountsWrapper getAccounts() {
        Collection<Account> accounts = service.getAllAccounts().values();

        Collection<AccountWrapper> wrappers = accounts.stream()
                .map(acc -> new AccountWrapper(acc.getNumber(), acc.getAmount()))
                .collect(toList());

        return new AccountsWrapper(wrappers);
    }

    /**
     * Updates user account.
     *
     * @param accountNum     user account number
     * @param accountWrapper updatable values of user account
     * @return user account representation
     */
    @PUT
    @Path("/{accountNum}")
    @Produces({APPLICATION_JSON})
    @Consumes({APPLICATION_JSON})
    public AccountWrapper updateAccount(@PathParam("accountNum") String accountNum, AccountWrapper accountWrapper) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.updateAccount(accountNum, accountWrapper.getAmount());

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
    @DELETE
    @Path("/{accountNum}")
    @Produces({APPLICATION_JSON})
    public AccountWrapper deleteAccount(@PathParam("accountNum") String accountNum) {
        if (accountNum == null || accountNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        Account account = service.deleteAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    /**
     * Transfers amount of money between user accounts.
     *
     * @param tx describes where amount is withdrawn from and is deposited to
     * @return representation of affected user accounts
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @PUT
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public AccountsWrapper transferMoney(TransactionWrapper tx) {
        if (tx.getSrcNum() == null || tx.getSrcNum().trim().isEmpty())
            throw new IllegalArgumentException("Source account number cannot be null or empty. ");

        if (tx.getDestNum() == null || tx.getDestNum().trim().isEmpty())
            throw new IllegalArgumentException("Destination account number cannot be null or empty. ");

        if (Objects.equals(tx.getSrcNum(), tx.getDestNum()))
            throw new IllegalArgumentException("Account numbers are the same. ");

        if (tx.getAmount() <= 0)
            throw new IllegalArgumentException("Transfer amount cannot be negative or zero. ");

        Collection<Account> accounts = service.transferMoney(tx.getSrcNum(), tx.getDestNum(), tx.getAmount());

        Collection<AccountWrapper> wrappers = accounts.stream()
                .map(acc -> new AccountWrapper(acc.getNumber(), acc.getAmount()))
                .collect(toList());

        return new AccountsWrapper(wrappers);
    }
}
