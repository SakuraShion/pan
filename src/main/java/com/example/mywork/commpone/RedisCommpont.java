package com.example.mywork.commpone;

import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.dto.UserSpaceDto;
import com.example.mywork.entity.FileInfo;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.po.UserInfo;
import com.example.mywork.entity.query.UserInfoQuery;
import com.example.mywork.mapper.FileInfoMapper;
import com.example.mywork.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisCommpont {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserMapper userInfoMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    public SysSettingDto getsysSettingDto() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (sysSettingDto == null) {
            sysSettingDto = new SysSettingDto();
            redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
        }
        return sysSettingDto;
    }

    public void saveUserSpaceUse(String userId, UserSpaceDto userSpaceDto) {
        redisUtils.setex(Constants.REDIS_KEY_USER_USE + userId, userSpaceDto, 60 * 60 * 24);
    }

    public UserSpaceDto getUserSpaceUse(String userId) {
        UserSpaceDto spaceDto = (UserSpaceDto) redisUtils.get(Constants.REDIS_KEY_USER_USE + userId);
        if (spaceDto == null) {
            UserSpaceDto userSpaceDto = new UserSpaceDto();
            userSpaceDto.setUserSpace(0L);
            userSpaceDto.setTotalSpace(getsysSettingDto().getUserInitUseSpace() * Constants.MB);
        }
        return spaceDto;
    }

    public UserSpaceDto resetUserSpaceUse(String userId) {
        UserSpaceDto userSpaceDto = new UserSpaceDto();
        FileInfo fileInfo = fileInfoMapper.selectUseSpaceByUserId(userId);
        if (fileInfo.getFileSize() == null) {
            fileInfo.setFileSize(0L);
        }
        userSpaceDto.setUserSpace(fileInfo.getFileSize());
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        userSpaceDto.setTotalSpace(userInfo.getTotalSpace());
        redisUtils.setex(Constants.REDIS_KEY_USER_USE + userId, userSpaceDto, Constants.REDIS_KEY_EXPIRES_DAY);
        return userSpaceDto;
    }
    public void saveFileTempSize(String userId, String fileId, Long fileSize) {
        Long currentSize = getFileTempSize(userId, fileId);
        redisUtils.setex(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId, currentSize + fileSize, Constants.REDIS_KEY_EXPIRES_ONE_HOUR);
    }


    public Long getFileTempSize(String userId, String fileId) {
        Long currentSize = getFileSizeFromRedis(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId);
        return currentSize;
    }
    private Long getFileSizeFromRedis(String key) {
        Object sizeObj = redisUtils.get(key);
        if (sizeObj == null) {
            return 0L;
        }
        if (sizeObj instanceof Integer) {
            return ((Integer) sizeObj).longValue();
        }else if (sizeObj instanceof Long) {
            return (Long) sizeObj;
        }
        return 0L;
    }

}
