package com.gmail.eksuzyan.pavel.money.transfer.it.view;

import com.gmail.eksuzyan.pavel.money.transfer.it.view.util.MockJerseyConfig;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;
import com.gmail.eksuzyan.pavel.transfer.money.media.json.wrappers.acc.AccountWrapper;
import com.gmail.eksuzyan.pavel.transfer.money.media.json.wrappers.acc.AccountsWrapper;
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
 * Account endpoint integration testing.
 *
 * @author Pavel Eksuzian.
 *         Created: 29.10.2018.
 */
public class AccountEndpointTest {

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
    public void testCreateAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        AccountWrapper expected = new AccountWrapper(accNum, accAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(expected))
                .invoke();

        AccountWrapper actual = response.readEntity(AccountWrapper.class);

        assertEquals(200, response.getStatus());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    public void testCreateAccountReturnsNoContent() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        storage.put(accNum, new Account(accNum, accAmount));
        AccountWrapper expected = new AccountWrapper(accNum, accAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(expected))
                .invoke();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testCreateAccountReturnsBadRequest() {
        String accNum = "";
        Double accAmount = 1.0;
        AccountWrapper expected = new AccountWrapper(accNum, accAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(expected))
                .invoke();

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        storage.put(accNum, new Account(accNum, accAmount));

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + accNum)
                .request()
                .accept(APPLICATION_JSON)
                .buildGet()
                .invoke();

        AccountWrapper actual = response.readEntity(AccountWrapper.class);

        assertEquals(200, response.getStatus());
        assertEquals(accNum, actual.getNumber());
        assertEquals(accAmount, actual.getAmount());
    }

    @Test
    public void testGetAccountReturnsNoContent() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        storage.put(accNum, new Account(accNum, accAmount));

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + "TEST-2")
                .request()
                .accept(APPLICATION_JSON)
                .buildGet()
                .invoke();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testGetAccounts() {
        String accNum1 = "TEST-1";
        String accNum2 = "TEST-2";
        Double accAmount1 = 1.0;
        Double accAmount2 = 2.0;
        storage.put(accNum1, new Account(accNum1, accAmount1));
        storage.put(accNum2, new Account(accNum2, accAmount2));

        Response response = client
                .target(TEST_URI)
                .path("/accounts")
                .request()
                .accept(APPLICATION_JSON)
                .buildGet()
                .invoke();

        AccountsWrapper actual = response.readEntity(AccountsWrapper.class);
        Collection<AccountWrapper> accounts = actual.getAccountWrappers();

        assertEquals(200, response.getStatus());
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(acc ->
                Objects.equals(accNum1, acc.getNumber()) && Objects.equals(accAmount1, acc.getAmount())));
        assertTrue(accounts.stream().anyMatch(acc ->
                Objects.equals(accNum2, acc.getNumber()) && Objects.equals(accAmount2, acc.getAmount())));
    }

    @Test
    public void testUpdateAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        Double newAccAmount = 2.0;
        storage.put(accNum, new Account(accNum, accAmount));

        AccountWrapper expected = new AccountWrapper(null, newAccAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + accNum)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPut(Entity.json(expected))
                .invoke();

        AccountWrapper actual = response.readEntity(AccountWrapper.class);

        assertEquals(200, response.getStatus());
        assertEquals(accNum, actual.getNumber());
        assertEquals(newAccAmount, actual.getAmount());
    }

    @Test
    public void testUpdateAccountReturnsNoContent() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        Double newAccAmount = 2.0;
        storage.put(accNum, new Account(accNum, accAmount));

        AccountWrapper expected = new AccountWrapper(null, newAccAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + "TEST-2")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPut(Entity.json(expected))
                .invoke();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testUpdateAccountReturnsBadRequest() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        Double newAccAmount = null;
        storage.put(accNum, new Account(accNum, accAmount));

        AccountWrapper expected = new AccountWrapper(null, newAccAmount);

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + accNum)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPut(Entity.json(expected))
                .invoke();

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testDeleteAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        storage.put(accNum, new Account(accNum, accAmount));

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + accNum)
                .request()
                .accept(APPLICATION_JSON)
                .buildDelete()
                .invoke();

        AccountWrapper actual = response.readEntity(AccountWrapper.class);

        assertEquals(200, response.getStatus());
        assertEquals(accNum, actual.getNumber());
        assertEquals(accAmount, actual.getAmount());
    }

    @Test
    public void testDeleteAccountReturnsNoContent() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        storage.put(accNum, new Account(accNum, accAmount));

        Response response = client
                .target(TEST_URI)
                .path("/accounts/" + "TEST-2")
                .request()
                .accept(APPLICATION_JSON)
                .buildDelete()
                .invoke();

        assertEquals(204, response.getStatus());
    }

}
