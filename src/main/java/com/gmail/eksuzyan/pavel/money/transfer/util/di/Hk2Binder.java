package com.gmail.eksuzyan.pavel.money.transfer.util.di;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.TransactionEndpoint;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Pavel Eksuzian.
 *         Created: 28.10.2018.
 */
public class Hk2Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(ConcurrentMapFactory.class).to(new TypeLiteral<ConcurrentMap<String, Account>>() {
        }).named("storage");

        bindAsContract(AccountDatastore.class).in(Singleton.class);
        bindAsContract(AccountService.class).in(Singleton.class);
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
