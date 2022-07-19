package com.godk.luckymoney.generator;

import java.util.UUID;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 14:28
 */
public class DefaultUniqueIdGenerator implements UniqueIdGenerator {


    @Override
    public String genUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
