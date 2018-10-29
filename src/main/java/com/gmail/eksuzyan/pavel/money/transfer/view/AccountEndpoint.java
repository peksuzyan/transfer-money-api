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
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Publishes API to access and manipulate user accounts.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/25/2018.
 */
@Path("/accounts")
public class AccountEndpoint {

    /**
     * Underlying account service.
     */
    private final AccountService accService;

    /**
     * Transaction endpoint.
     */
    private final TransactionEndpoint txEndpoint;

    /**
     * Single constructor.
     *
     * @param accService account service
     * @param txEndpoint transaction endpoint
     * @throws NullPointerException if account service or transaction endpoint is null
     */
    @Inject
    public AccountEndpoint(AccountService accService, TransactionEndpoint txEndpoint) {
        this.accService = requireNonNull(accService);
        this.txEndpoint = requireNonNull(txEndpoint);
    }

    /**
     * Creates user account.
     *
     * @param acc user account
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @POST
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public AccountWrapper createAccount(AccountWrapper acc) {
        accService.createAccount(acc.getNumber(), acc.getAmount());

        return acc;
    }

    /**
     * Gets user account by its number.
     *
     * @param accNum user account number
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @GET
    @Path("/{accNum}")
    @Produces({APPLICATION_JSON})
    public AccountWrapper getAccount(@PathParam("accNum") String accNum) {
        Account acc = accService.getAccount(accNum);

        return new AccountWrapper(acc.getNumber(), acc.getAmount());
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

        return accounts.stream()
                .map(acc -> new AccountWrapper(acc.getNumber(), acc.getAmount()))
                .collect(collectingAndThen(toList(), AccountsWrapper::new));
    }

    /**
     * Updates user account.
     *
     * @param accNum     user account number
     * @param accWrapper updatable values of user account
     * @return user account representation
     */
    @PUT
    @Path("/{accNum}")
    @Produces({APPLICATION_JSON})
    @Consumes({APPLICATION_JSON})
    public AccountWrapper updateAccount(@PathParam("accNum") String accNum, AccountWrapper accWrapper) {
        Account acc = accService.updateAccount(accNum, accWrapper.getAmount());

        return new AccountWrapper(acc.getNumber(), acc.getAmount());
    }

    /**
     * Deletes user account.
     *
     * @param accNum user account number
     * @return user account representation
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @DELETE
    @Path("/{accNum}")
    @Produces({APPLICATION_JSON})
    public AccountWrapper deleteAccount(@PathParam("accNum") String accNum) {
        Account acc = accService.deleteAccount(accNum);

        return new AccountWrapper(acc.getNumber(), acc.getAmount());
    }

    /**
     * Delegates requests for manipulation with transaction to responsible endpoint.
     *
     * @return transaction endpoint
     */
    @Path("/tx")
    public TransactionEndpoint getTxEndpoint() {
        return txEndpoint;
    }

}
