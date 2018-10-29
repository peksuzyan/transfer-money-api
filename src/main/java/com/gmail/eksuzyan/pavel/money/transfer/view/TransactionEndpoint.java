package com.gmail.eksuzyan.pavel.money.transfer.view;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountsWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Pavel Eksuzian.
 *         Created: 28.10.2018.
 */
@Path("/tx")
public class TransactionEndpoint {

    private final AccountService accService;

    @Inject
    public TransactionEndpoint(AccountService accService) {
        this.accService = requireNonNull(accService);
    }

    /**
     * Transfers amount of money between user accounts.
     *
     * @param tx describes where amount is withdrawn from and is deposited to
     * @return representation of affected user accounts
     * @throws IllegalArgumentException if request validation fails
     * @throws BusinessException        if business error happens
     */
    @POST
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

        Collection<Account> accounts = accService.transferMoney(tx.getSrcNum(), tx.getDestNum(), tx.getAmount());

        Collection<AccountWrapper> wrappers = accounts.stream()
                .map(acc -> new AccountWrapper(acc.getNumber(), acc.getAmount()))
                .collect(toList());

        return new AccountsWrapper(wrappers);
    }

}
