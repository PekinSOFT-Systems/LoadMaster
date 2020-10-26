package com.pekinsoft.loadmaster.err;

public class ValidationException extends Exception {
    
    /** Creates a new instance of ValidacaoException */
    public ValidationException(String msg) {
        this(msg, null);
    }
    
    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
