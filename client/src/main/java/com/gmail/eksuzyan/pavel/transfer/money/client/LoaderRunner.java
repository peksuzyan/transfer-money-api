package com.gmail.eksuzyan.pavel.transfer.money.client;

import com.gmail.eksuzyan.pavel.transfer.money.client.connector.HttpConnector;
import com.gmail.eksuzyan.pavel.transfer.money.util.cfg.RestProperties;
import com.gmail.eksuzyan.pavel.transfer.money.util.media.json.acc.AccountWrapper;
import com.gmail.eksuzyan.pavel.transfer.money.util.media.json.tx.TransactionWrapper;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

/**
 * Describes a unit of simulation work. Cannot be used more than once.
 * <p>
 * Not thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
class LoaderRunner implements AutoCloseable {

    private static final String HEADLINE_PATTERN = "============== Requesting '%s' ==============";
    private static final Format ACC_NUM_FORMATTER = new DecimalFormat("ACC-0000");

    private static final int MAX_INITIAL_AMOUNT = 100;
    private static final int MAX_TRANSFER_AMOUNT = 25;

    private final AtomicLong totalInitialAmount = new AtomicLong();
    private final AtomicLong totalFiniteAmount = new AtomicLong();

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final int accountsCount;
    private final int transfersCount;

    private final HttpConnector connector;

    private final CountDownLatch latchOnCreate;
    private final CountDownLatch latchOnTransfer;
    private final CountDownLatch latchOnGet;
    private final CountDownLatch latchOnDelete;

    private final Random randomizer;

    /**
     * Creates an instance with provided connection properties, accounts count and transfers count.
     * Initial state of pseudo-random generator is defined by inner implementation of {@link Random#Random()}.
     * Pseudo-random generator is used wherever random generated value is necessarily.
     *
     * @param props          connection properties
     * @param accountsCount  accounts count
     * @param transfersCount transfers count
     * @throws IllegalArgumentException if server port < 1000, accounts count < 2 or transfers count < 1
     */
    LoaderRunner(RestProperties props, int accountsCount, int transfersCount) {
        this(props, accountsCount, transfersCount, null);
    }

    /**
     * Creates an instance with provided connection properties, accounts count, transfers count and
     * initial state of pseudo-random generator.
     * Pseudo-random generator is used wherever random generated value is necessarily.
     *
     * @param props               connection properties
     * @param accountsCount       accounts count
     * @param transfersCount      transfers count
     * @param randomizerInitState initial state of pseudo-random generator
     * @throws IllegalArgumentException if server port < 1000, accounts count < 2 or transfers count < 1
     */
    LoaderRunner(RestProperties props, int accountsCount, int transfersCount, long randomizerInitState) {
        this(props, accountsCount, transfersCount, new Random(randomizerInitState));
    }

    private LoaderRunner(RestProperties props, int accountsCount, int transfersCount, Random randomizer) {
        if (props.getServerPort() < 1_000)
            throw new IllegalArgumentException("Server port is less than one thousand. ");
        if (accountsCount < 2)
            throw new IllegalArgumentException("Accounts count is less than two. ");
        if (transfersCount < 1)
            throw new IllegalArgumentException("Transfers count is less than one. ");

        this.accountsCount = accountsCount;
        this.transfersCount = transfersCount;

        this.connector = new HttpConnector(props);

        this.latchOnCreate = new CountDownLatch(accountsCount);
        this.latchOnTransfer = new CountDownLatch(transfersCount);
        this.latchOnGet = new CountDownLatch(accountsCount);
        this.latchOnDelete = new CountDownLatch(accountsCount);

        this.randomizer = nonNull(randomizer) ? randomizer : new Random();
    }

    /**
     * Starts simulation with parameters configured during runner construction.
     *
     * @throws InterruptedException if main thread is interrupted while waiting for previous step completion
     */
    void start() throws InterruptedException {
        List<String> accountNums = generateAccountNums();

        requestCreate(accountNums);
        requestTransfer(accountNums);
        requestGet(accountNums);
        requestDelete(accountNums);

        System.out.println("Total initial amount: " + totalInitialAmount.get());
        System.out.println("Total finite amount: " + totalFiniteAmount.get());
    }

    private List<String> generateAccountNums() {
        return IntStream
                .rangeClosed(1, accountsCount)
                .mapToObj(ACC_NUM_FORMATTER::format)
                .collect(Collectors.toList());
    }

    private void requestCreate(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "CREATE"));

        for (String accountNum : accountNums) {
            threadPool.execute(new CreateTask(accountNum, generateInitialAmount()));
        }

        latchOnCreate.await();
        System.out.println();
    }

    private void requestTransfer(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "TRANSFER"));

        final TransferTaskFactory transferTaskFactory = new TransferTaskFactory(accountNums);
        for (int i = 0; i < transfersCount; i++) {
            threadPool.execute(transferTaskFactory.createNew());
        }

        latchOnTransfer.await();
        System.out.println();
    }

    private void requestGet(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "GET"));

        for (String accountNum : accountNums) {
            threadPool.execute(new GetTask(accountNum));
        }

        latchOnGet.await();
        System.out.println();
    }

    private void requestDelete(List<String> accountNums) throws InterruptedException {
        System.out.println(format(HEADLINE_PATTERN, "DELETE"));

        for (String accountNum : accountNums) {
            threadPool.execute(new DeleteTask(accountNum));
        }

        latchOnDelete.await();
        System.out.println();
    }

    private int generateInitialAmount() {
        return randomizer.nextInt(MAX_INITIAL_AMOUNT);
    }

    private int generateTransferAmount() {
        return randomizer.nextInt(MAX_TRANSFER_AMOUNT) + 1;
    }

    /**
     * Releases outer resources acquired before or during execution.
     *
     * @throws IllegalStateException if thread pool could not terminate in defined time
     * @throws InterruptedException  if main thread interrupted while waiting for thread pool termination
     */
    @Override
    public void close() throws InterruptedException {
        threadPool.shutdown();
        if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
            List<Runnable> cancelledTasks = threadPool.shutdownNow();
            System.out.println("There were " + cancelledTasks.size() + " cancelled tasks. ");
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Application could not complete in time. ");
            }
        }
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
            return randomizer.nextInt(maxIndex);
        }
    }

    private class CreateTask implements Runnable {
        private final String accNum;
        private final double accAmount;

        CreateTask(String accNum, double accAmount) {
            this.accNum = accNum;
            this.accAmount = accAmount;
        }

        @Override
        public void run() {
            AccountWrapper acc = new AccountWrapper(accNum, accAmount);
            try {
                connector.deliverCreateAccountReq(acc);

                totalInitialAmount.addAndGet(acc.getAmount().longValue());

                System.out.println("Created: " + acc);
            } catch (Exception e) {
                System.out.println("Not created: " + e);
            } finally {
                latchOnCreate.countDown();
            }
        }
    }

    private class TransferTask implements Runnable {
        private final String srcNum;
        private final String destNum;
        private final double amount;

        TransferTask(String srcNum, String destNum, double amount) {
            this.srcNum = srcNum;
            this.destNum = destNum;
            this.amount = amount;
        }

        @Override
        public void run() {
            TransactionWrapper tx = new TransactionWrapper(srcNum, destNum, amount);
            try {
                connector.deliverTransferMoneyReq(tx);

                System.out.println("Transferred: " + tx);
            } catch (Exception e) {
                System.out.println("Not transferred: " + e);
            } finally {
                latchOnTransfer.countDown();
            }
        }
    }

    private class GetTask implements Runnable {
        private final String accNum;

        GetTask(String accNum) {
            this.accNum = accNum;
        }

        @Override
        public void run() {
            try {
                AccountWrapper acc = connector.deliverGetAccountReq(accNum);

                totalFiniteAmount.addAndGet(acc.getAmount().longValue());

                System.out.println("Got: " + acc);
            } catch (Exception e) {
                System.out.println("Not got: " + e);
            } finally {
                latchOnGet.countDown();
            }
        }
    }

    private class DeleteTask implements Runnable {
        private final String accNum;

        DeleteTask(String accNum) {
            this.accNum = accNum;
        }

        @Override
        public void run() {
            try {
                AccountWrapper acc = connector.deliverDeleteAccountReq(accNum);

                System.out.println("Deleted: " + acc);
            } catch (Exception e) {
                System.out.println("Not deleted: " + e);
            } finally {
                latchOnDelete.countDown();
            }
        }
    }
}
