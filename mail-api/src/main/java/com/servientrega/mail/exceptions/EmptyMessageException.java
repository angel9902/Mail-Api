package com.servientrega.mail.exceptions;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
public class EmptyMessageException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmptyMessageException() {
        super();
    }

    public EmptyMessageException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyMessageException(String message) {
        super(message);
    }

    public EmptyMessageException(Throwable cause) {
        super(cause);
    }

}
