package com.gmail.eksuzyan.pavel.transfer.money.client;

import com.gmail.eksuzyan.pavel.transfer.money.media.json.wrappers.acc.AccountWrapper;
import com.gmail.eksuzyan.pavel.transfer.money.media.json.wrappers.acc.AccountsWrapper;
import com.gmail.eksuzyan.pavel.transfer.money.media.json.wrappers.tx.TransactionWrapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/31/2018.
 */
public class Postman {

    private final URI uri;

    public Postman(int serverPort) {
        this.uri = UriBuilder.fromUri("")
                .scheme("http")
                .host("localhost")
                .port(serverPort)
                .build();
    }

    public void deliverCreateAccountReq(AccountWrapper acc) {
        final Client client = ClientBuilder.newClient();

        Response response = client
                .target(uri)
                .path("/accounts")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(acc))
                .invoke();

        checkOnFailure(response);

        client.close();
    }

    public AccountWrapper deliverGetAccountReq(String accNum) {
        final Client client = ClientBuilder.newClient();

        Response response = client
                .target(uri)
                .path("/accounts/" + accNum)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildGet()
                .invoke();

        checkOnFailure(response);

        AccountWrapper acc = response.readEntity(AccountWrapper.class);

        client.close();

        return acc;
    }

    public AccountWrapper deliverDeleteAccountReq(String accNum) {
        final Client client = ClientBuilder.newClient();

        Response response = client
                .target(uri)
                .path("/accounts/" + accNum)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildDelete()
                .invoke();

        checkOnFailure(response);

        AccountWrapper acc = response.readEntity(AccountWrapper.class);

        client.close();

        return acc;
    }

    public AccountsWrapper deliverTransferMoneyReq(TransactionWrapper tx) {
        final Client client = ClientBuilder.newClient();

        Response response = client
                .target(uri)
                .path("/accounts/tx")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .buildPost(Entity.json(tx))
                .invoke();

        checkOnFailure(response);

        AccountsWrapper accounts = response.readEntity(AccountsWrapper.class);

        client.close();

        return accounts;
    }

    private static void checkOnFailure(Response response) {
        if (response.getStatus() != 200) {
            Response.StatusType statusType = response.getStatusInfo();
            throw new RuntimeException(statusType.getStatusCode() + " - " + statusType.getReasonPhrase());
        }
    }

}