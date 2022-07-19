# lucky money function

# 红包类 : com.godk.luckymoney.LuckyMoneyServiceImpl

- [create] ： 创建红包
- [exist] : 红包是否可抢(被抢光、过期、抢过了)
- [get] :抢红包
-

[result]: 抢红包结果(已抢列表、剩余个数、金额)

# 需要自定义开发：

    持久化 红包保存(LuckyMoney)、抢红包结果保存(LuckyDog)、红包失效通知(尚未实现)实现com.godk.luckymoney.storage.PersistenceManager
    缓存  红包数据缓存      com.godk.luckymoney.storage.CacheManager
    锁   保证金额扣减原子性
    前置方法  用户创建红包时调用   com.godk.luckymoney.handler.PreProcessingHandler
    后置方法  用户抢到红包时调用   com.godk.luckymoney.handler.PostProcessingHandler

# TODO LIST

- 红包过期失效处理
- 