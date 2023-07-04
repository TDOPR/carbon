package com.carbon.job;

import com.carbon.condition.TRXEnableCondition;
import com.carbon.manager.Manager;
import com.carbon.utils.TaskRedisCheckKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 批量生成以太坊地址放入地址池
 */
@Component
@Slf4j
@EnableScheduling
@Conditional(TRXEnableCondition.class)
public class ManagerJob {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Manager ahtManager;

    @Scheduled(cron = "0/1 * * * * ?")
    public void analyzeEvent() {
        try {
            boolean bool = redisTemplate.opsForValue().setIfAbsent(TaskRedisCheckKey.AHT_ANALYZE_EVENT, "", Duration.ofSeconds(300));
            if (bool) {
                ahtManager.analyzeEvent();
                redisTemplate.delete(TaskRedisCheckKey.AHT_ANALYZE_EVENT);
            } else {
                log.error(log.getName() + "同步事务失败，上一任务正在运行!");
            }
        } catch (Exception e) {
            log.error("同步事务失败,失败信息【{}】", e);
            redisTemplate.delete(TaskRedisCheckKey.AHT_ANALYZE_EVENT);
            e.printStackTrace();
        }
    }

}
