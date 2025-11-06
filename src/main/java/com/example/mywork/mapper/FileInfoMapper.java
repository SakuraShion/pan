package com.example.mywork.mapper;

import com.example.mywork.entity.FileInfo;
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
public interface FileInfoMapper<T, P> extends BaseMapper<T, P> {

    FileInfo selectUseSpaceByUserId(String userId);

    List<FileInfo> selectList(@Param("query") SimplePage p);

    /**
     * 根据FileIdAndUserId更新
     */
    Integer updateByFileIdAndUserId(@Param("bean") T t,@Param("FileId")String fileId,@Param("userId")String userId);

    /**
     * 根据FileIdAndUserId删除
     */
    Integer deleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

    /**
     * 根据FileIdAndUserId获取对象
     */
    T selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

    Integer updateFileStatusWithOldStatus(@Param("fileId") String fileId, @Param("userId") String userId, @Param("bean") T t,
                                       @Param("oldStatus") Integer oldStatus);
}
