package com.gmail.eksuzyan.pavel.money.transfer.it.view.util;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.tx.TransactionEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.handlers.ExceptionHandler;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
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
