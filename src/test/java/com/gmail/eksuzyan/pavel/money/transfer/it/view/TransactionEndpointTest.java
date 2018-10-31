package com.gmail.eksuzyan.pavel.money.transfer.it.view;

import com.gmail.eksuzyan.pavel.money.transfer.it.view.util.MockJerseyConfig;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.acc.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.acc.AccountsWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.tx.TransactionWrapper;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Transaction endpoint integration testing.
 *
 * @author Pavel Eksuzian.
 *         Created: 29.10.2018.
 */
public class TransactionEndpointTest {

    private static final String TEST_SERVER_SCHEME = "http";
    private static final String TEST_SERVER_HOST = "localhost";
    private static final int TEST_SERVER_PORT = 9992;

    private static final URI TEST_URI = UriBuilder.fromUri("")
            .scheme(TEST_SERVER_SCHEME)
            .host(TEST_SERVER_HOST)
            .port(TEST_SERVER_PORT)
            .build();

    private static ConcurrentMap<String, Account> storage = new ConcurrentHashMap<>();
    private static Server server;

    private Client client;

    @BeforeClass
    public static void setUp() {
        ResourceConfig config = new MockJerseyConfig(storage);
        try {
            server = JettyHttpContainerFactory.createServer(TEST_URI, config);
        } catch (ProcessingException e) {
            throw new IllegalStateException("Port " + TEST_SERVER_PORT + " already in use. ", e);
        }
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void clear() {
        if (client != null)
            client.close();

        storage.clear();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (server != null)
            server.stop();
    }

    @Test
    public void testTransferMoney() {
        String accNum1 = "TEST-1";
        String accNum2 = "TEST-2";
        Double accAmount1 = 1.0;
        Double accAmount2 = 5.0;
        storage.put(accNum1, new Account(accNum1, accAmount1));
        storage.put(accNum2, new Account(accNum2, accAmount2));

        TransactionWrapper tx = new TransactionWrapper(accNum1, accNum2, 1.0);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/tx")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(tx))
                .invoke();

        AccountsWrapper actual = response.readEntity(AccountsWrapper.class);
        Collection<AccountWrapper> accounts = actual.getAccountWrappers();

        assertEquals(200, response.getStatus());
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(acc ->
                Objects.equals(accNum1, acc.getNumber()) && Objects.equals(0.0, acc.getAmount())));
        assertTrue(accounts.stream().anyMatch(acc ->
                Objects.equals(accNum2, acc.getNumber()) && Objects.equals(6.0, acc.getAmount())));
    }

    @Test
    public void testTransferMoneyReturnsNoContent() {
        String accNum1 = "TEST-1";
        String accNum2 = "TEST-2";
        Double accAmount1 = 1.0;
        Double accAmount2 = 5.0;
        storage.put(accNum1, new Account(accNum1, accAmount1));
        storage.put(accNum2, new Account(accNum2, accAmount2));

        TransactionWrapper tx = new TransactionWrapper(accNum1, accNum2, 2.0);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/tx")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(tx))
                .invoke();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testTransferMoneyReturnsBadRequest() {
        String accNum1 = "TEST-1";
        String accNum2 = "TEST-2";
        Double accAmount1 = 1.0;
        Double accAmount2 = 5.0;
        storage.put(accNum1, new Account(accNum1, accAmount1));
        storage.put(accNum2, new Account(accNum2, accAmount2));

        TransactionWrapper tx = new TransactionWrapper("", accNum2, 1.0);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/tx")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(tx))
                .invoke();

        assertEquals(400, response.getStatus());
    }

}
