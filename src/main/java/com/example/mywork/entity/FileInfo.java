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
 * @since 2025-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FileInfo对象", description="")
public class FileInfo extends Model<FileInfo> {

    private static final long serialVersionUID = 1L;

      @TableId(value = "file_id", type = IdType.AUTO)
    private String fileId;

    private String userId;

    private String fileMd5;

    private String filePid;

    private Long fileSize;

    private String fileName;

    private String fileCover;

    private String filePath;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdateTime;

    private Integer folderType;

    private Integer fileCategory;

    private Integer fileType;

    private Integer status;

    private LocalDateTime recoveryTime;

    private Integer delFlag;


    @Override
    public Serializable pkVal() {
        return this.fileId;
    }

}
