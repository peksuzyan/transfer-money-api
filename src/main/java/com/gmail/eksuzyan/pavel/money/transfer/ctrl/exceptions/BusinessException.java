package com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        this(message, null);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
