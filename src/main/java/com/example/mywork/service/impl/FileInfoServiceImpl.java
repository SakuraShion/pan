package com.example.mywork.service.impl;

import com.example.mywork.entity.FileInfo;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.mapper.FileInfoMapper;
import com.example.mywork.service.FileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hb
 * @since 2025-06-30
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Override
    public ResponseVo findListByPage(Integer category, Object userId) {
        return null;
    }
}
