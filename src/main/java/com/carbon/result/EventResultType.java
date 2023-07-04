package com.carbon.result;

import lombok.Data;

/**
 * "result_type": {
 *                 "from": "address",
 *                 "to": "address",
 *                 "value": "uint256"
 *             }
 */
@Data
public class EventResultType {
    private String from;
    private String to;
    private String value;
}
