package com.carbon.service;


import com.carbon.entity.EventEntity;
import com.carbon.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 
 *
 * @date 2021-12-24 11:09:24
 */
public interface EventService extends IService<EventEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

