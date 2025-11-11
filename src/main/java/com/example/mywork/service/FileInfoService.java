package com.example.mywork.service;

import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.dto.UploadResultDto;
import com.example.mywork.entity.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mywork.entity.query.FileInfoQuery;
import com.example.mywork.entity.vo.PaginationResultVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-06-30
 */
public interface FileInfoService  {



    PaginationResultVO<FileInfo> findListByPage(FileInfoQuery param);

    /**
     * 根据条件查询列表
     */
    List<FileInfo> findListByParam(FileInfoQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(FileInfoQuery param);

    /**
     * 新增
     */
    Integer add(FileInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<FileInfo> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<FileInfo> listBean);

    /**
     * 根据FileIdAndUserId查询对象
     */
    FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId);


    /**
     * 根据FileIdAndUserId修改
     */
    Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId);


    /**
     * 根据FileIdAndUserId删除
     */
    Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId);

    UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex,
                               Integer chunks);

    FileInfo rename(String fileId, String userId, String fileName);

    FileInfo newFolder(String filePid, String userId, String folderName);

    void changeFileFolder(String fileIds, String filePid, String userId);

    void removeFile2RecycleBatch(String userId, String fileIds);

    void recoverFileBatch(String userId, String fileIds);

    void delFileBatch(String userId, String fileIds, Boolean adminOp);

    void checkRootFilePid(String rootFilePid, String userId, String fileId);

    void saveShare(String shareRootFilePid, String shareFileIds, String myFolderId, String shareUserId, String cureentUserId);

    Long getUserUseSpace(@Param("userId") String userId);

    void deleteFileByUserId(@Param("userId") String userId);

}
