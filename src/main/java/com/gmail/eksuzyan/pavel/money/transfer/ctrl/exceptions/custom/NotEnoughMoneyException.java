package com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.custom;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.model.entities.Account;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/30/2018.
 */
public class NotEnoughMoneyException extends BusinessException {

    public NotEnoughMoneyException(Account srcAcc, Account destAcc) {
        super("Not enough money to transfer from '" + srcAcc.getNumber() + "' to '" + destAcc.getNumber() + "'. ");
    }

}
