package com.carbon.result;

import lombok.Data;

import java.util.List;

@Data
public class EventsResult {

   private List<EventResult> data;

   private EventsMetaResult meta;

   private Boolean success;

   private String error;

   private int statusCode;
}
