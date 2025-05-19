package com.example.mywork.service.impl;

import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.entity.User;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.constants.UserStatusEnum;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.EmailCodeService;
import com.example.mywork.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mywork.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hb
 * @since 2025-03-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailCodeService emailService;

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private RedisCommpont redisCommpont;

    @Override
    public User getByEamil(String email) {
        User user=userMapper.getByEmail(email);
        return user;
    }

    @Override
    public boolean register(String email, String checkcode, String nickName, String password) {
        User user = userMapper.getByEmail(email);
        if(user!=null){
            return false;
        }
        User userByNick =userMapper.selectByNickName(nickName);
        if(userByNick!=null){
            return false;
        }
        //邮箱验证
        boolean b = emailCodeService.checkCode(email, checkcode);
        if (!b){
            return false;
        }
        String userId = StringUtils.getRandomString(Constants.LENGTH_15);
        User newUser=new User();
        newUser.setUserId(userId);
        newUser.setNickName(nickName);
        newUser.setPassword(StringUtils.encodeByMd5(password));
        newUser.setEmail(email);
        newUser.setJoinTime(LocalDateTime.now());
        newUser.setUseSpace(0L);
        SysSettingDto sysSettingDto = redisCommpont.getsysSettingDto();
        newUser.setTotalSpace(sysSettingDto.getUserInitUseSpace()*Constants.MB);
        newUser.setStatus(UserStatusEnum.ENABLE.getStatus());
        userMapper.insert(newUser);
        return true;
    }

    @Override
    public SessionWebUserDto login(String email, String password) {
        User byEmail = userMapper.getByEmail(email);
        if (byEmail.getEmail().equals(StringUtils.encodeByMd5(password))) {
            byEmail.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(byEmail);
            SessionWebUserDto dto=new SessionWebUserDto();
            dto.setUserId(byEmail.getUserId());
            dto.setNickName(byEmail.getNickName());
            dto.setIsAdmin(byEmail.getUserId().equals("admin"));
            dto.setAvatar(byEmail.getQqAvater());
            //用户空间
            return dto;
        }
        return null;
    }
}
