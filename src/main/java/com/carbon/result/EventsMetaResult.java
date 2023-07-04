package com.carbon.result;

import lombok.Data;

@Data
public class EventsMetaResult {

    private Long at;
    private Integer page_size;
    private String fingerprint;
    private EventsLinksResult links;


}
