package com.carbon.result;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
@Data
public class BlockByLimitNextResult implements Serializable {

    private List<BlockResult> block;

    @NoArgsConstructor
    @Data
    public static class BlockResult{
        private String blockID;
        private BlockHeadResult block_header;
        private List<TransactionsResult> transactions;
    }

    @NoArgsConstructor
    @Data
    public static class BlockHeadResult{
        private RawDataResult raw_data;
        private String witness_signature;
    }

    @NoArgsConstructor
    @Data
    public static class RawDataResult{
        private BigInteger number;
        private String witness_address;
        private String parentHash;
        private Integer version;
        private Long timestamp;
        private String txTrieRoot;
    }

    @NoArgsConstructor
    @Data
    public static class TransactionsResult{
        private Map<String,String> ret;
        private List<String> signature;
        private String txID;
        private TransactionRawDaw raw_data;
    }

    @NoArgsConstructor
    @Data
    public static class TransactionRawDaw{
        private String ref_block_bytes;
        private String ref_block_hash;
        private Long expiration;
        private Long timestamp;
        private List<ContractResult> contract;
    }

    @NoArgsConstructor
    @Data
    public static class ContractResult{
        private String type;
        private ContractParameter parameter;
    }

    @NoArgsConstructor
    @Data
    public static class ContractParameter{
        private String type_url;
        private ContractParameterValue value;
    }

    @NoArgsConstructor
    @Data
    public static class ContractParameterValue{
        private String to_address;
        private String owner_address;
        private BigInteger amount;
        private String data;
        private String contract_address;
    }
}
