package com.gmail.eksuzyan.pavel.money.transfer.util.rs;

import com.gmail.eksuzyan.pavel.money.transfer.util.di.Hk2Binder;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.handlers.ExceptionHandler;
import com.gmail.eksuzyan.pavel.money.transfer.view.endpoints.tx.TransactionEndpoint;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Jersey configurable resource config.
 *
 * @author Pavel Eksuzian.
 * Created: 28.10.2018.
 */
public class JerseyConfig extends ResourceConfig {

    /**
     * Registers JAX-RS resources as part of public API as soon as config is constructed.
     */
    public JerseyConfig() {
        this(new Hk2Binder());
    }

    /**
     * Registers JAX-RS resources as part of public API as soon as config is constructed with custom binder.
     *
     * @param binder dependency injection binder
     */
    public JerseyConfig(AbstractBinder binder) {
        register(binder);

        register(TransactionEndpoint.class);
        register(AccountEndpoint.class);
        register(ExceptionHandler.class);
    }
}
