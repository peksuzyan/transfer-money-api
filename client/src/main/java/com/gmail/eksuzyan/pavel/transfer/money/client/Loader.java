package com.gmail.eksuzyan.pavel.transfer.money.client;

import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.file.LoadableRestProperties;
import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.RestProperties;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/31/2018.
 */
public final class Loader {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println(getHelpInfo());
        }
    }

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

        RestProperties props = new LoadableRestProperties();
        try (LoaderRunner loader =
                     createLoaderRunner(props, accountsCount, transfersCount, randomizerInitState)) {
            loader.start();
        }
    }

    private static LoaderRunner createLoaderRunner(RestProperties props,
                                                   int accountsCount,
                                                   int transfersCount,
                                                   Long randomizerInitState) {
        return nonNull(randomizerInitState)
                ? new LoaderRunner(props, accountsCount, transfersCount, randomizerInitState)
                : new LoaderRunner(props, accountsCount, transfersCount);
    }

    private static String getHelpInfo() {
        return lineSeparator() +
                "The following args have to be or might be specified: " + lineSeparator() +
                "[accountsCount] - total accounts count (int, >1, mandatory) " + lineSeparator() +
                "[transfersCount] - total transfers count (int, >0, mandatory) " + lineSeparator() +
                "[randomizerInitState] - initial state of pseudo-random generator (long, optional) " + lineSeparator();
    }
}
