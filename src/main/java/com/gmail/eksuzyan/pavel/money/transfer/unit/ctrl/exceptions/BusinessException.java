package com.gmail.eksuzyan.pavel.money.transfer.unit.ctrl.exceptions;

/**
 * Business exception is responsible for business errors. Used for wrapping low-layer checked exceptions.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructor with message param.
     *
     * @param message description
     */
    public BusinessException(String message) {
        this(message, null);
    }

    /**
     * Constructor with no writable stacktrace, only message and cause are being written.
     *
     * @param message description
     * @param cause   reason or null if there's no
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause, false, false);
    }
}
