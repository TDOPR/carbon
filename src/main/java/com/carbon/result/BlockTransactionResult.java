package com.carbon.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Lenovo
 */
@Data
public class BlockTransactionResult implements Serializable {

    private String fromAddress;

    private String toAddress;

    private BigInteger amount;

    private String contractAddress;

    private BigInteger blockNumber;

    private String txId;

}
