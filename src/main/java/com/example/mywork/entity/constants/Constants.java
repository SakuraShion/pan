package com.example.mywork.entity.constants;

public class Constants {
    public static final String CHECK_CODE_KEY = "CHECK_CODE_KEY";
    public static final String CHECK_CODE_KEY_EMAIL = "CHECK_CODE_KEY_EMAIL";

    public static final Integer LENGTH=5;
    public static final Integer LENGTH_15=15;

    public static final Integer ZERO=0;

    public static final String SESSION_SHARE_KEY = "session_share_key_";

    public static final Long MB=1024*1024L;

    /**
     * 过期时间 1分钟
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60;
    /**
     * 过期时间 1天
     */
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 60 * 24;

    public static final Integer REDIS_KEY_EXPIRES_ONE_HOUR = REDIS_KEY_EXPIRES_ONE_MIN * 60;
    public static final String REDIS_KEY_USER_FILE_TEMP_SIZE = "easypan:user:file:temp:";

    public static final String REDIS_KEY_SYS_SETTING="pan:syssetting:";

    public static final String REDIS_KEY_USER_USE="pan:user:spaceuser";
    public static final String SESSION_KEY = "session_key";
}
