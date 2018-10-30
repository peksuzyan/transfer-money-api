package com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.tx;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.acc.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.acc.AccountsWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.tx.TransactionWrapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Publishes API to access and manipulate user transactions.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 28.10.2018.
 */
@Path("/tx")
public class TransactionEndpoint {

    /**
     * Underlying account service.
     */
    private final AccountService accService;

    /**
     * Main constructor to build up transaction endpoint with passed accService.
     *
     * @param accService account service
     * @throws NullPointerException if account service is null
     */
    @Inject
    public TransactionEndpoint(AccountService accService) {
        this.accService = requireNonNull(accService);
    }

    /**
     * Transfers amount of money between user accounts.
     *
     * @param tx transaction representation describes where amount is withdrawn from and is deposited to
     * @return representation of affected user accounts
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @POST
    @Consumes({APPLICATION_JSON})
    @Produces({APPLICATION_JSON})
    public AccountsWrapper transferMoney(TransactionWrapper tx) {
        Collection<Account> accounts = accService.transferMoney(tx.getSrcNum(), tx.getDestNum(), tx.getAmount());

        return accounts.stream()
                .map(acc -> new AccountWrapper(acc.getNumber(), acc.getAmount()))
                .collect(collectingAndThen(toList(), AccountsWrapper::new));
    }

}
