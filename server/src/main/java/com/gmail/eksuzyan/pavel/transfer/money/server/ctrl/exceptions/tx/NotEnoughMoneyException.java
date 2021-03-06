package com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.tx;

import com.gmail.eksuzyan.pavel.transfer.money.server.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.transfer.money.server.model.entities.Account;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/30/2018.
 */
public class NotEnoughMoneyException extends BusinessException {

    public NotEnoughMoneyException(Double amount, Account srcAcc, Account destAcc) {
        super("Not enough money (" + amount + ") to transfer from '" + srcAcc.getNumber() +
                "' (" + srcAcc.getAmount() + ") to '" + destAcc.getNumber() + "'. ");
    }

}
