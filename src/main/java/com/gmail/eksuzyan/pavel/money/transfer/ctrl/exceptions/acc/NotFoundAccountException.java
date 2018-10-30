package com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.acc;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/30/2018.
 */
public class NotFoundAccountException extends BusinessException {

    public NotFoundAccountException(String accNum, Exception cause) {
        super("Account '" + accNum + "' is not found. ", cause);
    }

}
