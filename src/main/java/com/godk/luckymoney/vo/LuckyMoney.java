package com.godk.luckymoney.vo;

import com.godk.luckymoney.vo.LuckyDog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author weitao47
 * @project project-luckymoney
 * @date 2022/7/18 10:31
 */
public class LuckyMoney {

    /**
     *  红包ID
     */
    private String luckyMoneyId;
    /**
     *  总金额
     */
    private BigDecimal totalMoney;
    /**
     * 红包个数  >0
     */
    private int totalSize;
    /**
     *  失效时间
     */
    private Date failureTime;
    /**
     *  创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     *  已抢红包列表
     */
    private List<LuckyDog> luckDogs = new ArrayList<>();


    public LuckyDog luckly(BigDecimal money,String username,String nickName){
        LuckyDog  luckyDog = new LuckyDog();
        luckyDog.setGetTime(new Date());
        luckyDog.setMoney(money);
        luckyDog.setNickName(nickName);
        luckyDog.setUsername(username);
        luckyDog.setFromUsername(this.createdBy);
        luckDogs.add(luckyDog);
        return luckyDog;
    }

    /**
     *  手气最佳处理
     */
    public void bestLucky(){
        BigDecimal maxVal = new BigDecimal("-1");
        LuckyDog tmp = null;
        for(int i= luckDogs.size()-1;i>=0;i--){
            LuckyDog luckyDog = luckDogs.get(i);
            BigDecimal money = luckyDog.getMoney();
            if(money.compareTo(maxVal) >=0){
                tmp = luckyDog;
                maxVal = luckyDog.getMoney();
            }
        }
        if(tmp==null){
            //理论上不会出现该异常
            throw new RuntimeException();
        }
        tmp.setBestLucky(true);
    }

    /**
     *  重复抢红包校验
     * @param username
     * @return
     */
    public LuckyDog repeatCheck(String username){
        if(luckDogs.size() ==0){
            return null;
        }
        for (LuckyDog luckDog : luckDogs) {
            if(username.equals(luckDog.getUsername())){
                return luckDog;
            }
        }
        return null;
    }

    public String getLuckyMoneyId() {
        return luckyMoneyId;
    }

    public void setLuckyMoneyId(String luckyMoneyId) {
        this.luckyMoneyId = luckyMoneyId;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<LuckyDog> getLuckDogs() {
        return luckDogs;
    }

    public void setLuckDogs(List<LuckyDog> luckDogs) {
        this.luckDogs = luckDogs;
    }
}
