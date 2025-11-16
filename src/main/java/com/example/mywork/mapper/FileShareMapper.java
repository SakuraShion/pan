package com.example.mywork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mywork.entity.FileInfo;
import com.example.mywork.entity.User;
import com.example.mywork.entity.po.FileShare;
import com.example.mywork.entity.query.FileShareQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileShareMapper extends BaseMapper<FileShare> {

    /**
     * 根据ShareId更新
     */
    Integer updateByShareId(@Param("bean") FileShare t, @Param("shareId") String shareId);


    /**
     * 根据ShareId删除
     */
    Integer deleteByShareId(@Param("shareId") String shareId);


    /**
     * 根据ShareId获取对象
     */
    FileShare selectByShareId(@Param("shareId") String shareId);

    Integer deleteFileShareBatch(@Param("shareIdArray") String[] shareIdArray, @Param("userId") String userId);

    void updateShareShowCount(@Param("shareId") String shareId);

    List<FileShare> selectList (@Param("query") FileShareQuery share);

    Integer selectCount(@Param("query") FileShareQuery fileShareQuery);

    Integer insertBatch(@Param("list")List<FileShare> list);

    Integer insertOrUpdateBatch(@Param("list") List<FileShare> list);
}
