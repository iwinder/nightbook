package com.windcoder.nightbook.mina.exception;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-16
 * Time: 14:31 下午
 */
public class MinaLoginServiceException extends Exception {
    private static final long serialVersionUID = 7179434716738339025L;
    private String type;

    MinaLoginServiceException(String type, String message, Exception innerException) {
        super(message, innerException);
        this.type = type;
    }

    MinaLoginServiceException(String type, String message) {
        this(type, message, null);
    }

    /**
     * 获取登录异常的类型，具体的取值可参考 Constans 里面的常量
     * @see com.windcoder.nightbook.mina.utils.Constants
     * */
    public String getType() {
        return this.type;
    }
}
