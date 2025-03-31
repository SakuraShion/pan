package com.example.mywork.controller;


import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.annotation.VerfiyParam;
import com.example.mywork.dto.CreateImageCode;
import com.example.mywork.entity.EmailCode;
import com.example.mywork.entity.User;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.VerifyRegexEnum;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.service.EmailCodeService;
import com.example.mywork.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hb
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/userInfo")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailCodeService emailCodeService;

    @GetMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session,Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String code = vCode.getCode();
        if(type==null||type==0){
            session.setAttribute(Constants.CHECK_CODE_KEY,code);
        }else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL,code);
        }
        vCode.write(response.getOutputStream());
    }

    @GlobalInterceptor
    @GetMapping("/register")
    private ResponseVo register(HttpSession httpSession,
                               @VerfiyParam(required = true,regex = VerifyRegexEnum.EMALL) String email,
                               @VerfiyParam(required = true) String checkcode,
                               @VerfiyParam(required = true) String type,
                               @VerfiyParam(required = true) String nickName,
                                @VerfiyParam(required = true,regex = VerifyRegexEnum.PASSWORD) String password){
        try {
            if (!checkcode.equalsIgnoreCase((String) httpSession.getAttribute(Constants.CHECK_CODE_KEY))){
                return ResponseVo.fail("验证码不正确");
            }else {

            }
        }finally {
            httpSession.removeAttribute(Constants.CHECK_CODE_KEY);
        }
        return ResponseVo.ok("注册成功");

    }

    @GetMapping("/sendEmailCode")
    @GlobalInterceptor(checkParasm = true)
    public ResponseVo sendEmailCode(HttpSession session,String email,String checkCode,Integer type) throws IOException, MessagingException {
        if (session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL)!=null&&!checkCode.equalsIgnoreCase(String.valueOf(session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL)))){
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
            return ResponseVo.fail("验证码不正确");
        }
        if (type==0) {
            User byEamil = userService.getByEamil(email);
            if (byEamil != null) {
                return ResponseVo.fail("邮箱已存在");
            }
        }
        emailCodeService.sendEmailCode(email,type);
        return ResponseVo.ok("发送成功");
    }


}

