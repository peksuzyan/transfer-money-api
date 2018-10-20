package com.gmail.eksuzyan.pavel.money.transfer;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.lineSeparator;
import static java.util.Objects.nonNull;

/**
 * Main entry point for simulating access and manipulation with RESTful API for user account processing.
 *
 * @author Pavel Eksuzian.
 * Created: 10/19/2018.
 */
public class App {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getHelpInfo());
        }
    }

    private static void run(String[] args) throws InterruptedException {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. ");

        int totalAccounts;
        try {
            totalAccounts = parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Accounts count has wrong format: " + args[0], e);
        }
        if (totalAccounts < 0)
            throw new IllegalArgumentException("Accounts count is negative. ");

        int totalTransfers;
        try {
            totalTransfers = parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Transfers count has wrong format: " + args[1], e);
        }
        if (totalTransfers < 0)
            throw new IllegalArgumentException("Transfers count is negative. ");

        Long randomizerInitState = null;
        if (args.length > 2) {
            try {
                randomizerInitState = parseLong(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Initial state of pseudo-random generator has wrong format: " + args[2], e);
            }
        }

        try (AppRunner runner = createNewAppRunner(totalAccounts, totalTransfers, randomizerInitState)) {
            runner.start();
        }
    }

    private static AppRunner createNewAppRunner(int totalAccounts, int totalTransfers, Long randomizerInitState) {
        return nonNull(randomizerInitState)
                ? new AppRunner(totalAccounts, totalTransfers, randomizerInitState)
                : new AppRunner(totalAccounts, totalTransfers);
    }

    private static String getHelpInfo() {
        return lineSeparator() +
                "The following args have to be or might be specified: " + lineSeparator() + lineSeparator() +
                "[totalAccounts] - total accounts count (int, zero or positive, mandatory) " + lineSeparator() +
                "[totalTransfers] - total transfers count (int, zero or positive, mandatory) " + lineSeparator() +
                "[randomizerInitState] - initial state of pseudo-random generator (long, optional) " + lineSeparator();
    }
}
