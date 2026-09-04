package com.danny.shoppingplatform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 單純轉成 JSON 字串，方便其他地方調用
     */
    public static String toJson(Object object) {
        if (object == null) {
            return "null";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert object ({}) to JSON string", object.getClass().getName(), e);
            return "{}";
        }
    }

    /**
     * 專門用於印出 JSON Log
     */
    public static void logAsJson(Object object) {
        if (object == null) {
            log.info("Object is null");
            return;
        }

        log.info("Object ({}): {}", object.getClass().getSimpleName(), toJson(object));
    }
}