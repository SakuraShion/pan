package com.example.mywork.service.impl;

import com.example.mywork.entity.User;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public User getByEamil(String email) {
        User user=userMapper.getByEmail(email);
        return user;
    }
}
