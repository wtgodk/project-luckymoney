package com.godk.luckymoney.handler;

import com.godk.luckymoney.vo.LuckyMoney;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 14:19
 */
public class PreProcessingHandler implements ProcessingHandler<LuckyMoney>{


    @Override
    public void handle(LuckyMoney obj) {
        // pre processing when lucky money created call this method
    }
}
