package com.gmail.eksuzyan.pavel.money.transfer;

import com.gmail.eksuzyan.pavel.money.transfer.view.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.TransactionWrapper;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.random;
import static java.lang.String.format;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class Simulator implements AutoCloseable {

    private static final String HEADLINE_PATTERN = "============== Requesting '%s' ==============";
//    private static final String FINISH_OUTPUT_PATTERN = "============== Finish to request '%s' ==============";

    private static final Format ACCOUNT_NUMBER_FORMATTER = new DecimalFormat("ACC-0000");

    private static final int MAX_INITIAL_AMOUNT = 100;
    private static final int MAX_TRANSFER_AMOUNT = 25;

    private final AtomicLong totalInitialAmount = new AtomicLong();
    private final AtomicLong totalFiniteAmount = new AtomicLong();

    private final AccountEndpoint rest = new AccountEndpoint();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final int totalAccounts;
    private final int totalTransfers;

    private final CountDownLatch latchOnCreate;
    private final CountDownLatch latchOnTransfer;
    private final CountDownLatch latchOnGet;
    private final CountDownLatch latchOnDelete;

    private Simulator(int totalAccounts, int totalTransfers) {
        this.totalAccounts = totalAccounts;
        this.totalTransfers = totalTransfers;

        this.latchOnCreate = new CountDownLatch(totalAccounts);
        this.latchOnTransfer = new CountDownLatch(totalTransfers);
        this.latchOnGet = new CountDownLatch(totalAccounts);
        this.latchOnDelete = new CountDownLatch(totalAccounts);
    }

    public static void main(String[] args) {
        try (Simulator simulator = new Simulator(5, 100)) {
            simulator.simulate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void simulate() throws InterruptedException {
        List<String> accountNums = generateTestAccounts(totalAccounts);

        requestCreate(accountNums);
        requestTransfer(accountNums);
        requestGet(accountNums);
        requestDelete(accountNums);

        System.out.println("Total initial amount: " + totalInitialAmount.get());
        System.out.println("Total finite amount: " + totalFiniteAmount.get());
    }

    private void requestCreate(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "CREATE"));

        for (String accountNum : accountNums) {
            threadPool.execute(new CreateTask(accountNum, generateInitialAmount()));
        }

        latchOnCreate.await();
        System.out.println();
//        System.out.println(format(FINISH_OUTPUT_PATTERN, "CREATE"));
    }

    private void requestTransfer(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "TRANSFER"));

        final TransferTaskFactory transferTaskFactory = new TransferTaskFactory(accountNums);
        for (int i = 0; i < totalTransfers; i++) {
            threadPool.execute(transferTaskFactory.createNew());
        }

        latchOnTransfer.await();
        System.out.println();
//        System.out.println(format(FINISH_OUTPUT_PATTERN, "TRANSFER"));
    }

    private void requestGet(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "GET"));

        for (String accountNum : accountNums) {
            threadPool.execute(new GetTask(accountNum));
        }

        latchOnGet.await();
        System.out.println();
//        System.out.println(format(FINISH_OUTPUT_PATTERN, "GET"));
    }

    private void requestDelete(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "DELETE"));

        for (String accountNum : accountNums) {
            threadPool.execute(new DeleteTask(accountNum));
        }

        latchOnDelete.await();
        System.out.println();
//        System.out.println(format(FINISH_OUTPUT_PATTERN, "DELETE"));
    }

    private static List<String> generateTestAccounts(int totalAccounts) {
        return IntStream
                .rangeClosed(1, totalAccounts)
                .mapToObj(ACCOUNT_NUMBER_FORMATTER::format)
                .collect(Collectors.toList());
    }

    private static int generateInitialAmount() {
        return (int) (random() * MAX_INITIAL_AMOUNT);
    }

    private static int generateTransferAmount() {
        return (int) (random() * MAX_TRANSFER_AMOUNT + 1);
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }

    private class TransferTaskFactory {
        private final List<String> accounts;
        private final int maxIndex;

        private TransferTaskFactory(List<String> accounts) {
            this.accounts = accounts;
            this.maxIndex = accounts.size();
        }

        TransferTask createNew() {
            int fromIndex = generateIndex();

            int toIndex = generateIndex();
            while (fromIndex == toIndex) {
                toIndex = generateIndex();
            }

            return new TransferTask(accounts.get(fromIndex), accounts.get(toIndex), generateTransferAmount());
        }

        private int generateIndex() {
            return (int) (random() * maxIndex);
        }
    }

    private class CreateTask implements Runnable {
        private final String number;
        private final double initialAmount;

        CreateTask(String number, double initialAmount) {
            this.number = number;
            this.initialAmount = initialAmount;
        }

        @Override
        public void run() {
            try {
                AccountWrapper account = rest.createAccount(number, initialAmount);
                System.out.println("Created: " + account);
                totalInitialAmount.addAndGet((long) account.getAmount());
            } catch (Exception e) {
                System.out.println("Not created: " + e);
            } finally {
                latchOnCreate.countDown();
            }
        }
    }

    private class TransferTask implements Runnable {
        private final String fromAccountNum;
        private final String toAccountNum;
        private final double amount;

        TransferTask(String fromAccountNum, String toAccountNum, double amount) {
            this.fromAccountNum = fromAccountNum;
            this.toAccountNum = toAccountNum;
            this.amount = amount;
        }

        @Override
        public void run() {
            try {
                TransactionWrapper transaction = rest.transferMoney(fromAccountNum, toAccountNum, amount);
                System.out.println("Transferred: " + transaction);
            } catch (Exception e) {
                System.out.println("Not transferred: " + e);
            } finally {
                latchOnTransfer.countDown();
            }
        }
    }

    private class GetTask implements Runnable {
        private final String number;

        GetTask(String number) {
            this.number = number;
        }

        @Override
        public void run() {
            try {
                AccountWrapper account = rest.getAccount(number);
                System.out.println("Got: " + account);
                totalFiniteAmount.addAndGet((long) account.getAmount());
            } catch (Exception e) {
                System.out.println("Not got: " + e);
            } finally {
                latchOnGet.countDown();
            }
        }
    }

    private class DeleteTask implements Runnable {
        private final String number;

        DeleteTask(String number) {
            this.number = number;
        }

        @Override
        public void run() {
            try {
                AccountWrapper account = rest.deleteAccount(number);
                System.out.println("Deleted: " + account);
            } catch (Exception e) {
                System.out.println("Not deleted: " + e);
            } finally {
                latchOnDelete.countDown();
            }
        }
    }
}
