package com.gmail.eksuzyan.pavel.money.transfer;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/31/2018.
 */
public class AppLoader {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getHelpInfo());
        }
    }

    private static void run(String[] args) throws InterruptedException {
        if (args.length < 3)
            throw new IllegalArgumentException("Too few arguments. ");

        int serverPort;
        try {
            serverPort = parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Server port has wrong format: " + args[0], e);
        }

        int accountsCount;
        try {
            accountsCount = parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Accounts count has wrong format: " + args[1], e);
        }

        int transfersCount;
        try {
            transfersCount = parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Transfers count has wrong format: " + args[2], e);
        }

        Long randomizerInitState = null;
        if (args.length > 3) {
            try {
                randomizerInitState = parseLong(args[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Initial state of pseudo-random generator has wrong format: " + args[3], e);
            }
        }

        try (AppLoaderRunner runner =
                     createAppLoaderRunner(serverPort, accountsCount, transfersCount, randomizerInitState)) {
            runner.start();
        }
    }

    private static AppLoaderRunner createAppLoaderRunner(int serverPort,
                                                         int accountsCount,
                                                         int transfersCount,
                                                         Long randomizerInitState) {
        return nonNull(randomizerInitState)
                ? new AppLoaderRunner(serverPort, accountsCount, transfersCount, randomizerInitState)
                : new AppLoaderRunner(serverPort, accountsCount, transfersCount);
    }

    private static String getHelpInfo() {
        return lineSeparator() +
                "The following args have to be or might be specified: " + lineSeparator() + lineSeparator() +
                "[serverPort] - server port (int, >=1000, mandatory) " + lineSeparator() +
                "[accountsCount] - total accounts count (int, >1, mandatory) " + lineSeparator() +
                "[transfersCount] - total transfers count (int, >0, mandatory) " + lineSeparator() +
                "[randomizerInitState] - initial state of pseudo-random generator (long, optional) " + lineSeparator();
    }
}
