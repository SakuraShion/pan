package com.example.mywork.commpone;

import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.dto.UserSpaceDto;
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
    public void saveUserSpaceUse(String userId, UserSpaceDto userSpaceDto){
        redisUtils.setex(Constants.REDIS_KEY_USER_USE+userId,userSpaceDto,60*60*24);
    }

    public  UserSpaceDto getUserSpaceUse(String userId){
        UserSpaceDto spaceDto=(UserSpaceDto)redisUtils.get(Constants.REDIS_KEY_USER_USE+userId);
        if (spaceDto==null){
            UserSpaceDto userSpaceDto=new UserSpaceDto();
            userSpaceDto.setUserSpace(0L);
            userSpaceDto.setTotalSpace(getsysSettingDto().getUserInitUseSpace()*Constants.MB);
        }
        return spaceDto;
    }
}
