package com.gmail.eksuzyan.pavel.money.transfer.unit.ctrl;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.service.SensibleAccountService;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.datastore.MemoryAccountDatastore;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Unit testing.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/29/2018.
 */
public class SensibleAccountServiceValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNegative() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", -1.0);
    }

    @Test
    public void testCreateAccountPositive() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.createAccount("a111aa", 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.getAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAccountThrowsAccountNewAmountIsNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.updateAccount("a111aa", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.deleteAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney(null, "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("     ", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeEmpty() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotContainSpacesOnly() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsAccountNumsAreTheSame() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "a111aa", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNull() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNegative() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeZero() {
        SensibleAccountService accService = new SensibleAccountService(new MemoryAccountDatastore(new ConcurrentHashMap<>()));

        accService.transferMoney("a111aa", "b222bb", 0.0);
    }

}
