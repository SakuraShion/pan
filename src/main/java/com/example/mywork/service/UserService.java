package com.example.mywork.service;

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
}
