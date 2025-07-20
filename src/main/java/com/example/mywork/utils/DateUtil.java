package com.example.mywork.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateUtil {
    private static final Object lock = new Object();
    private static Map<String,ThreadLocal<SimpleDateFormat>> sdfMap = new ConcurrentHashMap<String,ThreadLocal<SimpleDateFormat>>();

    private static SimpleDateFormat getSdf(String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = sdfMap.get(pattern);
        if (threadLocal == null) {
            synchronized (lock) {
                threadLocal = sdfMap.get(pattern);
                if (threadLocal == null) {
                    threadLocal=ThreadLocal.withInitial(()->new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, threadLocal);
                }
            }
        }
        return threadLocal.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getAfterDate(Integer day) {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,day);
        return calendar.getTime();
    }

}

