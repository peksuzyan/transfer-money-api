package com.gmail.eksuzyan.pavel.money.transfer.util.rs;

import com.gmail.eksuzyan.pavel.money.transfer.util.di.GuiceModule;
import com.gmail.eksuzyan.pavel.money.transfer.view.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.TransactionEndpoint;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Pavel Eksuzian.
 * Created: 28.10.2018.
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(new GuiceModule());

        register(TransactionEndpoint.class);
        register(AccountEndpoint.class);
    }
}
