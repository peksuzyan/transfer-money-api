package com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.acc;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/30/2018.
 */
public class AccountExistsException  extends BusinessException {

    public AccountExistsException(Account acc, Exception cause) {
        super("Account '" + acc.getNumber() + "' already exists. ", cause);
    }

}
