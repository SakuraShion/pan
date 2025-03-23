package com.example.mywork.mapper;

import com.example.mywork.entity.EmailCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hb
 * @since 2025-03-23
 */
public interface EmailCodeMapper extends BaseMapper<EmailCode> {

    void disableEmailCode(@Param("email")String email);
}
