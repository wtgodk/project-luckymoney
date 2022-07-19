package com.godk.luckymoney.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 10:41
 */
public class LuckyDog {

    /**
     * 红包发送者用户名
     */
    private String fromUsername;

    /**
     * 用户名
     */
    private String username;

    /**
     * nickName
     */
    private String nickName;

    /**
     * 红包金额
     */
    private BigDecimal money;

    /**
     * 抢到时间
     */
    private Date getTime;
    /**
     * 手气最佳
     */
    private boolean bestLucky;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public boolean isBestLucky() {
        return bestLucky;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public void setBestLucky(boolean bestLucky) {
        this.bestLucky = bestLucky;
    }
}
