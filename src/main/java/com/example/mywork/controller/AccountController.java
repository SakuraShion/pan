package com.example.mywork.controller;

import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.annotation.VerfiyParam;
import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.dto.CreateImageCode;
import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.entity.config.AppConfig;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.VerifyRegexEnum;
import com.example.mywork.service.EmailCodeService;
import com.example.mywork.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mywork.entity.vo.ResponseVo;
import javax.annotation.Resource;
import com.example.mywork.exception.BusinessException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController("accountController")
public class AccountController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    @Resource
    private UserService userInfoService;

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisCommpont redisCommpont;

    /**
     * 验证码
     *
     * @param response
     * @param session
     * @param type
     * @throws IOException
     */
    @RequestMapping(value = "/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }

    /**
     * @Description: 发送邮箱验证码
     * @auther: laoluo
     * @date: 20:39 2023/4/1
     * @param: [session, email, checkCode, type]
     * @return: com.easypan.entity.vo.ResponseVO
     */
    @RequestMapping("/sendEmailCode")
    @GlobalInterceptor(checkLogin = false, checkParasm = true)
    public ResponseVo sendEmailCode(HttpSession session,
                                    @VerfiyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                                    @VerfiyParam(required = true) String checkCode,
                                    @VerfiyParam(required = true) Integer type) throws MessagingException {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException("图片验证码不正确");
            }
            emailCodeService.sendEmailCode(email, type);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }
    }


    /**
     * @Description: 注册
     * @auther: laoluo
     * @date: 20:39 2023/4/1
     * @param: [session, email, nickName, password, checkCode, emailCode]
     * @return: com.easypan.entity.vo.ResponseVO
     */
    @RequestMapping("/register")
    @GlobalInterceptor(checkLogin = false, checkParasm = true)
    public ResponseVo register(HttpSession session,
                               @VerfiyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                               @VerfiyParam(required = true, max = 20) String nickName,
                               @VerfiyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password,
                               @VerfiyParam(required = true) String checkCode,
                               @VerfiyParam(required = true) String emailCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("图片验证码不正确");
            }
            userInfoService.register(email, nickName, password, emailCode);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * @Description: 登录
     * @auther: laoluo
     * @date: 20:39 2023/4/1
     * @param: [session, request, email, password, checkCode]
     * @return: com.easypan.entity.vo.ResponseVO
     */
    @RequestMapping("/login")
    @GlobalInterceptor(checkLogin = false, checkParasm = true)
    public ResponseVo login(HttpSession session, HttpServletRequest request,
                            @VerfiyParam(required = true) String email,
                            @VerfiyParam(required = true) String password,
                            @VerfiyParam(required = true) String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("图片验证码不正确");
            }
            SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password);
            session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
            return getSuccessResponseVO(sessionWebUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @RequestMapping("/resetPwd")
    @GlobalInterceptor(checkLogin = false, checkParasm = true)
    public ResponseVo resetPwd(HttpSession session,
                               @VerfiyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                               @VerfiyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password,
                               @VerfiyParam(required = true) String checkCode,
                               @VerfiyParam(required = true) String emailCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("图片验证码不正确");
            }
            userInfoService.resetPwd(email, password, emailCode);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

}
