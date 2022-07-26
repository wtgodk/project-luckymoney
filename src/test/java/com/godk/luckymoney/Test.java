package com.godk.luckymoney;

import com.alibaba.fastjson.JSON;
import com.godk.luckymoney.generator.DefaultUniqueIdGenerator;
import com.godk.luckymoney.handler.ExpireProcessingHandler;
import com.godk.luckymoney.handler.PostProcessingHandler;
import com.godk.luckymoney.handler.PreProcessingHandler;
import com.godk.luckymoney.schedule.ScheduleHandler;
import com.godk.luckymoney.storage.CacheManager;
import com.godk.luckymoney.storage.LockHandler;
import com.godk.luckymoney.storage.PersistenceManager;
import com.godk.luckymoney.vo.LuckyDog;
import com.godk.luckymoney.vo.LuckyMoney;
import com.godk.luckymoney.vo.LuckyMoneyCacheVO;
import com.godk.luckymoney.vo.LuckyMoneyVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author godk_
 * @class Test.java
 * @description test case
 * @createTime 2022年07月19日 22:00:00
 */
public class Test {

    private LuckyMoneyService luckyMoneyService;

    public static void main(String[] args) {
        Test test = new Test();
        test.init();
        //while (true){
        test.test1();

        // }
    }

    public void init() {
        CacheManager<LuckyMoneyCacheVO> cacheManager = new CacheManager<LuckyMoneyCacheVO>() {

            private Map<String, LuckyMoneyCacheVO> cache = new HashMap<>();

            @Override
            public LuckyMoneyCacheVO put(String key, LuckyMoneyCacheVO value, long effectiveTime) {
                cache.put(key, value);
                return value;
            }

            @Override
            public LuckyMoneyCacheVO get(String key) {
                return cache.get(key);
            }
        };

// 持久化  测试用例不进行持久化
        PersistenceManager persistenceManager = new PersistenceManager() {


            @Override
            public void saveLuckyMoney(LuckyMoney luckyMoney) {

            }

            @Override
            public void updateLuckMoney(LuckyMoneyVo luckyMoney) {

            }

            @Override
            public void luckMoneyExpire(String luckMoneyId) {

            }

            @Override
            public void saveLuckDog(LuckyDog luckyDog) {

            }

            @Override
            public LuckyMoneyVo get(String luckMoneyId) {
                return null;
            }
        };

        LockHandler lockHandler = new LockHandler() {
            private Map<String, String> cache = new HashMap<>();

            @Override
            public boolean lock(String key, String value, long expire) {
                String lockValue = cache.get(key);
                if (lockValue == null || lockValue.equals(value)) {
                    cache.put(key, value);
                    return true;
                }
                return false;
            }

            @Override
            public void unlock(String key) {
                cache.remove(key);
            }

            @Override
            public String get(String key) {
                return cache.get(key);
            }
        };
        luckyMoneyService = new LuckyMoneyServiceImpl(cacheManager, new PostProcessingHandler(), new PreProcessingHandler(), new ExpireProcessingHandler(),persistenceManager, new DefaultUniqueIdGenerator(), lockHandler,new ScheduleHandler());


    }

    public void test1() {
        LuckyMoney luckyMoney = luckyMoneyService.create(new BigDecimal(100), 10, "11111", 1000 * 60 * 60 * 24);
        System.out.println("红包已经生成： " + JSON.toJSONString(luckyMoney));
        String[] users = new String[]{"11111", "22222", "33333", "44444", "55555", "66666", "77777", "88888", "99999", "00000", "-----", "232323"};
        String luckyMoneyId = luckyMoney.getLuckyMoneyId();

        for (String user : users) {
            boolean exist = luckyMoneyService.exist(luckyMoneyId, user);
            if (exist) {
                LuckyDog luckyDog = luckyMoneyService.get(luckyMoneyId, user);
                if (luckyDog.getMoney().doubleValue() > 60d) {
                    System.out.println(1);
                }
            } else {
                System.out.println("红包已被抢光");
            }
        }
        LuckyMoneyVo result = luckyMoneyService.result(luckyMoneyId);
        List<LuckyDog> luckyDogs = result.getLuckyMoney().getLuckyDogs();
        BigDecimal total = new BigDecimal("0");
        for (LuckyDog luckyDog : luckyDogs) {
            BigDecimal money = luckyDog.getMoney();
            total = total.add(money);
        }
        total = total.setScale(2, RoundingMode.HALF_UP);
        System.out.println("红包结果:" + JSON.toJSONString(result.getLuckyMoney().getLuckyDogs()));
        System.out.println("红包总数:" + total.doubleValue());
    }
}
