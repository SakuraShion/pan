package com.example.mywork.service;

import com.example.mywork.entity.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mywork.entity.vo.PaginationResultVO;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-06-30
 */
public interface FileInfoService extends IService<FileInfo> {

    PaginationResultVO<FileInfo> findListByPage(Integer category, Object userId, Integer pageOn);
}
