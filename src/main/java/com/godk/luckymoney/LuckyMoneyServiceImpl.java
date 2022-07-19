package com.godk.luckymoney;

import com.alibaba.fastjson.JSON;
import com.godk.luckymoney.exception.BusyException;
import com.godk.luckymoney.exception.InvalidParameterException;
import com.godk.luckymoney.exception.LuckMoneyEmptyException;
import com.godk.luckymoney.exception.LuckMoneyNotFoundException;
import com.godk.luckymoney.generator.UniqueIdGenerator;
import com.godk.luckymoney.handler.ProcessingHandler;
import com.godk.luckymoney.storage.CacheManager;
import com.godk.luckymoney.storage.LockHandler;
import com.godk.luckymoney.storage.PersistenceManager;
import com.godk.luckymoney.vo.LuckyDog;
import com.godk.luckymoney.vo.LuckyMoney;
import com.godk.luckymoney.vo.LuckyMoneyCacheVO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 11:14
 */
@Slf4j
public class LuckyMoneyServiceImpl implements LuckyMoneyService {
    /**
     * 缓存过期 增加延迟时间 1天
     */
    private final long CACHE_DELAY_TIME = 1000 * 60 * 60 * 24;
    /**
     *  锁过期时间
     *   10分钟
     */
    private final long LOCK_EXPIRE = 1000 * 60 * 10;

    /**
     *  红包抢购锁前缀
     */
    private final String LOCK_KEY_PRE = "LUCKY_MONEY_LOCK_";
    /**
     *  锁获取自旋次数
     */
    private final int CAS_NUMBER = 10;
    /**
     * 缓存
     */
    private CacheManager<LuckyMoneyCacheVO> cacheManager;
    /**
     * 后置处理  用户抢红包时调用
     */
    private ProcessingHandler<LuckyDog> postProcessingHandler;
    /**
     * 前置处理  红包创建时使用
     */
    private ProcessingHandler<LuckyMoney> preProcessingHandler;
    /**
     * 持久化接口，存储数据
     * 1、红包信息
     * 2、红包抢完数据
     */
    private PersistenceManager persistenceManager;
    /**
     * 唯一ID生成器
     */
    private UniqueIdGenerator uniqueIdGenerator;
    /**
     *  锁
     */
    private LockHandler lockHandler;

    /**
     * 红包金额最大值
     */
    private final BigDecimal maxTotalMoney = new BigDecimal("200");
    /**
     * 最大红包个数
     */
    private final int maxLuckyMoneyNumber = 100;

    public LuckyMoneyServiceImpl() {
    }

    public LuckyMoneyServiceImpl(CacheManager<LuckyMoneyCacheVO> cacheManager, ProcessingHandler<LuckyDog> postProcessingHandler, ProcessingHandler<LuckyMoney> preProcessingHandler, PersistenceManager persistenceManager, UniqueIdGenerator uniqueIdGenerator, LockHandler lockHandler) {
        this.cacheManager = cacheManager;
        this.postProcessingHandler = postProcessingHandler;
        this.preProcessingHandler = preProcessingHandler;
        this.persistenceManager = persistenceManager;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.lockHandler = lockHandler;
    }

    @Override
    public LuckyMoney create(BigDecimal totalMoney, int totalSize, String username, long effectiveTime) {
        log.info("[CREATE] 红包创建[totalMoney,totalSize,username,effectiveTime]->[{},{},{},{}]", totalMoney, totalSize, username, effectiveTime);
        //1、参数校验
        if (totalMoney == null || totalMoney.doubleValue() <= 0 || totalMoney.compareTo(maxTotalMoney) > 0) {
            String format = String.format("红包金额超限%s:[0,%s]", totalMoney, maxTotalMoney);
            throw new InvalidParameterException(format);
        }
        if (totalSize <= 0 || totalSize > maxLuckyMoneyNumber) {
            String format = String.format("红包个数超限%s:[0,%s]", totalSize, maxLuckyMoneyNumber);
            throw new InvalidParameterException(format);
        }
        if (effectiveTime < 0) {
            throw new InvalidParameterException("未设置红包有效时间");
        }
        //2、构建红包对象
        LuckyMoney luckyMoney = new LuckyMoney();
        luckyMoney.setLuckyMoneyId(uniqueIdGenerator.genUniqueId() + "-" + username);
        luckyMoney.setCreatedBy(username);
        luckyMoney.setCreatedTime(new Date());
        luckyMoney.setFailureTime(new Date(System.currentTimeMillis() + effectiveTime));
        luckyMoney.setTotalMoney(totalMoney);
        luckyMoney.setTotalSize(totalSize);
        log.info("[CREATE] 红包对象构建结束:{}", JSON.toJSONString(luckyMoney));
        //3、持久化存储
        persistenceManager.save(luckyMoney);
        log.info("[CREATE] 红包对象持久化结束:{}", JSON.toJSONString(luckyMoney));
        //4、缓存对象构建、存储
        LuckyMoneyCacheVO luckyMoneyCacheVO = new LuckyMoneyCacheVO(luckyMoney);
        log.info("[CREATE] 红包缓存对象结束:{}", JSON.toJSONString(luckyMoneyCacheVO));
        long realCacheEffectiveTime = effectiveTime + CACHE_DELAY_TIME;
        log.info("[CREATE] 红包缓存入参:[key,value,effectiveTime]->[{},{},{}]", luckyMoney.getLuckyMoneyId(), luckyMoneyCacheVO, realCacheEffectiveTime);
        cacheManager.put(luckyMoney.getLuckyMoneyId(), luckyMoneyCacheVO, realCacheEffectiveTime);
        log.info("[CREATE] 红包创建完成,返回对象:{}", JSON.toJSONString(luckyMoney));
        return luckyMoney;
    }

    @Override
    public boolean exist(String id,String username) {
        log.info("[exist] 检验红包是否有效(抢光/过期/抢过):[id,username]->[{},{}]", id);
        //1、 红包是否过期
        LuckyMoneyCacheVO luckyMoneyCacheVO = cacheManager.get(id);
        if (luckyMoneyCacheVO == null) {
            log.info("[exist] 红包已过期失效:[缓存已清除]");
            return false;
        }
        Date failureTime = luckyMoneyCacheVO.getLuckyMoney().getFailureTime();
        if (failureTime.getTime() < System.currentTimeMillis()) {
            log.info("[exist] 红包已过期失效[超过有效时间]");
            return false;
        }
        //2、 红包是否有剩余
        int lastSize = luckyMoneyCacheVO.getLastSize();
        if (lastSize <= 0) {
            log.info("[exist] 红包已被抢光");
            return false;
        }
        LuckyDog luckyDog = luckyMoneyCacheVO.getLuckyMoney().repeatCheck(username);
        if (luckyDog != null) {
            log.error("[get] 用户已经抢过:{}", JSON.toJSONString(luckyDog));
            return false;
        }
        return true;
    }

    @Override
    public LuckyMoney result(String id) {
        log.info("[result] 查询抢红包结果列表:{}", id);
        LuckyMoneyCacheVO luckyMoneyCacheVO = cacheManager.get(id);
        log.info("[result] 缓存查询抢红包结果列表结果:{}", luckyMoneyCacheVO);
        if (luckyMoneyCacheVO == null) {
            log.info("[result] 缓存红包已过期,使用持久化数据");
            luckyMoneyCacheVO = getFromDB(id);
            //填充cache,过期时间1天
            cacheManager.put(id, luckyMoneyCacheVO, CACHE_DELAY_TIME);
        }
        LuckyMoney luckyMoney = luckyMoneyCacheVO.getLuckyMoney();
        int lastSize = luckyMoneyCacheVO.getLastSize();
        if (lastSize == 0) {
            //红包已经抢完,配置手气最佳
            luckyMoney.bestLucky();
        }
        log.info("[result] 获取抢红包结果列表：{}", JSON.toJSONString(luckyMoney));
        return luckyMoney;
    }


    @Override
    public LuckyDog get(String id, String username) {
        log.info("[get] 抢红包:[id,username]->[{},{}]", id, username);
        String uuid = UUID.randomUUID().toString().substring(32);
        String key  = LOCK_KEY_PRE + id;
        try {
            //1、CAS获取锁
            boolean lock = lockHandler.lock(key, uuid, LOCK_EXPIRE);
            int count = 0;
            while(!lock && count < CAS_NUMBER){
                count++;
                lock =   lockHandler.lock(key,uuid,LOCK_EXPIRE);
            }
            if(!lock){
                throw new BusyException("有序排队哈~");
            }
            //2、确保红包还可抢
            boolean exist = exist(id,username);
            if (!exist) {
                log.warn("[get] 红包已经失效:{}", id);
                //红包已经失效
                throw new LuckMoneyEmptyException("红包已经被抢光了~");
            }
            LuckyMoneyCacheVO luckyMoneyCacheVO = cacheManager.get(id);
            //3、 此人是否已经抢过
            LuckyMoney luckyMoney = luckyMoneyCacheVO.getLuckyMoney();
            //4、抢红包
            BigDecimal lottery = lottery(luckyMoneyCacheVO);
            //5、红包列表更新
            LuckyDog dog = luckyMoney.luckly(lottery, username, null);
            //6、 持久化处理
            persistenceManager.update(luckyMoney);
            persistenceManager.save(dog);
            //7、缓存更新,缓存过期时间点不变
            long time = luckyMoneyCacheVO.getLuckyMoney().getCreatedTime().getTime();
            long currentTimeMillis = System.currentTimeMillis();
            cacheManager.put(id, luckyMoneyCacheVO, time + CACHE_DELAY_TIME - (currentTimeMillis - time));
            //8、 后置处理
            postProcessingHandler.handle(dog);
            return dog;
        } finally {
            //9、锁释放
            String value = lockHandler.get(key);
            if(value!=null && value.equals(uuid)){
                lockHandler.unlock(key);
            }
        }
    }


    /**
     * 生成红包金额
     *
     * @param luckyMoney
     * @return
     */
    private BigDecimal lottery(LuckyMoneyCacheVO luckyMoney) {
        int lastSize = luckyMoney.getLastSize();
        BigDecimal lastMoney = luckyMoney.getLastMoney();
        if (lastSize == 1) {
            luckyMoney.subtract(lastMoney);
            return lastMoney;
        }
        Random r = new Random();
        // 最低0.01
        BigDecimal min = new BigDecimal("0.01");
        BigDecimal max = lastMoney.divide(new BigDecimal(lastSize + ""), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(2));
        BigDecimal money = new BigDecimal(r.nextDouble() + "").multiply(max);
        money = money.setScale(2,RoundingMode.HALF_UP);
        money = money.compareTo(min) <= 0 ? min : money;
        luckyMoney.subtract(money);
        return money;
    }

    /**
     * 从持久化获取红包数据
     *
     * @param id 红包ID
     * @return
     */
    private LuckyMoneyCacheVO getFromDB(String id) {
        log.info("[getFromDB] 持久化获取红包:{}", id);
        //TODO 从数据库获取
        String format = String.format("红包未找到[%s]", id);
        throw new LuckMoneyNotFoundException(format);
    }
}
