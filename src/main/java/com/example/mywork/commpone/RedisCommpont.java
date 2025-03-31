package com.example.mywork.commpone;

import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.entity.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisCommpont {
    @Resource
    private RedisUtils redisUtils;

    public SysSettingDto getsysSettingDto(){
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (sysSettingDto==null){
            sysSettingDto = new SysSettingDto();
            redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
        }
        return sysSettingDto;
    }

}
