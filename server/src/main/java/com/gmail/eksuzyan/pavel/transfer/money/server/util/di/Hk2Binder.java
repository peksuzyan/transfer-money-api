package com.gmail.eksuzyan.pavel.transfer.money.server.util.di;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.service.SensibleAccountService;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.datastore.MemoryAccountDatastore;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.tx.TransactionEndpoint;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * HK2 dependency injection binder.
 *
 * @author Pavel Eksuzian.
 *         Created: 28.10.2018.
 */
public final class Hk2Binder extends AbstractBinder {

    /**
     * Declares and binds injectable resources (i.e. implementations) with their contracts.
     */
    @Override
    protected void configure() {
        bindFactory(ConcurrentMapFactory.class).to(new TypeLiteral<ConcurrentMap<String, Account>>() {
        }).named("storage");

        bind(MemoryAccountDatastore.class).to(AccountDatastore.class).in(Singleton.class);
        bind(SensibleAccountService.class).to(AccountService.class).in(Singleton.class);

        bindAsContract(TransactionEndpoint.class).in(Singleton.class);
        bindAsContract(AccountEndpoint.class).in(Singleton.class);
    }

    private static class ConcurrentMapFactory implements Factory<ConcurrentMap<String, Account>> {

        @Override
        public ConcurrentMap<String, Account> provide() {
            return new ConcurrentHashMap<>();
        }

        @Override
        public void dispose(ConcurrentMap<String, Account> instance) {
            if (instance != null) instance.clear();
        }
    }

}
