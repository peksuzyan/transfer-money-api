package com.gmail.eksuzyan.pavel.money.transfer.unit.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.unit.model.AccountDatastore;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Unit testing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/29/2018.
 */
public class AccountServiceValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNegative() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", -1.0);
    }

    @Test
    public void testCreateAccountPositive() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNewAmountIsNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("a111aa", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney(null, "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("     ", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeEmpty() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotContainSpacesOnly() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsAccountNumsAreTheSame() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "a111aa", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNull() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNegative() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeZero() {
        AccountService accService = new AccountService(new AccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", 0.0);
    }

}
