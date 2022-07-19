package com.godk.luckymoney.storage;

import com.godk.luckymoney.vo.LuckyMoney;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:20
 */
public interface PersistenceManager {


    //TODO  完善
    void save(Object luckyMoney);


    void update(Object luckyMoney);




}
