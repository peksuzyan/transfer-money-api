package com.gmail.eksuzyan.pavel.money.transfer.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account.transferAmount;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Provides ways to manipulate user accounts from the business point of view.
 * <p>
 * Unconditionally thread-safe.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class AccountService {

    /**
     * Underlying datastore.
     */
    private final AccountDatastore datastore;

    /**
     * Main constructor to build up service with passed datastore.
     *
     * @param datastore datastore
     * @throws NullPointerException if datastore is null
     */
    @Inject
    public AccountService(AccountDatastore datastore) {
        this.datastore = requireNonNull(datastore);
    }

    /**
     * Creates user account.
     *
     * @param accNum     user account number
     * @param initAmount user account initial amount
     * @throws IllegalArgumentException if account validation fails
     * @throws BusinessException        if user account cannot be created
     */
    public void createAccount(String accNum, Double initAmount) {
        if (accNum == null || accNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (initAmount == null || initAmount < 0)
            throw new IllegalArgumentException("Initial amount cannot be null or negative. ");

        Account account = new Account(accNum, initAmount);

        try {
            datastore.createAccount(account);
        } catch (DatastoreException e) {
            throw new BusinessException("Could not create an account '" + accNum + "'. ", e);
        }
    }

    /**
     * Gets user account by its number.
     *
     * @param accNum user account number
     * @return user account
     * @throws IllegalArgumentException if account number validation fails
     * @throws BusinessException        if user account cannot be got
     */
    public Account getAccount(String accNum) {
        if (accNum == null || accNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        try {
            return datastore.getAccount(accNum);
        } catch (DatastoreException e) {
            throw new BusinessException("Could not get an account '" + accNum + "'. ", e);
        }
    }

    /**
     * Gets all user accounts.
     *
     * @return user accounts as a map
     */
    public Map<String, Account> getAllAccounts() {
        return datastore.getAllAccounts().stream()
                .collect(toMap(Account::getNumber, identity()));
    }

    /**
     * Updates user account amount to newer value.
     *
     * @param accNum    user account number
     * @param newAmount new amount
     * @return user account
     * @throws IllegalArgumentException if account validation fails
     * @throws BusinessException        if user account cannot be updated
     */
    public Account updateAccount(String accNum, Double newAmount) {
        if (accNum == null || accNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (newAmount == null)
            throw new IllegalArgumentException("Account amount cannot be null. ");

        try {
            Account acc = datastore.getAccount(accNum);

            acc.setAmount(newAmount);

            return acc;
        } catch (DatastoreException e) {
            throw new BusinessException("Could not update an account '" + accNum + "'. ", e);
        }
    }

    /**
     * Deletes user account.
     *
     * @param accNum user account number
     * @return user account
     * @throws IllegalArgumentException if account number validation fails
     * @throws BusinessException        if user account cannot be deleted
     */
    public Account deleteAccount(String accNum) {
        if (accNum == null || accNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        try {
            Account acc = datastore.getAccount(accNum);

            datastore.deleteAccount(acc);

            return acc;
        } catch (DatastoreException e) {
            throw new BusinessException("Could not delete an account '" + accNum + "'. ", e);
        }
    }

    /**
     * Transfers amount of money between user accounts.
     *
     * @param srcNum  user account number where amount is withdrawn from
     * @param destNum user account number where amount is deposited in
     * @param amount  amount of money to transfer
     * @throws IllegalArgumentException if transaction validation fails
     * @throws BusinessException        if transfer cannot be performed
     */
    public List<Account> transferMoney(String srcNum, String destNum, Double amount) {
        if (srcNum == null || srcNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (destNum == null || destNum.trim().isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty. ");

        if (Objects.equals(srcNum, destNum))
            throw new IllegalArgumentException("Account numbers are the same. ");

        if (amount == null || amount <= 0)
            throw new IllegalArgumentException("Transfer amount cannot be negative or zero. ");

        Account srcAcc, destAcc;
        try {
            srcAcc = datastore.getAccount(srcNum);
            destAcc = datastore.getAccount(destNum);
        } catch (DatastoreException e) {
            throw new BusinessException("Could not transfer money from '" + srcNum + "' to '" + destNum + "'. ", e);
        }

        transferAmount(srcAcc, destAcc, amount);

        return Stream.of(srcAcc, destAcc).collect(toList());
    }


}
