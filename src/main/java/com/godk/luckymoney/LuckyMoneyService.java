package com.godk.luckymoney;

import com.godk.luckymoney.vo.LuckyDog;
import com.godk.luckymoney.vo.LuckyMoney;

import java.math.BigDecimal;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 10:28
 */
public interface LuckyMoneyService {

    /**
     * 创建红包
     * @param totalMoney 总金额
     * @param totalSize  红包个数
     * @param username  创建人
     * @param effectiveTime 有效时间（毫秒）
     * @return
     */
     LuckyMoney create(BigDecimal totalMoney, int totalSize, String username, long effectiveTime);

    /**
     *  红包是否还有剩余、有效期内
     * @param id  红包ID
     * @see LuckyMoney#luckyMoneyId
     * @return
     */
     boolean exist(String id,String username);

    /**
     *   红包结果
     * @param id
     * @return
     */
    LuckyMoney result(String id);

    /**
     * 抢红包
     * @param id 红包ID
     * @param username 用户ID
     * @return
     */
    LuckyDog get(String id, String username);

}
