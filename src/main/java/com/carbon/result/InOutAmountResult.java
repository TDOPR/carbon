package com.carbon.result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InOutAmountResult {

    /**
     *
     */
    private BigDecimal outAmount;
    /**
     *
     */
    private BigDecimal inAmount;
    /**
     *
     */
    private BigDecimal remainAmount;
}
