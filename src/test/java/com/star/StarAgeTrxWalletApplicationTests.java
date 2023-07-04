package com.carbon;

import com.alibaba.fastjson.JSON;
import com.carbon.service.*;
import com.carbon.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.carbon.entity.CoinConfigEntity;
import com.carbon.mapper.CoinConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tron.keystore.WalletFile;
import org.tron.walletserver.WalletApi;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class StarAgeTrxWalletApplicationTests {

    @Autowired
    private Tron20Service tron20Service;

    @Resource
    private CoinConfigDao coinConfigDao;

    @Resource
    private UserWalletDao userWalletDao;

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private CoinAddressPoolService coinAddressPoolService;
    @Autowired
    private UserWithdrawService userWithdrawService;
    @Autowired
    private TRXTokenConfirmJobService confirmJobService;
    @Autowired
    private CoinCollectService coinCollectService;

    @Autowired
    private DESUtil desUtil;

    @Test
    void collect() {

        //BigDecimal surplus = tron20Service.balanceOf("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t", "TDsBchranwrmYfoQf3biRd5dFrgfcYe2nN", 6);
        //coinCollectService.sendTransferFee();
        coinCollectService.collect();
    }

    @Test
    void excuteOneBlock() {
        List<CoinConfigEntity> list = coinConfigDao.getEnableAll();

        Map<String, CoinConfigEntity> cmap = list.stream().collect(Collectors.toMap(CoinConfigEntity::getCoin, a -> a, (k1, k2) -> k1));

        Map<String, CoinConfigEntity> tokenConfigMap = list.stream().filter(configEntity -> Help.isNotNull(configEntity.getContract())).collect(Collectors.toMap(CoinConfigEntity::getContract, a -> a, (k1, k2) -> k1));

        CoinConfigEntity trxCoinConfig = cmap.get("TRX");

        userRechargeService.excuteOneBlock(tokenConfigMap, trxCoinConfig, new BigInteger("28919767"));

    }

    @Test
    void stringToHexString() {
        String s = "";
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        System.out.println(str);
    }

    @Test
    void trxAddr() {
        String s = "418a85a176bd600bc6c997734ee515b1b6da342813";
        System.out.println(TronUtils.fromHexAddress(s));
    }

    /**
     * 同步单个区块
     */
    @Test
    void rechargeByLimitNext() {
        List<CoinConfigEntity> list = coinConfigDao.getEnableAll();
        Map<String, CoinConfigEntity> cmap = list.stream().collect(Collectors.toMap(CoinConfigEntity::getCoin, a -> a, (k1, k2) -> k1));
        Map<String, CoinConfigEntity> tokenConfigMap = list.stream().filter(configEntity -> Help.isNotNull(configEntity.getContract())).collect(Collectors.toMap(CoinConfigEntity::getContract, a -> a, (k1, k2) -> k1));
        CoinConfigEntity trxCoinConfig = cmap.get("TRX");
        userRechargeService.getblockbylimitnext(tokenConfigMap, trxCoinConfig, new BigInteger("28930596"), new BigInteger("28930601"), 0);
    }

    @Test
    void rechargeJobByRange() {
        userRechargeService.rechargeJobByRange();
    }


    @Test
    void queryBlockJob() {
        confirmJobService.queryBlockJob();
    }


    @Test
    void showUserBonus() {

        String addresssStr = "TF9hCZphq2nNrRsoFRyiHxJJwXhZEdPRRc,TEUU1DcZuhDZFr5K732RbysEYxcNyH55bk,TC6SFzkhfsB6VK2mtdXWaKPaRMvgCsYHE3";
        String[] split = addresssStr.split(",");

        List<String> list = new ArrayList<>();
        for (String address : split) {
            String str = address + "_";
            List<BigDecimal> showUserBonus = tron20Service.showUserBonus(address);
            if (null != showUserBonus && showUserBonus.size() > 0) {
                BigDecimal bigDecimal = showUserBonus.get(1);
                str += bigDecimal.toPlainString() + "_";
                BigDecimal bigDecimal1 = showUserBonus.get(3);
                str += bigDecimal1.toPlainString() + "_";

            }

            List<BigDecimal> showUserBonusDy = tron20Service.showUserBonusDy(address);
            if (null != showUserBonusDy && showUserBonusDy.size() > 0) {
                BigDecimal bigDecimal = showUserBonusDy.get(1);
                str += bigDecimal.toPlainString() + "_";
                BigDecimal bigDecimal1 = showUserBonusDy.get(7);
                str += bigDecimal1.toPlainString() + "_";
            }
            list.add(str);
            log.info(log.getName() + "{}", str);
        }
        log.info(log.getName() + "------------------------------------------------------");
        for (String str : list) {
            log.info(log.getName() + "{}", str);
        }

    }

    @Test
    void batchInsert() {
        List<String> list = new ArrayList<>();
        list.add("TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE_4186812_0_0_0_TPUZFhG7VUEZfnHCvinbHep3ts3vRMt25z_10000000000_1608724965");
        list.add("TNEeHVcvPSeVSYX6HwKx4vtVBKyJLe4NoX_301034482_0_0_0_TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE_10000000000_1608518091");
        list.add("TYCRxskLNMynBWnxhh2R4H9xREnM5wGzmo_517241375_0_78509850_0_TNEeHVcvPSeVSYX6HwKx4vtVBKyJLe4NoX_10000000000_1608360828");
        list.add("TPkhqw9FzZFxAYdr5nv4zD1636mxaD4FQF_517241375_0_100123150_0_TYCRxskLNMynBWnxhh2R4H9xREnM5wGzmo_10000000000_1608367710");
        list.add("TFozHX9QmzdeZJ44RWoNXQqRfKzS4A7Gjr_535714285_0_140394086_0_TPkhqw9FzZFxAYdr5nv4zD1636mxaD4FQF_10000000000_1608367893");
        list.add("TMCd3aRioL2NuFWYPaEZD9Xj6Dsp4EZtht_517241375_0_76924137_0_TFozHX9QmzdeZJ44RWoNXQqRfKzS4A7Gjr_10000000000_1608369312");
        list.add("TBsqQ3QXEjZmsUvKb5XU7owyYdbWNHXsqj_517241375_0_52900000_0_TMCd3aRioL2NuFWYPaEZD9Xj6Dsp4EZtht_10000000000_1608372549");
        list.add("TJcKVoqxdmVU1REUtonKfAn2FCxPmWaGo5_504000000_0_0_0_TBsqQ3QXEjZmsUvKb5XU7owyYdbWNHXsqj_10000000000_1608373383");
        list.add("TLSNpqG2w3G2wuLp58g46Bakkz5Lr4e7z6_1000000_0_0_0_TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE_20000000000_1608533550");
        list.add("TFwaSa2RUVQjANEZpM5EWrQppsTt6tHV7w_108000000_0_0_0_TLSNpqG2w3G2wuLp58g46Bakkz5Lr4e7z6_10500000000_1608698793");
        list.add("TMjFNnXQQfv6UzWK45Cup1mUPi3cXM3SPD_4000000_0_0_0_TFwaSa2RUVQjANEZpM5EWrQppsTt6tHV7w_100000000_1608376989");
        list.add("TH2PLjXWsoCn1LHH4SeigtecVCvxrRcgbP_55000000_0_0_0_TMjFNnXQQfv6UzWK45Cup1mUPi3cXM3SPD_5500000000_1608380826");
        list.add("TWCxTXH6rBXK2Sc8RoLcghKGXHuToF6sJR_0_0_0_0_TH2PLjXWsoCn1LHH4SeigtecVCvxrRcgbP_4100000000_1608384153");
        list.add("TR8QitrhUrcReUxRGM4mrcscpBj1KXdXpW_0_0_0_0_TWCxTXH6rBXK2Sc8RoLcghKGXHuToF6sJR_2200000000_1608384885");
        //list.add("TRfiZeDeRHoAg9miPLeyBbsKsEzccu1MNW_0_0_0_0_TPUZFhG7VUEZfnHCvinbHep3ts3vRMt25z_5000000000_1608384939");
        list.add("TLN5tzR9vrc9kg4QyCXX6REzp3UT22a8bx_50000000_0_0_0_TH2PLjXWsoCn1LHH4SeigtecVCvxrRcgbP_5000000000_1608390477");
        list.add("TWy1MdG68rAgcU92xJwA5g68n4PayYBXHC_41000000_0_0_0_TLN5tzR9vrc9kg4QyCXX6REzp3UT22a8bx_4100000000_1608390807");
        list.add("TCx7UVVwJtpNTwkaHrCGW2Ksj5uq7Tuc3i_0_0_0_0_TH2PLjXWsoCn1LHH4SeigtecVCvxrRcgbP_5000000000_1608442374");
        list.add("TS5QirxqNsUYy9TiKH6UKr8hfJB597uymS_413793100_0_0_0_TFozHX9QmzdeZJ44RWoNXQqRfKzS4A7Gjr_10000000000_1608452877");
        list.add("TM2grqmGcZuwLKwuaDvXtfeyPoSWf2e2tZ_428571428_0_0_0_TS5QirxqNsUYy9TiKH6UKr8hfJB597uymS_10000000000_1608454326");
        list.add("THLNnRCpLXnmPa9doZ481dag9bLcC8g6hv_421750660_0_0_0_TM2grqmGcZuwLKwuaDvXtfeyPoSWf2e2tZ_10000000000_1608458886");
        list.add("TBjkQL19n3ze148VUgFBDuVJdXiA7eSWSu_0_0_0_0_TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE_100000000_1608517842");
        list.add("THLNedUbnocu1QBAuZfnzr3DTr4QniiM6j_0_0_0_0_TWy1MdG68rAgcU92xJwA5g68n4PayYBXHC_4000000000_1608520212");
        list.add("TNSjhF5kDV8fKc2gBkHVy9vtjURyEikFRR_300000000_0_0_0_TM2grqmGcZuwLKwuaDvXtfeyPoSWf2e2tZ_10000000000_1608533541");
        list.add("TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_330901855_0_0_0_THLNnRCpLXnmPa9doZ481dag9bLcC8g6hv_10000000000_1608535896");
        list.add("TUXvuWvhy882BtHaHL3TADhgbrPh9bnWbe_0_0_0_0_TLN5tzR9vrc9kg4QyCXX6REzp3UT22a8bx_3800000000_1608537603");
        list.add("TL6FcUTUJujdUiWTga583r6ACRcRrLDnTg_0_0_0_0_TBsqQ3QXEjZmsUvKb5XU7owyYdbWNHXsqj_5000000000_1608544818");
        list.add("TKvkLMkQC8j7Q3fYjw7ERMt7jh17HdD3jf_0_0_0_0_TL6FcUTUJujdUiWTga583r6ACRcRrLDnTg_5000000000_1608544950");
        list.add("TCXEsHRKDE5wfhpytroTkVsuJnRKasMXsL_200000000_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_10000000000_1608553713");
        list.add("TA2f5nS4TAYKcQhox1eXSLYkMuNs2vSkm6_0_0_0_0_TKvkLMkQC8j7Q3fYjw7ERMt7jh17HdD3jf_100000000_1608561498");
        list.add("THx6oAUrexu7xFZ3JekZRVNDAmYqWBbWYf_0_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_5000000000_1608623790");
        list.add("TF4GDgwmHjdJRSGmWC792pdYN2vVqGxV7j_0_0_0_0_TJcKVoqxdmVU1REUtonKfAn2FCxPmWaGo5_5000000000_1608633975");
        list.add("TNcKZqQ3wGky1XhNSkpRFRVsLkr35GzRHE_200000000_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_10000000000_1608634503");
        list.add("TQzSdq48ioPyaRwYa1F3ArzWG8Sq9Hbm1i_200000000_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_10000000000_1608634965");
        list.add("TC44rN2oBgbr4Qv5d8g7pPmXaZhcDykq9N_50000000_0_0_0_TJcKVoqxdmVU1REUtonKfAn2FCxPmWaGo5_5000000000_1608635832");
        list.add("TXR22PwCqbDk8G4jmB2SHKdSewkfeFg44U_50000000_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_5000000000_1608637665");
        list.add("TG24AJixjzAtLpFcF4s5qB2kjVXtxB8u8P_50000000_0_0_0_TNSjhF5kDV8fKc2gBkHVy9vtjURyEikFRR_5000000000_1608639210");
        list.add("TBALvDZBgUph6R6NXjxCWx8vrMMYLZMoUr_50000000_0_0_0_TF4GDgwmHjdJRSGmWC792pdYN2vVqGxV7j_5000000000_1608639504");
        list.add("TLQg4jRgPX9cw4pGiyUouMLgqakHkcSP2Z_70000000_0_0_0_TCXEsHRKDE5wfhpytroTkVsuJnRKasMXsL_7000000000_1608639726");
        list.add("THsuWpiWZ9gd3wBqdeHdW9wkQA3nNcmtje_70000000_0_0_0_TNcKZqQ3wGky1XhNSkpRFRVsLkr35GzRHE_7000000000_1608639897");
        list.add("TMnjREJKVAubEfDjiLoRCSWKsNb2fS38wG_100000000_0_0_0_THLNnRCpLXnmPa9doZ481dag9bLcC8g6hv_10000000000_1608641154");
        list.add("TBStskEmp2c5QpK2QmY1tRzDPa4zPRvyhU_0_0_0_0_TBjkQL19n3ze148VUgFBDuVJdXiA7eSWSu_100000000_1608642672");
        list.add("TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_100000000_0_25000000_0_TF4GDgwmHjdJRSGmWC792pdYN2vVqGxV7j_10000000000_1608647802");
        list.add("TLKSiiRNb5jsxb2iznXfX7pSGzCQpwfFVV_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608651465");
        list.add("TCVdXp4RgfffY91okgWRWXTntYGmcPQ9LA_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608651561");
        list.add("TJkbG3oWPsxAvYJbVrQEHUYJRpxuWFRGaa_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608651672");
        list.add("TNM441qTZSSJtoQSXuCusfqNHt5FZ6TWN4_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608651945");
        list.add("TU9sVYQmZSokDcWtuWxNoP11GvYnJgT6N9_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608652104");
        list.add("TPKFGKABkSYauw2EkPv5KJk7dM13TmZHCL_50000000_0_0_0_TStnzHpMSosXjzBRpzFGSXhSEdHAFbVtx5_5000000000_1608652248");
        list.add("TSmCwmdaQGCzp9dg3mC6HAQ4Ha47LMgs3D_0_0_0_0_TCVdXp4RgfffY91okgWRWXTntYGmcPQ9LA_5000000000_1608692289");
        list.add("TNp9f8aQDTtfWht97JHbGcZGrnxPKfzKnW_0_0_0_0_TCVdXp4RgfffY91okgWRWXTntYGmcPQ9LA_5000000000_1608692544");
        list.add("TGJvezwAnsVNRoReAhF7qKePJwQaWyYnHn_0_0_0_0_THx6oAUrexu7xFZ3JekZRVNDAmYqWBbWYf_5000000000_1608696660");
        list.add("TGAVkaEErhiegoQUB2V35W7MEeQ9V1aTgK_0_0_0_0_TGJvezwAnsVNRoReAhF7qKePJwQaWyYnHn_5000000000_1608729189");
        list.add("TUXFGkaiEvdq3YFNrFABb4C3RuqhTDCPiy_0_0_0_0_TCmPA8TV1yGHjtVioskuFdK7b6BjwHpZmB_10000000000_1608732444");
        list.add("TPH7eKXja2zkQfwx1BS4d4rXZRxqY6B6XS_0_0_0_0_TA2f5nS4TAYKcQhox1eXSLYkMuNs2vSkm6_100000000_1608738885");
        list.add("TRs3KAgRgnxnYN1bmi7zN19Tj4NbNLvTiB_0_0_0_0_TPH7eKXja2zkQfwx1BS4d4rXZRxqY6B6XS_10000000000_1608739575");
        list.add("TWQDHjkw5myxiQZDsRf1L9fGDN1P5deBeJ_0_0_0_0_TRs3KAgRgnxnYN1bmi7zN19Tj4NbNLvTiB_10000000000_1608741096");
        list.add("TJ6gTw5xK2ipKnhGuJGy4fbfTinpci2SVT_0_0_0_0_TJcKVoqxdmVU1REUtonKfAn2FCxPmWaGo5_10000000000_1608795375");
        list.add("TNvx2VdW18PUZDF9UB56PPSLqEEQzptPSn_0_0_0_0_TWQDHjkw5myxiQZDsRf1L9fGDN1P5deBeJ_10000000000_1608801693");
        list.add("TF9hCZphq2nNrRsoFRyiHxJJwXhZEdPRRc_0_0_0_0_TNvx2VdW18PUZDF9UB56PPSLqEEQzptPSn_10000000000_1608802653");
        list.add("TEUU1DcZuhDZFr5K732RbysEYxcNyH55bk_0_0_0_0_TFwaSa2RUVQjANEZpM5EWrQppsTt6tHV7w_10000000000_1608804471");
        list.add("TC6SFzkhfsB6VK2mtdXWaKPaRMvgCsYHE3_0_0_0_0_TFwaSa2RUVQjANEZpM5EWrQppsTt6tHV7w_3800000000_1608804726");

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            log.info(log.getName() + " i:{}, str:{}", i, str);
            String[] arr = str.split("_");
            /*String _addr, BigDecimal _value, String _upAddr, Long _investTime,
                    BigDecimal _historyGlcStaticBonus, BigDecimal _historyFbbStaticBonus, BigDecimal _historyGlcDyBonus, BigDecimal _historyPartnerBonus*/
            String _addr = arr[0];
            BigDecimal _historyGlcStaticBonus = new BigDecimal(arr[1]);
            BigDecimal _historyFbbStaticBonus = new BigDecimal(arr[2]);
            BigDecimal _historyGlcDyBonus = new BigDecimal(arr[3]);
            BigDecimal _historyPartnerBonus = new BigDecimal(arr[4]);
            String _upAddr = arr[5];
            BigDecimal _value = new BigDecimal(arr[6]);
            Long _investTime = Long.parseLong(arr[7]);

            String txid = insertUser(_addr, _value, _upAddr, _investTime,
                    _historyGlcStaticBonus, _historyFbbStaticBonus, _historyGlcDyBonus, _historyPartnerBonus);
            log.info(log.getName() + " i:{}, str:{}, txid:{}", i, str, txid);
        }


    }

    String insertUser(String _addr, BigDecimal _value, String _upAddr, Long _investTime,
                      BigDecimal _historyGlcStaticBonus, BigDecimal _historyFbbStaticBonus, BigDecimal _historyGlcDyBonus, BigDecimal _historyPartnerBonus) {
        String from = "TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS";
        String prikey = "c1a34e5250f58b6c0a132a954efb7bb320cf5f509d22efd34676de77b623d570";
        String contract = "TFYdhtZABaDe2jTqhG14WymDaMEkkqXm2c";

        /**
         * 新增插入数据接口
         *    function insertUser(address _addr,uint256 _value, address _upAddr,uint256 _investTime, uint256 _historyGlcStaticBonus, uint256 _historyFbbStaticBonus, uint256 _historyGlcDyBonus, uint256 _historyPartnerBonus)
         * //参数：用户地址，入金量，上级地址，投资时间，已领取静态GLC数量，已领取静态FBB数量，已领取推荐GLC数量，已领取股东加权GLC数量
         */
        String method = "insertUser(address,uint256,address,uint256,uint256,uint256,uint256,uint256)";

        List<Object> params = new ArrayList();
        params.add(_addr);
        params.add(_value);
        params.add(_upAddr);
        params.add(_investTime);

        params.add(_historyGlcStaticBonus);
        params.add(_historyFbbStaticBonus);
        params.add(_historyGlcDyBonus);
        params.add(_historyPartnerBonus);

        try {
            System.out.println("开始发送合约交易");
            TronUiltNew.BroadcastResultBean result = TronUiltNew.sendContractTransaction(from, prikey, contract, method, params);
            System.out.println(result.result + " ; " + JSON.toJSONString(result));
            return result.txid;
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        return null;
    }

    @Test
    void settlementVip() {
        String from = "TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS";
        String prikey = "c1a34e5250f58b6c0a132a954efb7bb320cf5f509d22efd34676de77b623d570";
        String contract = "TFYdhtZABaDe2jTqhG14WymDaMEkkqXm2c";

        String method = "settlementVip(address[],uint256[])";
        List<String> addressList = new ArrayList<>();
        addressList.add("TCfFHryA93Cb4hsGPQij2jdwc8PyePS4hE");
        List<BigDecimal> amountList = new ArrayList<>();
        amountList.add(new BigDecimal("14811200000"));
        List<Object> params = new ArrayList();
        params.add(addressList);
        params.add(amountList);

        try {
            System.out.println("开始发送合约交易");
            TronUiltNew.BroadcastResultBean result = TronUiltNew.sendContractTransaction(from, prikey, contract, method, params);
            System.out.println(result.result + " ; " + JSON.toJSONString(result));
        } catch (Exception var12) {
            var12.printStackTrace();
        }
    }

    @Test
    void showUserBonusDy() {
    }

    /**
     * {
     * "data": [],
     * "success": true,
     * "meta": {
     * "at": 1607345485525,
     * "page_size": 0
     * }
     * }
     */
    @Test
    void eventsTest() {
        /*CoinConfigEntity coinConfigEntity = coinConfigDao.selectById(4);
        BigInteger blockNo = coinConfigEntity.getBlockNo();
        String url = "https://api.trongrid.io/v1/contracts/" + coinConfigEntity.getContract() + "/events?limit=20&block_number=" + blockNo;*/
        userRechargeService.rechargeJob();

//        userWithdrawService.transTrxToken(userWithdrawService.selectById(5));
    }

    @Test
    void desUtil() {
        String password = "BDC32B0435D1AD3C2D6FC3C494281231D34B8160D0FD70D700BD11AEA184D4722E931FBAADD712AC";
        String privateKey = "A0BC41CD0640CE066FCC195AB8514BE524F8219E22BC723CE0FD11DB5764272AAB3B88155D9411C13A7872E2630C9395F0919A4EFCB1B4702B569B72F671679A2E931FBAADD712AC";
        password = desUtil.decrypt(password);
        log.info("password:{}", password);
        privateKey = desUtil.decrypt(privateKey);
        log.info("privateKey:{}", privateKey);
    }

    private WalletApi walletApi;

    @Test
    void getBalance() {
        String address = "TQkt9GKA4SCif5Yu4vdbRhdUJQbPnNaEWh";
        String contract = "TLep6Hu5yEaw6Gkykf7Y4PzeS7VQPgph83";

        BigDecimal balanceOfTron = tron20Service.balanceOfTron(address);
        log.info("balanceOfTron:{}", balanceOfTron);

        BigDecimal balanceOfContract = tron20Service.balanceOf(contract, address, 6);
        log.info("balanceOfContract:{}", balanceOfContract);

    }

    public WalletFile getWalletFile(String fromUserKeystore) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(fromUserKeystore, WalletFile.class);

    }

    @Test
    void createAddress() throws Exception {
        coinAddressPoolService.batchCreateEthAddress();
        /*String password = "qwTer1234q51wer";
        byte[] passwd = StringUtils.char2Byte(password.toCharArray());
        WalletFile walletFile = WalletApi.CreateWalletFile(passwd);
        String keystore = JSON.toJSONString(walletFile);
        log.info("keystore:{}", keystore);
        ECKey decrypt = Wallet.decrypt(passwd, walletFile);
        byte[] privKeyBytes = decrypt.getPrivKeyBytes();
        String privateKey = ByteArray.toHexString(privKeyBytes);
        log.info("privateKey:{}", privateKey);*/

        //String s2 = ByteArray.toStr(privKeyBytes);
        /*String s1 = new String(privateKey);
        log.info("privateKey:{}", s1);*/

        /*CredentialsEckey credentialsEckey = CredentialsEckey.create(Wallet.decrypt(passwd, walletFile));
        SignInterface pair = credentialsEckey.getPair();
        byte[] privateKey1 = pair.getPrivateKey();
        char[] chars = StringUtils.byte2Char(privateKey1);
        String s = chars.toString();*/


        /*String address = walletFile.getAddress();
        log.info("address :{}", address);
        byte[] decrypt2PrivateBytes = Wallet.decrypt2PrivateBytes(passwd, walletFile);
        String privateKey = decrypt2PrivateBytes.toString();
        log.info("privateKey :{}", privateKey);*/

        //WalletApiWrapper w = new WalletApiWrapper();

        /*char[] password = Utils.inputPassword(true);
        String fileName = w.registerWallet(password);
        log.info("fileName :{}", fileName);*/

        /*GrpcAPI.AddressPrKeyPairMessage addressPrKeyPairMessage =
                w.generateAddress();
        String privateKey = addressPrKeyPairMessage.getPrivateKey();*/

        /*String password = "qwTer1234q51wer";
        String keystore = w.registerWallet(password.toCharArray());
        log.info("keystore :{}", keystore);*/

        /*walletApi.createAccount()
        JSONObject address = tron20Service.createAddress();
        log.info("address :{}", address);*/
    }

    @Test
    void contextLoads() {

        //tron20Service.getTransactionInfoByBlockNum(new BigInteger("25656644"));
        tron20Service.getConfirmedTransaction("ce20129437530aad20c850c3bdd7969a86135b5fd0076e7a9b329289c213c36c");

        //tron20Service.getTransactionFromBlock(new BigInteger("25656644"));
        //tronsShowTrc20Service.addressToUser("TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS");
        //tronsShowTrc20Service.globalNodeNumber();
        //tronsShowTrc20Service.isOut("TQPKS1sDmHxLuUYpFP6C7G8yfbKsJVqseS");
        //tronsShowTrc20Service.getAlllUsers1(10,5);
        //tronsShowTrc20Service.getAlllUsers1(0,13);
        //tronsShowTrc20Service.getAlllUsers2(0, 10);
        //tronsShowTrc20Service.totalInvestAmount();
        //tronsShowTrc20Service.sendRace();
        //Help.stampToDate(1605022707);
        //R reboot = tronsShowTrc20Service.reboot();
        //tronsShowTrc20Service.opened();

		/*SendRaceFrom from = new SendRaceFrom();
		from.setAddress0("TK6VxYw6GLfanfZfX8bDe5kUqC1X3RGuzS");
		from.setAddress1("TMgQrgGYCD8UNrTZvURycJ5jCVxzvxCQD8");
		from.setAddress2("TSPDNBCTZFtv9mkdTV7E48t7VZn3w4Jv2f");
		from.setAddress3("TMsNp9pzWLChc38PdiLBH2mMVLFMJFseb2");
		from.setAddress4("TY48uEG8HKCBG3jeFi4efzzgaa2gSFqFtN");
		from.setAddress5("TAynY1epEGbL97SjrF7iBjswxxadSAb72r");
		from.setAddress6("THjBo6CqYKRRZHPCw3djwUre7QQNYW5wLn");
		from.setAddress7("TCFYPstrR9xhpHxkPKaZLKZt8KJijZLDPA");
		from.setAddress8("TLqSYkHVT8EenKu1dqRWpmqQn7bHaxb9A2");
		from.setAddress9("TBP6d2fPDkF6FR6ptGUKSFfJk5nEcjDw98");
		tronsShowTrc20Service.sendRace(from);*/

        //tronGlobalTrc20Service.getAlllUsers1(0, 10);
    }

    @Test
    void test() {
        String data = "a9059cbb00000000000000000000000012ef9b8c9bc78bae91652136a71548aa23aad5d90000000000000000000000000000000000000000000000000000000007088980";
        String address = data.substring(8, 8 + 64);
        String amount = data.substring(8 + 64, data.length());
        System.out.println(new BigInteger(amount, 16).longValue());
        address = "41" + address.substring(24, address.length());
        System.out.println(TronUtils.encode58Check(ByteArray.fromHexString(address)));
        System.out.println(TronUtils.encode58Check(ByteArray.fromHexString("41a614f803b6fd780986a42c78ec9c7f77e6ded13c")));
    }

}
