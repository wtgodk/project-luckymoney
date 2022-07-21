package com.godk.luckymoney.schedule;

import cc.godk.leisure.timewheel.Schedule;
import cc.godk.leisure.timewheel.Worker;
import com.godk.luckymoney.InvalidListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;

/**
 * @author godk_
 * @class ScheduleHandler.java
 * @description 红包失效监听实现(定时任务)
 * @createTime 2022年07月21日 22:09:00
 */
@Slf4j
public class ScheduleHandler  implements InvalidListener {


    /**
     *  时间轮 初始化
     *   // 该jar包不存在未上传
     */
    private final Schedule timer = new Schedule(1, 10, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2));


    @Override
    public void addTask(String luckyMoneyId,long expireDate, Runnable runnable) {
        log.info("[addTask] 红包失效任务添加[luckyMoneyId,expireDate,runnable]->[{},{},{}]",luckyMoneyId,expireDate,runnable);
        timer.addTask(new Worker(luckyMoneyId,expireDate,runnable));
        log.info("[addTask] 红包失效任务添加[完成");

    }
}
