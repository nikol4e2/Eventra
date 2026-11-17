package com.nikola.eventra.exceptions;

public class QrQenerationException extends EventTicketException{

    public QrQenerationException() {
    }

    public QrQenerationException(String message) {
        super(message);
    }

    public QrQenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public QrQenerationException(Throwable cause) {
        super(cause);
    }

    public QrQenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
