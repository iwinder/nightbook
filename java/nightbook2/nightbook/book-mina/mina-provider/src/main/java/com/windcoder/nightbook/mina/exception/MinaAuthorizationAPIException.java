package com.windcoder.nightbook.mina.exception;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-16
 * Time: 14:24 下午
 */
public class MinaAuthorizationAPIException  extends Exception {
    private static final long serialVersionUID = -3088657611850871775L;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MinaAuthorizationAPIException(String message, Exception inner) {
        super(message, inner);
    }

    public MinaAuthorizationAPIException(String message) {
        this(message, null);
    }
    public MinaAuthorizationAPIException(int code,String message) {
        this(message, null);
        this.code = code;
    }
}
