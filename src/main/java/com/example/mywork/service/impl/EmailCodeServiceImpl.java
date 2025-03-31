package com.example.mywork.service.impl;

import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.config.EmailConfig;
import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.entity.EmailCode;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.mapper.EmailCodeMapper;
import com.example.mywork.service.EmailCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mywork.service.UserService;
import com.example.mywork.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hb
 * @since 2025-03-23
 */
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode> implements EmailCodeService {

    //调试日志
    private final static Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);


    @Autowired
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private EmailConfig emailConfig;

    @Resource
    private RedisCommpont redisCommpont;

    @Override

    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) throws MessagingException {
        /**
         * status 0 ==注册 1==找回密码
         */
        String code= StringUtils.getRandomString(Constants.LENGTH);
        //发送邮箱
  //      sendMailMessage(code,email);
        //验证码置为无效
        this.baseMapper.disableEmailCode(email);
        EmailCode emailCode=new EmailCode();
        emailCode.setEmail(email);
        emailCode.setCode(code);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(LocalDateTime.now());

        this.baseMapper.insert(emailCode);
    }

    private void sendMailMessage(String code,String email) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(email);
            SysSettingDto sysSettingDto = redisCommpont.getsysSettingDto();
            helper.setSubject(sysSettingDto.getRegisterMail());//标题
            helper.setText(String.format(sysSettingDto.getRegisterEmailContent(),code));//文本
            helper.setSentDate(new Date());
            mailSender.send(mimeMessage);
        }catch (Exception e){
            logger.error("邮件发送失败",e);
        }
    }
}
