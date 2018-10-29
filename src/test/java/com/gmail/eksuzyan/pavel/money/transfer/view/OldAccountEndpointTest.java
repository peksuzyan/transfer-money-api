package com.gmail.eksuzyan.pavel.money.transfer.view;

import org.junit.Test;

/**
 * @author Pavel Eksuzian.
 *         Created: 20.10.2018.
 */
public class OldAccountEndpointTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeNull() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.createAccount(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotBeEmpty() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.createAccount("", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountNumCannotContainSpacesOnly() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.createAccount("     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountThrowsAccountInitAmountIsNegative() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.createAccount("a111aa", -1.0);
    }

    @Test
    public void testCreateAccountPositive() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.createAccount("a111aa", 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeNull() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.getAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotBeEmpty() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.getAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountThrowsAccountNumCannotContainSpacesOnly() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.getAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeNull() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.deleteAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotBeEmpty() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.deleteAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAccountThrowsAccountNumCannotContainSpacesOnly() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.deleteAccount("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeNull() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney(null, "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotBeEmpty() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsFirstAccountNumCannotContainSpacesOnly() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("     ", "b222bb", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeNull() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotBeEmpty() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", "", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsSecondAccountNumCannotContainSpacesOnly() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", "     ", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsAccountNumsAreTheSame() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", "a111aa", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeNegative() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", "b222bb", -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferMoneyThrowsTransferAmountCannotBeZero() {
        OldAccountEndpoint endpoint = new OldAccountEndpoint();

        endpoint.transferMoney("a111aa", "b222bb", 0.0);
    }
}
