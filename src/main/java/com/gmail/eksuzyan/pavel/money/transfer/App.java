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

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;

/**
 * Entry point for simulating access and manipulation with RESTful API for user account processing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/19/2018.
 */
public class App {

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

//    public static void main(String[] args) {
//        try {
//            run(args);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(getHelpInfo());
//        }
//    }

    private static void run(String[] args) throws InterruptedException {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. ");

        int accountsCount;
        try {
            accountsCount = parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Accounts count has wrong format: " + args[0], e);
        }

        int transfersCount;
        try {
            transfersCount = parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Transfers count has wrong format: " + args[1], e);
        }

        Long randomizerInitState = null;
        if (args.length > 2) {
            try {
                randomizerInitState = parseLong(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Initial state of pseudo-random generator has wrong format: " + args[2], e);
            }
        }

        try (AppRunner runner = createNewAppRunner(accountsCount, transfersCount, randomizerInitState)) {
            runner.start();
        }
    }

    private static AppRunner createNewAppRunner(int accountsCount, int transfersCount, Long randomizerInitState) {
        return nonNull(randomizerInitState)
                ? new AppRunner(accountsCount, transfersCount, randomizerInitState)
                : new AppRunner(accountsCount, transfersCount);
    }

    private static String getHelpInfo() {
        return lineSeparator() +
                "The following args have to be or might be specified: " + lineSeparator() + lineSeparator() +
                "[accountsCount] - total accounts count (int, >1, mandatory) " + lineSeparator() +
                "[transfersCount] - total transfers count (int, >0, mandatory) " + lineSeparator() +
                "[randomizerInitState] - initial state of pseudo-random generator (long, optional) " + lineSeparator();
    }
}
