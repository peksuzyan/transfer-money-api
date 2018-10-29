package com.gmail.eksuzyan.pavel.money.transfer.it;

import com.gmail.eksuzyan.pavel.money.transfer.it.util.MockHk2Binder;
import com.gmail.eksuzyan.pavel.money.transfer.unit.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.util.rs.JerseyConfig;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Integration testing.
 *
 * @author Pavel Eksuzian.
 * Created: 29.10.2018.
 */
public class AccountEndpointTest {

    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost").port(9999).build();
    private static final ConcurrentMap<String, Account> STORAGE = new ConcurrentHashMap<>();
    private static Server server;

    @BeforeClass
    public static void setUp() {
        MockHk2Binder.storage = STORAGE;
        ResourceConfig config = new JerseyConfig(new MockHk2Binder());
        server = JettyHttpContainerFactory.createServer(BASE_URI, config);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.stop();
    }

}
