package com.godk.luckymoney.exception;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 14:34
 */
public class InvalidParameterException extends RuntimeException{

    public InvalidParameterException(String message) {
        super(message);
    }
}
