package com.godk.luckymoney.handler;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:31
 */
public interface ProcessingHandler<T> {
    /**
     *  流程处理器
     * @param obj
     */
    void  handle(T obj);

}
