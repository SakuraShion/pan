package com.example.mywork.mapper;

import com.example.mywork.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mywork.entity.po.UserInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hb
 * @since 2025-03-17
 */
public interface UserMapper extends BaseMapper<User> {

    User getByEmail(String email);

    User selectByNickName(String nickName);

    User selectByEmail(String email);

    Integer setPassWord(String email, String s);

    UserInfo selectByUserId(String userId);
}
