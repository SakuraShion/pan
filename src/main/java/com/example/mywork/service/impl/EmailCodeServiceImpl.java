package com.example.mywork.service.impl;

import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.config.EmailConfig;
import com.example.mywork.dto.SysSettingDto;
import com.example.mywork.entity.EmailCode;
import com.example.mywork.entity.User;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.PageSize;
import com.example.mywork.entity.po.UserInfo;
import com.example.mywork.entity.query.EmailCodeQuery;
import com.example.mywork.entity.query.SimplePage;
import com.example.mywork.entity.query.UserInfoQuery;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.exception.BusinessException;
import com.example.mywork.mapper.EmailCodeMapper;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.EmailCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mywork.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hb
 * @since 2025-03-23
 */
@Service
public class EmailCodeServiceImpl  implements EmailCodeService {

    //调试日志
    private final static Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);



    @Resource
    private JavaMailSender mailSender;

    @Resource
    private EmailCodeMapper emailCodeMapper;

    @Resource
    private UserMapper userInfoMapper;

    @Resource
    private EmailConfig emailConfig;

    @Resource
    private RedisCommpont redisCommpont;

    @Override
    public List<EmailCode> findListByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectList(param);
    }

    @Override
    public Integer findCountByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectCount(param);
    }

    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
        Integer countByParam = this.findCountByParam(param);
        Integer pageSize=param.getPageSize()==null? PageSize.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), countByParam, pageSize);
        param.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(param);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(countByParam, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    @Override

    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) throws MessagingException {
        //如果是注册，检验邮箱是否存在
        if (type==Constants.ZERO){
            User userInfo=userInfoMapper.selectByEmail(email);
            if (null != userInfo) {
                throw new BusinessException("邮箱已经存在");
            }
        }
        String code = StringUtils.getRandomString(Constants.LENGTH);
        sendMailMessage(email, code);

        emailCodeMapper.disableEmailCode(email);
        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(LocalDateTime.now());
        emailCodeMapper.insert(emailCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkCode(String email, String code) {
        EmailCode emailCode = (EmailCode) emailCodeMapper.selectByEmailAndCode(email,code);
        if (emailCode.getStatus()==1&&System.currentTimeMillis()-emailCode.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()>Constants.LENGTH_15*60*1000) {}
        if (emailCode!=null && emailCode.getEmail().equals(email)) {
            return true;
        }
        return false;
    }

    @Override
    public Integer add(EmailCode bean) {
        if (bean==null){
            return 0;
        }
       return emailCodeMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean.isEmpty()||listBean==null) {
            return 0;
        }
        return emailCodeMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
        return (EmailCode) this.emailCodeMapper.selectByEmailAndCode(email, code);
    }

    @Override
    public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
    }

    @Override
    public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
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
