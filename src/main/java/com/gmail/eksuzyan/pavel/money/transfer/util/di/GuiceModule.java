package com.gmail.eksuzyan.pavel.money.transfer.util.di;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.view.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.TransactionEndpoint;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Pavel Eksuzian.
 * Created: 28.10.2018.
 */
public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConcurrentMap.class)
                .annotatedWith(Names.named("storage"))
                .toInstance(new ConcurrentHashMap<>());

        bind(AccountDatastore.class).in(Singleton.class);
        bind(AccountService.class).in(Singleton.class);
        bind(TransactionEndpoint.class).in(Singleton.class);
        bind(AccountEndpoint.class).in(Singleton.class);
    }

}
