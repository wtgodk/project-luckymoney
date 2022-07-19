package com.godk.luckymoney.vo;

import java.math.BigDecimal;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:36
 */
public class LuckyMoneyCacheVO {

    public LuckyMoneyCacheVO() {
    }

    public LuckyMoneyCacheVO(LuckyMoney luckyMoney) {
        this.luckyMoney = luckyMoney;
        this.lastMoney = luckyMoney.getTotalMoney();
        this.lastSize = luckyMoney.getTotalSize();
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

    /**
     * 金额扣减
     */
    public void subtract(BigDecimal money) {
        lastSize--;
        this.lastMoney = lastMoney.subtract(money);
    }


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
