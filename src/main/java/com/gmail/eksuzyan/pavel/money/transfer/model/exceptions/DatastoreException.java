package com.gmail.eksuzyan.pavel.money.transfer.model.exceptions;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class DatastoreException extends Exception {

    public DatastoreException(String message) {
        super(message, null, false, false);
    }
}
