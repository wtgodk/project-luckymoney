package com.godk.luckymoney.handler;

import com.godk.luckymoney.vo.LuckyMoneyCacheVO;

/**
 * @author godk_
 * @class ExpireProcessingHandler.java
 * @description 红包失效处理  退款操作
 * @createTime 2022年07月21日 22:25:00
 */
public class ExpireProcessingHandler implements ProcessingHandler<LuckyMoneyCacheVO>{

    @Override
    public void handle(LuckyMoneyCacheVO obj) {
        //TODO execute when lucky money expire and lucky money number > 0
    }
}
