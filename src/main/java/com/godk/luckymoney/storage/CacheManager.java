package com.godk.luckymoney.storage;


/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:15
 */
public interface CacheManager<T> {


    /**
     * 增加/更新 带有过期时间
     *
     * @param key           key
     * @param value         value
     * @param effectiveTime 有效时间(毫秒)
     * @return
     */
    T put(String key, T value, long effectiveTime);

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    T get(String key);


}
