package com.example.mywork.service;

import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mywork.entity.po.UserInfo;
import com.example.mywork.entity.query.UserInfoQuery;
import com.example.mywork.entity.vo.ResponseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-03-17
 */
public interface UserService extends IService<User> {

    User getByEamil(String email);

    /**
     * 注册
     * @param email
     * @param checkcode
     * @param nickName
     * @param password
     */
    boolean register(String email, String checkcode, String nickName, String password);

    SessionWebUserDto login(String email, String password);

    ResponseVo resetWord(String email, String password, String emailCode);

    List<UserInfo> findListByParam(UserInfoQuery userInfoQuery);

    void resetPwd(String email, String password, String emailCode);

    void changeUserSpace(String userId, Integer changeSpace);

    SessionWebUserDto qqLogin(String code);
}
