package com.godk.luckymoney;


/**
 * @author godk_
 * @class InvalidListener.java
 * @description 红包失效监听
 * @createTime 2022年07月20日 21:32:00
 */
public interface InvalidListener {


    /**
     *  监听添加
     *  到达失效时间调用 runnable
     * @param luckMoneyId  红包ID
     * @param expireDate  失效日期
     * @param runnable    失效后执行
     */
    void addTask(String luckMoneyId,long expireDate , Runnable runnable);



}
