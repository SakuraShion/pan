package com.example.mywork.mapper;

import com.example.mywork.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
