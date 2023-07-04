package com.carbon.enums;

/**
 * @Description: 充币状态
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 上午9:58
 * @Modified by: Chen Long
 */
public enum CoinRechargeStatusEnum {
    //状态：0-待入帐；1-充值成功，2到账失败，3到账成功

    PENDING_ENTRY(0, "待入帐"),
    RECHARGE_SUCCEEDED(1, "充值成功"),
    FAILED(2, "2到账失败"),
    SUCCEEDED(3, "3到账成功");

    private int code;
    private String desc;

    CoinRechargeStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
