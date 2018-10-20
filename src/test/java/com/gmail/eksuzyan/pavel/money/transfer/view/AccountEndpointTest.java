package com.gmail.eksuzyan.pavel.money.transfer.view;

import org.junit.Test;

/**
 * @author Pavel Eksuzian.
 * Created: 20.10.2018.
 */
public class AccountEndpointTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeNull() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.createAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeEmpty() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.createAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.createAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNegative() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.createAccount("a111aa", -1.0);
    }

    @Test
    public void testCreateAccountPositive() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.createAccount("a111aa", 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeNull() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.getAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeEmpty() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.getAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.getAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeNull() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.deleteAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeEmpty() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.deleteAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotContainSpacesOnly() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.deleteAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeNull() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney(null, "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeEmpty() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotContainSpacesOnly() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("     ", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeNull() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeEmpty() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", "", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotContainSpacesOnly() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", "     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsAccountNumsAreTheSame() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", "a111aa", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNegative() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", "b222bb", -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeZero() {
        AccountEndpoint endpoint = new AccountEndpoint();

        endpoint.transferMoney("a111aa", "b222bb", 0.0);
    }
}
