package com.gmail.eksuzyan.pavel.transfer.money.server.util.rs;

import com.gmail.eksuzyan.pavel.transfer.money.server.util.di.Hk2Binder;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.acc.AccountEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.endpoints.tx.TransactionEndpoint;
import com.gmail.eksuzyan.pavel.transfer.money.server.view.handlers.ExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Jersey configurable resource config.
 *
 * @author Pavel Eksuzian.
 *         Created: 28.10.2018.
 */
public class JerseyConfig extends ResourceConfig {

    /**
     * Registers JAX-RS resources as part of public API as soon as config is constructed.
     */
    public JerseyConfig() {
        register(new Hk2Binder());

        register(TransactionEndpoint.class);
        register(AccountEndpoint.class);
        register(ExceptionHandler.class);
    }
}
