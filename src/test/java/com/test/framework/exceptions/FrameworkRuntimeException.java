package com.test.framework.exceptions;

public class FrameworkRuntimeException extends RuntimeException {
    public FrameworkRuntimeException() {
    }

    public FrameworkRuntimeException(String message) {
        super(message);
    }

    public FrameworkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkRuntimeException(Throwable cause) {
        super(cause);
    }
}
