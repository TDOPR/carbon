package com.carbon.result;

import lombok.Data;

import java.math.BigInteger;


/**
 * {
 *             "block_number": 25692754,
 *             "block_timestamp": 1607421099000,
 *             "caller_contract_address": "TWgQGagxMdNPt5fa1XDL4XMSkM1uYrgnG6",
 *             "contract_address": "TWgQGagxMdNPt5fa1XDL4XMSkM1uYrgnG6",
 *             "event_index": 0,
 *             "event_name": "Transfer",
 *             "result": {
 *                 "0": "0x9e211c9e4e004d37765393cef79bdb0b5259cd20",
 *                 "1": "0xa7be5eeb69efd6de9e620c4bbd65e25ba0461f1c",
 *                 "2": "9000000000000000000",
 *                 "from": "0x9e211c9e4e004d37765393cef79bdb0b5259cd20",
 *                 "to": "0xa7be5eeb69efd6de9e620c4bbd65e25ba0461f1c",
 *                 "value": "9000000000000000000"
 *             },
 *             "result_type": {
 *                 "from": "address",
 *                 "to": "address",
 *                 "value": "uint256"
 *             },
 *             "transaction_id": "f32253d315087e8bd3389a6773ffc74c228f1508ceb2ec462aca5116f75beb8a"
 *         }
 */

@Data
public class EventResult {

    private BigInteger block_number;
    private Long block_timestamp;
    private String caller_contract_address;
    private String contract_address;
    private Integer event_index;
    private String event_name;
    private String transaction_id;
    private EventResultResult result;
    private EventResultType result_type;
}
