package com.godk.luckymoney.storage;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 18:43
 */
public interface LockHandler {

    /**
     * 加锁
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return
     */
    boolean lock(String key, String value, long expire);

    /**
     * 解锁
     *
     * @param key
     */
    void unlock(String key);

    /**
     * 获取锁内容
     *
     * @param key
     * @return
     */
    String get(String key);

}
