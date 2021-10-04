package com.flowlinx.fix.server.exception;

public class PreconditionalFailedException extends RuntimeException{

    public PreconditionalFailedException(String message) {
        super(message);
    }

    public PreconditionalFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
