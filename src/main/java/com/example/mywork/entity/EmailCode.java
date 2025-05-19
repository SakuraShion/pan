package com.example.mywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2025-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EmailCode对象", description="")
public class EmailCode extends Model<EmailCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "email", type = IdType.AUTO)
    private String email;

    private String code;

    private LocalDateTime createTime;

    private Integer status;


}
