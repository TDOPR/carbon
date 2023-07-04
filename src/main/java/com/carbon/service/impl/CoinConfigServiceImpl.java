package com.carbon.service.impl;

import com.carbon.entity.CoinConfigEntity;
import com.carbon.mapper.CoinConfigDao;
import com.carbon.service.CoinConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("coinConfigService")
public class CoinConfigServiceImpl extends ServiceImpl<CoinConfigDao, CoinConfigEntity> implements CoinConfigService {


}
