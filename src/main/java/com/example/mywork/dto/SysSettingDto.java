package com.example.mywork.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SysSettingDto implements Serializable {
    private String registerMail="邮箱验证码";

    private String registerEmailContent="你好，你的邮箱验证码是,%s,15分钟有效";

    private Integer userInitUseSpace=5;
}
