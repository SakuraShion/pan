package com.example.mywork.service;

import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
