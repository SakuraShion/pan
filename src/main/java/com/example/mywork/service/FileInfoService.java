package com.example.mywork.service;

import com.example.mywork.entity.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mywork.entity.vo.ResponseVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-06-30
 */
public interface FileInfoService extends IService<FileInfo> {

    ResponseVo findListByPage(Integer category, Object userId);
}
