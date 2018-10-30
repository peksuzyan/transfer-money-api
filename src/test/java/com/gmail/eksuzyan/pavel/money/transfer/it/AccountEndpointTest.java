package com.gmail.eksuzyan.pavel.money.transfer.it;

import com.gmail.eksuzyan.pavel.money.transfer.it.util.MockHk2Binder;
import com.gmail.eksuzyan.pavel.money.transfer.unit.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.util.rs.JerseyConfig;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountsWrapper;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.*;

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

    private static final URI TEST_URI = UriBuilder.fromUri("http://localhost").port(9991).build();

    private static ConcurrentMap<String, Account> datastore = new ConcurrentHashMap<>();
    private static Server server;

    private Client client;

    @BeforeClass
    public static void setUp() {
        MockHk2Binder.storage = datastore;
        ResourceConfig config = new JerseyConfig(new MockHk2Binder());
        server = JettyHttpContainerFactory.createServer(TEST_URI, config);
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void clear() {
        if (client != null)
            client.close();

        datastore.clear();
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
    public void testGetAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        datastore.put(accNum, new Account(accNum, accAmount));

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
    public void testGetAccounts() {
        String accNum1 = "TEST-1";
        String accNum2 = "TEST-2";
        Double accAmount1 = 1.0;
        Double accAmount2 = 2.0;
        datastore.put(accNum1, new Account(accNum1, accAmount1));
        datastore.put(accNum2, new Account(accNum2, accAmount2));

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
        datastore.put(accNum, new Account(accNum, accAmount));

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
    public void testDeleteAccount() {
        String accNum = "TEST-1";
        Double accAmount = 1.0;
        datastore.put(accNum, new Account(accNum, accAmount));

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

}
