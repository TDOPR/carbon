package com.carbon.service.impl;

import com.carbon.entity.EventEntity;
import com.carbon.mapper.EventDao;
import com.carbon.service.EventService;
import com.carbon.utils.PageUtils;
import com.carbon.utils.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("eventService")
public class EventServiceImpl extends ServiceImpl<EventDao, EventEntity> implements EventService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EventEntity> page = this.page(
                new Query<EventEntity>().getPage(params),
                new QueryWrapper<EventEntity>()
        );

        return new PageUtils(page);
    }

}
