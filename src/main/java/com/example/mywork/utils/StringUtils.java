package com.example.mywork.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class StringUtils {

    public static final String getRandomString(Integer count) {
        return RandomStringUtils.random(count,false,true);
    }

    public static Boolean isEmpty(String str) {
        if (str==null|| str.equals("")|| "null".equals(str)|| "\u0000".equals(str)){
            return true;
        }else if ("".equals(str.trim())){
            return true;
        }
        return false;
    }
}
