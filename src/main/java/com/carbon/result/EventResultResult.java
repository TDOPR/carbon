package com.carbon.result;

import lombok.Data;

import java.math.BigInteger;

/**
 * "result": {
 *                 "0": "0x9e211c9e4e004d37765393cef79bdb0b5259cd20",
 *                 "1": "0xa7be5eeb69efd6de9e620c4bbd65e25ba0461f1c",
 *                 "2": "9000000000000000000",
 *                 "from": "0x9e211c9e4e004d37765393cef79bdb0b5259cd20",
 *                 "to": "0xa7be5eeb69efd6de9e620c4bbd65e25ba0461f1c",
 *                 "value": "9000000000000000000"
 *             }
 */
@Data
public class EventResultResult {
    private String from;
    private String to;
    private BigInteger value;
    private BigInteger blockNumber;
    private String contract;
}
