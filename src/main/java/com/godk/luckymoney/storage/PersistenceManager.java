package com.godk.luckymoney.storage;

import com.godk.luckymoney.vo.LuckyDog;
import com.godk.luckymoney.vo.LuckyMoney;
import com.godk.luckymoney.vo.LuckyMoneyVo;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:20
 */
public interface PersistenceManager {


    /**
     * 保存 红包
     *
     * @param luckyMoney
     */
    void saveLuckyMoney(LuckyMoney luckyMoney);

    /**
     * 更新红包结果
     * <p>
     * 更新红包抢购状态  剩余金额、已抢金额、是否抢光等等
     *
     * @param luckyMoney
     */
    void updateLuckMoney(LuckyMoneyVo luckyMoney);

    /**
     * 红包已失效
     * 红包失效时调用 即使红包未抢光也要讲红包失效
     * <p>
     * TODO 需要监听红包失效时间
     *
     * @param luckMoneyId
     */
    void luckMoneyExpire(String luckMoneyId);

    /**
     * 保存抢红包结果列表  luckMoneyId 为外键
     *
     * @param luckyDog
     */
    void saveLuckDog(LuckyDog luckyDog);

    /**
     * 获取红包信息
     * 1、红包缓存过期
     * 2、红包缓存崩溃托底
     *
     * @param luckMoneyId
     * @return
     */
    LuckyMoneyVo get(String luckMoneyId);

}
