package com.example.mywork.mapper;

import com.example.mywork.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mywork.entity.po.UserInfo;
import org.apache.ibatis.annotations.Param;

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

    Integer updateByEmail(UserInfo userInfo, String email);

    /**
     * 根据Email删除
     */
    Integer deleteByEmail(@Param("email") String email);



    /**
     * 根据NickName更新
     */
    Integer updateByNickName(@Param("bean")  User user, @Param("nickName") String nickName);


    /**
     * 根据NickName删除
     */
    Integer deleteByNickName(@Param("nickName") String nickName);




    /**
     * 根据QqOpenId更新
     */
    Integer updateByQqOpenId(@Param("bean") User user, @Param("qqOpenId") String qqOpenId);



    /**
     * 根据QqOpenId删除
     */
    Integer deleteByQqOpenId(@Param("qqOpenId") String qqOpenId);


    /**
     * 根据QqOpenId获取对象
     */
    User selectByQqOpenId(@Param("qqOpenId") String qqOpenId);


    Integer updateUserSpace(@Param("userId") String userId, @Param("useSpace") Long useSpace, @Param("totalSpace") Long totalSpace);

    Integer updateByUserId(@Param("bean")UserInfo userInfo, @Param("userId")String userId);
}
