package com.godk.luckymoney.exception;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 17:19
 */
public class LuckMoneyNotFoundException extends RuntimeException {
    public LuckMoneyNotFoundException(String message) {
        super(message);
    }
}
