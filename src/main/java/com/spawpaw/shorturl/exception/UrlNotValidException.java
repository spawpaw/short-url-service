package com.spawpaw.shorturl.exception;

public class UrlNotValidException extends ServiceException {
    public UrlNotValidException(Integer code) {
        super(code);
    }

    public UrlNotValidException(Integer code, String message) {
        super(code, message);
    }

    public UrlNotValidException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UrlNotValidException(Integer code, Throwable cause) {
        super(code, cause);
    }

    public UrlNotValidException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
