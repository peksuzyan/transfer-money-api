package com.gmail.eksuzyan.pavel.money.transfer.it.view.util;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.tx.TransactionEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.handlers.ExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;

/**
 * @author Pavel Eksuzian.
 * Created: 29.10.2018.
 */
public final class MockJerseyConfig extends ResourceConfig {

    public MockJerseyConfig(ConcurrentMap<String, Account> storage) {
        AccountDatastore accountDatastore = new AccountDatastore(storage);
        AccountService accountService = new AccountService(accountDatastore);
        TransactionEndpoint txEndpoint = new TransactionEndpoint(accountService);
        AccountEndpoint accEndpoint = new AccountEndpoint(accountService, txEndpoint);

        register(txEndpoint);
        register(accEndpoint);
        register(ExceptionHandler.class);
    }
}
