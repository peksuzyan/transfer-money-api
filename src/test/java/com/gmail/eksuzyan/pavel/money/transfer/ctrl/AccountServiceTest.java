package com.gmail.eksuzyan.pavel.money.transfer.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;

/**
 * @author Pavel Eksuzian.
 * Created: 20.10.2018.
 */
public class AccountServiceTest {

    @Test
    public void testCreateAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        double initAmount = 1.0;

        service.createAccount(num, initAmount);

        Account expected = storage.get(num);
        boolean actual = Objects.equals(num, expected.getNumber())
                && Objects.equals(initAmount, expected.getAmount());

        assertTrue(actual);
    }

    @Test(expected = BusinessException.class)
    public void testCreateAccountThrowsCouldNotCreateAccount() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage) {
            @Override
            public void createAccount(Account newAccount) throws DatastoreException {
                throw new DatastoreException("Account already exists. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";
        double initAmount = 1.0;

        service.createAccount(num, initAmount);
    }

    @Test
    public void testGetAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        double initAmount = 1.0;
        storage.put(num, new Account(num, initAmount));

        Account actual = service.getAccount(num);

        boolean result = Objects.equals(num, actual.getNumber())
                && Objects.equals(initAmount, actual.getAmount());

        assertTrue(result);
    }

    @Test(expected = BusinessException.class)
    public void testGetAccountThrowsCouldNotGetAccount() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage) {
            @Override
            public Account getAccount(String accountNum) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";

        service.getAccount(num);
    }

    @Test
    public void testDeleteAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        double initAmount = 1.0;
        storage.put(num, new Account(num, initAmount));

        Account actual = service.deleteAccount(num);

        boolean result = Objects.equals(num, actual.getNumber())
                && Objects.equals(initAmount, actual.getAmount());

        assertTrue(result);
    }

    @Test(expected = BusinessException.class)
    public void testDeleteAccountThrowsCouldNotDeleteAccount() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage) {
            @Override
            public void deleteAccount(Account account) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";

        service.deleteAccount(num);
    }

    @Test
    public void testTransferMoneyPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num1 = "a111aa";
        String num2 = "b222bb";
        double initAmount1 = 1.0;
        double initAmount2 = 2.0;
        storage.put(num1, new Account(num1, initAmount1));
        storage.put(num2, new Account(num2, initAmount2));

        service.transferMoney(num1, num2, 1.0);

        Account account1 = storage.get(num1);
        Account account2 = storage.get(num2);

        boolean result = account1.getAmount() == 0.0
                && account2.getAmount() == 3.0;

        assertTrue(result);
    }

    @Test(expected = BusinessException.class)
    public void testTransferMoneyThrowsCouldNotFoundAccount() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage) {
            @Override
            public Account getAccount(String accountNum) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num1 = "a111aa";
        String num2 = "b222bb";
        double initAmount1 = 1.0;
        double initAmount2 = 2.0;
        storage.put(num1, new Account(num1, initAmount1));
        storage.put(num2, new Account(num2, initAmount2));

        service.transferMoney(num1, num2, 1.0);
    }

    @Test(expected = BusinessException.class)
    public void testTransferMoneyThrowsNotEnoughMoney() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num1 = "a111aa";
        String num2 = "b222bb";
        double initAmount1 = 1.0;
        double initAmount2 = 2.0;
        storage.put(num1, new Account(num1, initAmount1));
        storage.put(num2, new Account(num2, initAmount2));

        service.transferMoney(num1, num2, 2.0);
    }
}
