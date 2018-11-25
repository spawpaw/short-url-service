package com.spawpaw.shorturl.exception;

public class ErrorCode {
    public static final int OK = 0;//everything is ok, no error
    public static final int ERR_UNKNOWN = -1;
    public static final int ERR_SHORT_URL_NOT_EXIST = -2;
    public static final int ERR_SHORT_URL_CANNOT_BE_NULL = -3;
    public static final int ERR_INVALID_REQUEST_PARAM = -4;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////参数错误
    public static final int ERR_INVALID_URL = -101;
    public static final int ERR_INVALID_FTP_AUTH = -101;
    public static final int ERR_INVALID_SERVER_PORT = -102;
    public static final int ERR_INVALID_HOST_FORMAT = -103;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////禁用的功能
    public static final int ERR_CANNOT_CUSTOMIZE_SERVER_PORT = -201;
    public static final int ERR_CANNOT_USE_IP_ADDRESS_AS_HOST = -202;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////禁止的值域
    public static final int ERR_BLOCKED_PROTOCOL = -301;
    public static final int ERR_BLOCKED_TOP_LEVEL_DOMAIN = -302;
    public static final int ERR_BLOCKED_DOMAIN = -303;
}
