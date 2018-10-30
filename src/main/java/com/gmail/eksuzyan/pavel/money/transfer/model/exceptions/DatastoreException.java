package com.gmail.eksuzyan.pavel.money.transfer.model.exceptions;

/**
 * Datastore exception is responsible for errors while working with datastore.
 *
 * @author Pavel Eksuzian.
 *         Created: 10/17/2018.
 */
public class DatastoreException extends Exception {

    /**
     * Constructor with message param.
     *
     * @param message description
     */
    public DatastoreException(String message) {
        this(message, null);
    }

    /**
     * Constructor with message and cause params.
     *
     * @param message description
     * @param cause   reason or null if there's no
     */
    @SuppressWarnings("WeakerAccess")
    public DatastoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
