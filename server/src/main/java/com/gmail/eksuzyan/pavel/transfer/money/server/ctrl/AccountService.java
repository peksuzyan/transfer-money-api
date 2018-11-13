package com.gmail.eksuzyan.pavel.transfer.money.server.ctrl;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.acc.AccountExistsException;
import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.acc.NotFoundAccountException;
import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.tx.NotEnoughMoneyException;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;

import java.util.List;
import java.util.Map;

/**
 * Describes ways to manipulate user accounts from the business point of view.
 *
 * @author Pavel Eksuzian.
 *         Created: 11/13/2018.
 */
public interface AccountService {

    /**
     * Creates user account.
     *
     * @param accNum     user account number
     * @param initAmount user account initial amount
     * @throws IllegalArgumentException if account validation fails
     * @throws AccountExistsException   if user account already exists
     */
    void createAccount(String accNum, Double initAmount);

    /**
     * Gets user account by its number.
     *
     * @param accNum user account number
     * @return user account
     * @throws IllegalArgumentException if account number validation fails
     * @throws NotFoundAccountException if user account is not found
     */
    Account getAccount(String accNum);

    /**
     * Gets all user accounts.
     *
     * @return user accounts as a map
     */
    Map<String, Account> getAllAccounts();

    /**
     * Updates user account amount to newer value.
     *
     * @param accNum    user account number
     * @param newAmount new amount
     * @return user account
     * @throws IllegalArgumentException if account validation fails
     * @throws NotFoundAccountException if user account is not found
     */
    Account updateAccount(String accNum, Double newAmount);

    /**
     * Deletes user account.
     *
     * @param accNum user account number
     * @return user account
     * @throws IllegalArgumentException if account number validation fails
     * @throws NotFoundAccountException if user account is not found
     */
    Account deleteAccount(String accNum);

    /**
     * Transfers amount of money between user accounts.
     *
     * @param srcNum  user account number where amount is withdrawn from
     * @param destNum user account number where amount is deposited in
     * @param amount  amount of money to transfer
     * @throws IllegalArgumentException if transaction validation fails
     * @throws NotFoundAccountException if any account is not found
     * @throws NotEnoughMoneyException  if source user account doesn't have enough amount
     */
    List<Account> transferMoney(String srcNum, String destNum, Double amount);

}
