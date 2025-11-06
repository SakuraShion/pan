package com.example.mywork.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String convertObj2Json(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T convertJson2Obj(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    public static <T> List<T> convertJsonArray2List(String json, Class<T> classz) {
        try {
            return mapper.readValue(json,mapper.getTypeFactory().constructCollectionType(List.class,classz));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return java.util.Collections.emptyList();
    }
}
