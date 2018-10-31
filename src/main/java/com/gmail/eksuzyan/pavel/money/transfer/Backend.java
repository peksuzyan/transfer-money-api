package com.gmail.eksuzyan.pavel.money.transfer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.lang.System.lineSeparator;

/**
 * Entry point for simulating access and manipulation with RESTful API for user account processing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/19/2018.
 */
public final class Backend {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println(getHelpInfo());
        }
    }

    private static void run(String[] args) throws Exception {
        if (args.length < 1)
            throw new IllegalArgumentException("Too few arguments. ");

        int serverPort;
        try {
            serverPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Server port has wrong format: " + args[0], e);
        }

        BackendRunner backend = new BackendRunner(serverPort);
        try {
            waitForUserStop();
        } finally {
            backend.stop();
        }
    }

    private static void waitForUserStop() throws Exception {
        System.out.println("Type any key to stop server. ");
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            console.readLine();
        }
    }

    private static String getHelpInfo() {
        return lineSeparator() +
                "The following args have to be specified: " + lineSeparator() +
                "[serverPort] - server port (int, >=1000, mandatory) ";
    }

}
