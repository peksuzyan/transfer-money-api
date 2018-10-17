package com.gmail.eksuzyan.pavel.money.transfer.model.exceptions;

public class DatastoreException extends Exception {

    public DatastoreException(String message) {
        super(message, null, true, false);
    }
}
