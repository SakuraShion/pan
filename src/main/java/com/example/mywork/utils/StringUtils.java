package com.example.mywork.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class StringUtils {

    public static final String getRandomString(Integer count) {
        return RandomStringUtils.random(count,false,true);
    }
}
