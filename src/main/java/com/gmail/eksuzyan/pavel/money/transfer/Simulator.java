package com.gmail.eksuzyan.pavel.money.transfer;

import com.gmail.eksuzyan.pavel.money.transfer.view.AccountEndpoint;
import com.gmail.eksuzyan.pavel.money.transfer.view.wrappers.AccountWrapper;

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

public class Simulator implements AutoCloseable {

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
            simulator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void start() throws InterruptedException {
        List<String> accountNums = generateTestAccounts(totalAccounts);

        requestCreate(accountNums);
        latchOnCreate.await();

        requestTransfer(accountNums);
        latchOnTransfer.await();

        requestGet(accountNums);
        latchOnGet.await();

        requestDelete(accountNums);
        latchOnDelete.await();

        System.out.println("Total initial amount: " + totalInitialAmount.get());
        System.out.println("Total finite amount: " + totalFiniteAmount.get());
    }

    private void requestCreate(List<String> accountNums) {
        for (String accountNum : accountNums) {
            threadPool.execute(new CreateTask(accountNum, generateInitialAmount()));
        }
    }

    private void requestTransfer(List<String> accountNums) {
        final TransferTaskFactory taskFactory = new TransferTaskFactory(accountNums);
        for (int i = 0; i < totalTransfers; i++) {
            threadPool.execute(taskFactory.createNew());
        }
    }

    private void requestGet(List<String> accountNums) {
        for (String accountNum : accountNums) {
            threadPool.execute(new GetTask(accountNum));
        }
    }

    private void requestDelete(List<String> accountNums) {
        for (String accountNum : accountNums) {
            threadPool.execute(new DeleteTask(accountNum));
        }
    }

    private static List<String> generateTestAccounts(int totalAccounts) {
        return IntStream
                .iterate(1, num -> num + 1)
                .limit(totalAccounts)
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
                rest.transferMoney(fromAccountNum, toAccountNum, amount);
                System.out.println("Transferred: " + toString());
            } catch (Exception e) {
                System.out.println("Not transferred: " + e);
            } finally {
                latchOnTransfer.countDown();
            }
        }

        @Override
        public String toString() {
            return "from " + fromAccountNum + " to " + toAccountNum + " on " + amount;
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
            } finally {
                latchOnDelete.countDown();
            }
        }
    }
}
