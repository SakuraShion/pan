package com.example.mywork.service.impl;

import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.config.AppConfig;
import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.dto.UserSpaceDto;
import com.example.mywork.entity.User;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.constants.UserStatusEnum;
import com.example.mywork.entity.po.UserInfo;
import com.example.mywork.entity.query.UserInfoQuery;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.exception.BusinessException;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.EmailCodeService;
import com.example.mywork.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mywork.utils.OKHttpUtils;
import com.example.mywork.utils.StringUtils;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private EmailCodeService emailCodeService;

    @Resource
    private RedisCommpont redisCommpont;

    @Resource
    private AppConfig appConfig;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
            UserSpaceDto userSpaceDto=new UserSpaceDto();
            //userSpaceDto.setTotalSpace();
            userSpaceDto.setUserSpace(byEmail.getUseSpace());
            redisCommpont.saveUserSpaceUse(byEmail.getUserId(),userSpaceDto);
            return dto;
        }
        return null;
    }

    @Override
    public ResponseVo resetWord(String email, String password, String emailCode) {
        User user = this.userMapper.selectByEmail(email);
        if (user==null){
            return ResponseVo.fail("账号不存在");
        }
        boolean b = emailCodeService.checkCode(email, emailCode);
        if (b==true) {
            String s = StringUtils.encodeByMd5(password);
            Integer i = userMapper.setPassWord(email, s);
            if (i==1) {
                return ResponseVo.ok("重置成功");
            }
        }
        return ResponseVo.fail("重置失败");
    }

    @Override
    public List<UserInfo> findListByParam(UserInfoQuery userInfoQuery) {
        return Collections.emptyList();
    }

    @Override
    public void resetPwd(String email, String password, String emailCode) {
        User user = this.userMapper.selectByEmail(email);
        if (user==null){
            throw new BusinessException("邮箱账号不存在");
        }

        emailCodeService.checkCode(email, emailCode);

        UserInfo userInfo=new UserInfo();
        userInfo.setPassword(StringUtils.encodeByMd5(password));
        this.userMapper.updateByEmail(userInfo,email);
    }

    @Override
    public void changeUserSpace(String userId, Integer changeSpace) {
        Long space = changeSpace * Constants.MB;
        this.userMapper.updateUserSpace(userId,null,space);
        redisCommpont.resetUserSpaceUse(userId);
    }

    private String getQQOpenId(String accessToken) throws BusinessException {
        return "";
    }

    @Override
    public SessionWebUserDto qqLogin(String code) {
        return null;
    }
    private String getQQAccessToken(String code) {
        /**
         * 返回结果是字符串 access_token=*&expires_in=7776000&refresh_token=* 返回错误 callback({UcWebConstants.VIEW_OBJ_RESULT_KEY:111,error_description:"error msg"})
         */
        String accessToken = null;
        String url = null;

        try {
            url = String.format(appConfig.getQqUrlAccessToken(), appConfig.getQqAppId(), appConfig.getQqAppKey(), code, URLEncoder.encode(appConfig
                    .getQqUrlRedirect(), "utf-8"));
        }catch (UnsupportedEncodingException e) {
            logger.error("encode失败");
            String tokenResult = OKHttpUtils.getRequest(url);
            if (tokenResult==null|| tokenResult.contains(Constants.VIEW_OBJ_RESULT_KEY)){
                logger.error("获取qqToken失败:{}", tokenResult);
                throw new BusinessException("获取qqToken失败");
            }
            String[] params = tokenResult.split("&");
            if (params!=null && params.length>0){
                for (String param : params) {
                    if (param.contains("access_token")){
                        accessToken = param.split("=")[1];
                        break;
                    }
                }
            }

        }
        return accessToken;


    }
}
