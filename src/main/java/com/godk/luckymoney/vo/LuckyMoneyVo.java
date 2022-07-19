package com.godk.luckymoney.vo;

import java.math.BigDecimal;

/**
 * @author godk_
 * @class LuckyMoneyVo.java
 * @description 区分于 cache对象
 * @createTime 2022年07月19日 22:51:00
 */
public class LuckyMoneyVo {
    public LuckyMoneyVo(LuckyMoney luckyMoney, BigDecimal lastMoney, int lastSize) {
        this.luckyMoney = luckyMoney;
        this.lastMoney = lastMoney;
        this.lastSize = lastSize;
    }


    public LuckyMoneyVo() {
    }

    /**
     * 原始红包对象
     */
    private LuckyMoney luckyMoney;
    /**
     * 当前余额
     */
    private BigDecimal lastMoney;
    /**
     * 剩余个数
     */
    private int lastSize;


    public LuckyMoney getLuckyMoney() {
        return luckyMoney;
    }

    public void setLuckyMoney(LuckyMoney luckyMoney) {
        this.luckyMoney = luckyMoney;
    }

    public BigDecimal getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(BigDecimal lastMoney) {
        this.lastMoney = lastMoney;
    }

    public int getLastSize() {
        return lastSize;
    }

    public void setLastSize(int lastSize) {
        this.lastSize = lastSize;
    }
}
