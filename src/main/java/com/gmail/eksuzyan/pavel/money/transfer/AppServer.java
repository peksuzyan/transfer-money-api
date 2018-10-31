package com.gmail.eksuzyan.pavel.money.transfer;

import com.gmail.eksuzyan.pavel.money.transfer.util.rs.JerseyConfig;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

/**
 * Entry point for simulating access and manipulation with RESTful API for user account processing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/19/2018.
 */
public class AppServer {

    private static final String SERVER_SCHEME = "http";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9998;

    private static final URI TEST_URI = UriBuilder.fromUri("")
            .scheme(SERVER_SCHEME)
            .host(SERVER_HOST)
            .port(SERVER_PORT)
            .build();

    public static void main(String[] args) {
        ResourceConfig config = new JerseyConfig();
        Server server = JettyHttpContainerFactory.createServer(TEST_URI, config);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (!Objects.equals(reader.readLine(), "stop")) {
                Thread.sleep(1_000);
            }
            doStop(server);
        } catch (IOException | InterruptedException e) {
            doStop(server);
            throw new IllegalStateException("Something went wrong. ", e);
        }
    }

    private static void doStop(Server server) {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Server could not stop properly. ", e);
        }
    }

}
