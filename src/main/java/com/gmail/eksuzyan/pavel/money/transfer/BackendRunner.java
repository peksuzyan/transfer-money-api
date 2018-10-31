package com.gmail.eksuzyan.pavel.money.transfer;

import com.gmail.eksuzyan.pavel.money.transfer.util.rs.JerseyConfig;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Pavel Eksuzian.
 * Created: 31.10.2018.
 */
class BackendRunner {

    private final Server server;

    BackendRunner(int serverPort) {
        if (serverPort < 1_000)
            throw new IllegalArgumentException("Server port is less than one thousand. ");

        URI serverUri = UriBuilder.fromUri("")
                .scheme("http")
                .host("localhost")
                .port(serverPort)
                .build();

        server = JettyHttpContainerFactory.createServer(serverUri, new JerseyConfig());

        System.out.println("Server started on port " + serverPort + ". ");
    }

    void stop() throws Exception {
        if (!server.isStopping() && !server.isStopped())
            server.stop();

        System.out.println("Server stopped. ");
    }
}
