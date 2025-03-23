package com.example.mywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hb
 * @since 2025-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
      @TableId(value = "user_id", type = IdType.AUTO)
    private String userId;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "别名")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "qq第三方登录id")
    private String qqOpenId;

    @ApiModelProperty(value = "qq头像")
    private String qqAvater;

    @ApiModelProperty(value = "注册时间")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "最后活跃时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "使用空间单位 byte")
    private Long useSpace;

    @ApiModelProperty(value = "总空间")
    private Long totalSpace;



}
