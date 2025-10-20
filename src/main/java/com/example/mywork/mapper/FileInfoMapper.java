package com.example.mywork.mapper;

import com.example.mywork.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mywork.entity.query.SimplePage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hb
 * @since 2025-06-30
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    FileInfo selectUseSpaceByUserId(String userId);

    List<FileInfo> selectList(@Param("query") SimplePage p);
}
