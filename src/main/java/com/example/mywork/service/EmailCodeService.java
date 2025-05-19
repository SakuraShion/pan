package com.example.mywork.service;

import com.example.mywork.entity.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.mail.MessagingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-03-23
 */
public interface EmailCodeService extends IService<EmailCode> {

    void sendEmailCode(String email,Integer type) throws MessagingException;

    boolean checkCode(String email,String code);

}
