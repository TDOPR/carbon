package com.carbon.manager;

import com.carbon.entity.CoinConfigEntity;
import com.carbon.entity.EventEntity;
import com.carbon.service.CoinConfigService;
import com.carbon.service.EventService;
import com.carbon.service.Tron20Service;
import com.carbon.utils.Help;
import com.carbon.utils.R;
import com.carbon.utils.TronUiltNew;
import com.carbon.utils.TronUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class Manager {

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private EventService eventService;

    @Autowired
    private Tron20Service tron20Service;

    private final static String EVENT_CONTRACT = "TPUBjsQNoAyVuxJmg1n8pKjA4YtejbGy3L";

    private final static BigInteger BLOCK_DIFF = new BigInteger("15");

    private static String URL_LAST_BLOCK = "https://api.trongrid.io/wallet/getnowblock";

    public void analyzeEvent() throws Exception {
        //获取事务
        String contractAddress = EVENT_CONTRACT;
        QueryWrapper<CoinConfigEntity> coinConfigEntityQueryWrapper = new QueryWrapper<>();
        coinConfigEntityQueryWrapper.eq("coin", "EVENT");
        BigInteger blockNumber = coinConfigService.getOne(coinConfigEntityQueryWrapper).getBlockNo();
        blockNumber = blockNumber.add(BigInteger.ONE);
        //获取当前最新区块
        String blockStr = this.doGet(URL_LAST_BLOCK);
        TronUiltNew.BlockResultBean blockInfo = (TronUiltNew.BlockResultBean) JSON.parseObject(blockStr, TronUiltNew.BlockResultBean.class);
        BigInteger currentBlock = new BigInteger(blockInfo.block_header.raw_data.number + "");
        if (currentBlock.compareTo(blockNumber.add(BLOCK_DIFF)) == 1) {
            String url = "https://api.trongrid.io/v1/contracts/" + contractAddress + "/events?block_number=" + blockNumber.toString();

            //根据事务处理逻辑
            String res = this.doGet(url);
            JSONObject data = JSONObject.parseObject(res);
            JSONArray events = data.getJSONArray("data");
            for (int i = 0; i < events.size(); i++) {
                JSONObject eventObj = JSONObject.parseObject(events.get(i).toString());
                String eventName = eventObj.get("event_name").toString();
                JSONObject eventParams = JSONObject.parseObject(eventObj.get("result").toString());
                String addr = TronUtils.fromHexAddress("41" + eventParams.get(0).toString().substring(2));
                String inviteAddr = TronUtils.fromHexAddress("41" + eventParams.get(1).toString().substring(2));
                String amount = eventParams.get(2).toString();
                EventEntity eventEntity = new EventEntity();
                if (eventName.equals("BuyPowerOne")) {
//                    this.buyPower(addr, inviteAddr, amount, 0);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                } else if (eventName.equals("BuyPowerTwo")) {
//                    this.buyPower(addr, inviteAddr, amount, 1);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                } else if (eventName.equals("BuyLpPower")) {
//                    this.buyLpPower(addr, inviteAddr, amount);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                }
                if (Help.isNotNull(eventEntity)) {
                    eventService.save(eventEntity);
                }
            }
            //更新事务处理块no
            CoinConfigEntity coinConfig = new CoinConfigEntity();
            coinConfig.setId(4);
            coinConfig.setBlockNo(blockNumber);
            coinConfigService.updateById(coinConfig);
        }
    }

    public R debugAnalyzeEvent(String txHash) throws Exception {
        //获取事务
        String contractAddress = EVENT_CONTRACT;
        //根据hash查询区块号
        R infoResult = tron20Service.getConfirmedTransaction(txHash);
        if (Help.isNotNull(infoResult) && "0".equals(infoResult.get("code").toString())) {
            String blockNumber = infoResult.get("data").toString();
            //查看是否处理过该区块
            QueryWrapper<EventEntity> eventEntityQueryWrapper = new QueryWrapper<>();
            eventEntityQueryWrapper.eq("block_num", blockNumber);
            EventEntity checkEventEntity = eventService.getOne(eventEntityQueryWrapper);
            if (Help.isNotNull(checkEventEntity)) {
                return R.error("该交易已完成，不可重复添加");
            }
            //查看该区块是否领先系统区块
            QueryWrapper<CoinConfigEntity> coinConfigEntityQueryWrapper = new QueryWrapper<>();
            coinConfigEntityQueryWrapper.eq("coin", "EVENT");
            CoinConfigEntity coinConfig = coinConfigService.getOne(coinConfigEntityQueryWrapper);
            if (new BigInteger(blockNumber).compareTo(coinConfig.getBlockNo()) > -1) {
                return R.error("该交易系统还未扫描到，请耐心等待扫描，暂时无法手动添加");
            }
            //获取当前区块事务
            String url = "https://api.trongrid.io/v1/contracts/" + contractAddress + "/events?block_number=" + blockNumber.toString();
            //根据事务处理逻辑
            String res = this.doGet(url);
            JSONObject data = JSONObject.parseObject(res);
            JSONArray events = data.getJSONArray("data");
            for (int i = 0; i < events.size(); i++) {
                JSONObject eventObj = JSONObject.parseObject(events.get(i).toString());
                String eventName = eventObj.get("event_name").toString();
                JSONObject eventParams = JSONObject.parseObject(eventObj.get("result").toString());
                String addr = TronUtils.fromHexAddress("41" + eventParams.get(0).toString().substring(2));
                String inviteAddr = TronUtils.fromHexAddress("41" + eventParams.get(1).toString().substring(2));
                String amount = eventParams.get(2).toString();
                EventEntity eventEntity = new EventEntity();
                if (eventName.equals("BuyPowerOne")) {
//                    this.buyPower(addr, inviteAddr, amount, 0);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                } else if (eventName.equals("BuyPowerTwo")) {
//                    this.buyPower(addr, inviteAddr, amount, 1);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                } else if (eventName.equals("BuyLpPower")) {
//                    this.buyLpPower(addr, inviteAddr, amount);
                    log.info("当前扫描到的区块是" + blockNumber);

                    eventEntity.setBlockNum(blockNumber.toString());
                }
                if (Help.isNotNull(eventEntity)) {
                    eventService.save(eventEntity);
                    return R.ok("确认交易成功");
                }
            }
            return R.error("交易hash非法，没有投入成功");
        } else {
            return R.error("交易hash错误，获取区块交易失败");
        }
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;

        try {
            URL url = new URL(httpurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("TRON-PRO-API-KEY", "c15b86be-e273-462e-a2b3-3ff4fe713dc1");

            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;

                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }

                result = sbf.toString();
            }
        } catch (MalformedURLException var23) {
            var23.printStackTrace();
        } catch (IOException var24) {
            var24.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }
            }

            connection.disconnect();
        }

        return result;
    }
}
