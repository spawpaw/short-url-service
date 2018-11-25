package com.spawpaw.shorturl.exception;

public class ParseExceprion extends ServiceException {
    public ParseExceprion(Integer code) {
        super(code);
    }

    public ParseExceprion(Integer code, String message) {
        super(code, message);
    }

    public ParseExceprion(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ParseExceprion(Integer code, Throwable cause) {
        super(code, cause);
    }

    public ParseExceprion(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
