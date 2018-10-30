package com.gmail.eksuzyan.pavel.money.transfer.unit.model;

import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit testing.
 *
 * @author Pavel Eksuzian.
 *         Created: 20.10.2018.
 */
public class AccountDatastoreTest {

    @Test
    public void testCreateAccountPositive() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);

        datastore.createAccount(expected);

        Account actual = storage.get(num);
        assertEquals(expected, actual);
    }

    @Test(expected = DatastoreException.class)
    public void testCreateAccountThrowsAccountAlreadyExists() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);

        datastore.createAccount(expected);
        datastore.createAccount(expected);
    }

    @Test
    public void testGetAccountPositive() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);
        storage.put(num, expected);

        Account actual = datastore.getAccount(num);

        assertEquals(expected, actual);
    }

    @Test(expected = DatastoreException.class)
    public void testGetAccountThrowsAccountIsNotFound() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";

        datastore.getAccount(num);
    }

    @Test
    public void testGetAllAccountsPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);
        storage.put(num, expected);

        List<Account> actual = datastore.getAllAccounts();

        assertEquals(1, actual.size());
    }

    @Test
    public void testGetAllAccountsNegative() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        List<Account> actual = datastore.getAllAccounts();

        assertEquals(0, actual.size());
    }

    @Test
    public void testDeleteAccountPositive() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);
        storage.put(num, expected);

        datastore.deleteAccount(expected);
        Account actual = storage.get(num);

        assertNull(actual);
    }

    @Test(expected = DatastoreException.class)
    public void testDeleteAccountThrowsAccountIsNotFound() throws DatastoreException {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage);

        String num = "a111aa";
        Double initAmount = 1.0;
        Account expected = new Account(num, initAmount);

        datastore.deleteAccount(expected);
    }
}
