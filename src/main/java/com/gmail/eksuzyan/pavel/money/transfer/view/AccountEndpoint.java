package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountsWrapper;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/25/2018.
 */
@Path("/accounts")
public class AccountEndpoint {

    /**
     * Underlying account service.
     */
    private final AccountService accService;

    private final TransactionEndpoint txEndpoint;

    /**
     * Main constructor to build up endpoint with passed accService.
     *
     * @param accService account service
     * @param txEndpoint transaction endpoint
     * @throws NullPointerException if accService is null
     */
    @Inject
    public AccountEndpoint(AccountService accService, TransactionEndpoint txEndpoint) {
        this.accService = requireNonNull(accService);
        this.txEndpoint = requireNonNull(txEndpoint);
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

        accService.createAccount(account.getNumber(), account.getAmount());

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

        Account account = accService.getAccount(accountNum);

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
        Collection<Account> accounts = accService.getAllAccounts().values();

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

        Account account = accService.updateAccount(accountNum, accountWrapper.getAmount());

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

        Account account = accService.deleteAccount(accountNum);

        return new AccountWrapper(account.getNumber(), account.getAmount());
    }

    @Path("/tx")
    public TransactionEndpoint getTxEndpoint() {
        return txEndpoint;
    }

}
