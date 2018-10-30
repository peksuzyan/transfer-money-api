package com.gmail.eksuzyan.pavel.money.transfer.it.view.util;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.tx.TransactionEndpoint;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;

/**
 * @author Pavel Eksuzian.
 * Created: 29.10.2018.
 */
@SuppressWarnings("Duplicates")
public final class MockHk2Binder extends AbstractBinder {

    public static ConcurrentMap<String, Account> storage;

    @Override
    protected void configure() {
        bindFactory(SettableConcurrentMapFactory.class).to(new TypeLiteral<ConcurrentMap<String, Account>>() {
        }).named("storage");

        bindAsContract(AccountDatastore.class).in(Singleton.class);
        bindAsContract(AccountService.class).in(Singleton.class);
        bindAsContract(TransactionEndpoint.class).in(Singleton.class);
        bindAsContract(AccountEndpoint.class).in(Singleton.class);
    }

    private static class SettableConcurrentMapFactory implements Factory<ConcurrentMap<String, Account>> {

        @Override
        public ConcurrentMap<String, Account> provide() {
            return requireNonNull(storage);
        }

        @Override
        public void dispose(ConcurrentMap<String, Account> instance) {
            if (instance != null) instance.clear();
        }
    }
}
