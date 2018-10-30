package com.gmail.eksuzyan.pavel.money.transfer.unit.ctrl;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.AccountService;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.AccountDatastore;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;
import com.gmail.eksuzyan.pavel.money.transfer.model.exceptions.DatastoreException;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit testing.
 *
 * @author Pavel Eksuzian.
 * Created: 20.10.2018.
 */
public class AccountServiceBusinessTest {

    @Test
    public void testCreateAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        Double initAmount = 1.0;

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
            public void createAccount(Account newAcc) throws DatastoreException {
                throw new DatastoreException("Account already exists. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";
        Double initAmount = 1.0;

        service.createAccount(num, initAmount);
    }

    @Test
    public void testGetAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        Double initAmount = 1.0;
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
            public Account getAccount(String accNum) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";

        service.getAccount(num);
    }

    @Test
    public void testGetAllAccountsPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        Double initAmount = 1.0;
        storage.put(num, new Account(num, initAmount));

        Map<String, Account> actual = service.getAllAccounts();

        assertEquals(1, actual.size());
    }

    @Test
    public void testGetAllAccountsNegative() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        Map<String, Account> actual = service.getAllAccounts();

        assertEquals(0, actual.size());
    }

    @Test
    public void testDeleteAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        Double initAmount = 1.0;
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
            public void deleteAccount(Account acc) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";

        service.deleteAccount(num);
    }

    @Test
    public void testUpdateAccountPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num = "a111aa";
        Double initAmount = 1.0;
        storage.put(num, new Account(num, initAmount));

        Double newAmount = 2.0;
        Account actual = service.updateAccount(num, newAmount);

        boolean result = Objects.equals(num, actual.getNumber())
                && Objects.equals(newAmount, actual.getAmount());

        assertTrue(result);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateAccountThrowsCouldNotUpdateAccount() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountDatastore datastore = new AccountDatastore(storage) {
            @Override
            public Account getAccount(String accNum) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num = "a111aa";

        service.updateAccount(num, 2.0);
    }

    @Test
    public void testTransferMoneyPositive() {
        ConcurrentHashMap<String, Account> storage = new ConcurrentHashMap<>();
        AccountService service = new AccountService(new AccountDatastore(storage));

        String num1 = "a111aa";
        String num2 = "b222bb";
        Double initAmount1 = 1.0;
        Double initAmount2 = 2.0;
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
            public Account getAccount(String accNum) throws DatastoreException {
                throw new DatastoreException("Account is not found. ");
            }
        };
        AccountService service = new AccountService(datastore);

        String num1 = "a111aa";
        String num2 = "b222bb";
        Double initAmount1 = 1.0;
        Double initAmount2 = 2.0;
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
        Double initAmount1 = 1.0;
        Double initAmount2 = 2.0;
        storage.put(num1, new Account(num1, initAmount1));
        storage.put(num2, new Account(num2, initAmount2));

        service.transferMoney(num1, num2, 2.0);
    }
}
