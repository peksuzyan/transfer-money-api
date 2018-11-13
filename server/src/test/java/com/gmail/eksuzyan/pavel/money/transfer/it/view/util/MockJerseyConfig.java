package com.gmail.eksuzyan.pavel.money.transfer.it.view.util;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.service.SensibleAccountService;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.datastore.MemoryAccountDatastore;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.tx.TransactionEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.handlers.ExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Pavel Eksuzian.
 *         Created: 29.10.2018.
 */
public final class MockJerseyConfig extends ResourceConfig {

    public MockJerseyConfig(ConcurrentMap<String, Account> storage) {
        MemoryAccountDatastore accountDatastore = new MemoryAccountDatastore(storage);
        SensibleAccountService accountService = new SensibleAccountService(accountDatastore);
        TransactionEndpoint txEndpoint = new TransactionEndpoint(accountService);
        AccountEndpoint accEndpoint = new AccountEndpoint(accountService, txEndpoint);

        register(txEndpoint);
        register(accEndpoint);
        register(ExceptionHandler.class);
    }
}
