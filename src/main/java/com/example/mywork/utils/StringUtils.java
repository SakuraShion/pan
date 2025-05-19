package com.example.mywork.utils;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
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
    public static String encodeByMd5(String str) {
        return str==null?null:DigestUtils.md2Hex(str);
    }
}
