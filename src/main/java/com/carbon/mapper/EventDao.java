package com.carbon.mapper;

import com.carbon.entity.EventEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @date 2021-12-24 11:09:24
 */
@Mapper
public interface EventDao extends BaseMapper<EventEntity> {
	
}
