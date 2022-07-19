package com.godk.luckymoney.exception;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 18:50
 */
public class BusyException extends RuntimeException{

    public BusyException(String message) {
        super(message);
    }
}
