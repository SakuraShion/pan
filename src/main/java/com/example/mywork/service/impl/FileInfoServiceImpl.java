package com.example.mywork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mywork.entity.FileInfo;
import com.example.mywork.entity.query.FileInfoQuery;
import com.example.mywork.entity.query.SimplePage;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.mapper.FileInfoMapper;
import com.example.mywork.service.FileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public PaginationResultVO<FileInfo> findListByPage(Integer category, Object userId, Integer pageOn) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        Long count = fileInfoMapper.selectCount(queryWrapper);
        int pageSize=15;
        SimplePage simplePage=new SimplePage(pageOn, Math.toIntExact(count),pageSize);
        List<FileInfo> result = fileInfoMapper.selectList(simplePage);
        PaginationResultVO<FileInfo> ans = new PaginationResultVO(Math.toIntExact(count), simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), result);
        return ans;
    }

    @Override
    public List<FileInfo> findListByParam(FileInfoQuery fileInfoQuery) {
        return fileInfoMapper.selectList(fileInfoQuery);
    }
}
