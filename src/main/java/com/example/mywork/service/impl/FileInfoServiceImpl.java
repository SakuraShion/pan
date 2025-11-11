package com.example.mywork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.dto.UploadResultDto;
import com.example.mywork.entity.FileInfo;
import com.example.mywork.entity.config.AppConfig;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.DateTimePatternEnum;
import com.example.mywork.entity.enums.FileStatusEnums;
import com.example.mywork.entity.enums.FileTypeEnums;
import com.example.mywork.entity.enums.PageSize;
import com.example.mywork.entity.query.FileInfoQuery;
import com.example.mywork.entity.query.SimplePage;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.exception.BusinessException;
import com.example.mywork.mapper.FileInfoMapper;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.FileInfoService;
import com.example.mywork.utils.DateUtil;
import com.example.mywork.utils.ScaleFilter;
import com.example.mywork.utils.StringUtils;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.ZoneId;
import java.util.Date;
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
public class FileInfoServiceImpl implements FileInfoService {
    private static final Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    @Resource
    @Lazy
    private FileInfoServiceImpl fileInfoService;

    @Resource
    private AppConfig appConfig;


    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    @Resource
    private UserMapper  userInfoMapper;

    @Resource
    private RedisCommpont redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(FileInfoQuery param) {
        return this.fileInfoMapper.selectCount(param);
    }

    @Override
    public Integer add(FileInfo bean) {
        return this.fileInfoMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<FileInfo> listBean) {
        return 0;
    }

    @Override
    public Integer addOrUpdateBatch(List<FileInfo> listBean) {
        return 0;
    }

    @Override
    public FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId) {
        return null;
    }

    @Override
    public Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId) {
        return 0;
    }

    @Override
    public Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId) {
        return 0;
    }

    @Override
    public UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks) {
        return null;
    }


    @Override
    public FileInfo rename(String fileId, String userId, String fileName) {
        return null;
    }

    @Override
    public FileInfo newFolder(String filePid, String userId, String folderName) {
        return null;
    }

    @Override
    public void changeFileFolder(String fileIds, String filePid, String userId) {

    }

    @Override
    public void removeFile2RecycleBatch(String userId, String fileIds) {

    }

    @Override
    public void recoverFileBatch(String userId, String fileIds) {

    }

    @Override
    public void delFileBatch(String userId, String fileIds, Boolean adminOp) {

    }

    @Override
    public void checkRootFilePid(String rootFilePid, String userId, String fileId) {

    }

    @Override
    public void saveShare(String shareRootFilePid, String shareFileIds, String myFolderId, String shareUserId, String cureentUserId) {

    }

    @Override
    public Long getUserUseSpace(String userId) {
        return 0L;
    }

    @Override
    public void deleteFileByUserId(String userId) {

    }
    @Async("transerExecutor")
    public void transferFile(String fileId, SessionWebUserDto webUserDto) {
        String cover=null;
        String targetFilePath=null;
        Boolean transferSuccess=true;
        FileInfo fileInfo = fileInfoMapper.selectByFileIdAndUserId(fileId, webUserDto.getUserId());
        try {
            if (fileInfo == null || !FileStatusEnums.TRANSFER.getStatus().equals(fileInfo.getStatus())) {
                return;
            }
            //临时目录
            String tempFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP;
            String currentUserFolderName = webUserDto.getUserId() + fileId;
            File fileFolder = new File(tempFolderName + currentUserFolderName);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
            //文件后缀
            String fileSuffix = StringUtils.getFileSuffix(fileInfo.getFileName());
            String month = DateUtil.format(Date.from(fileInfo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()), DateTimePatternEnum.YYYYMM.getPattern());
            //目标目录
            String targetFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
            File targetFolder = new File(targetFolderName + "/" + month);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            //真实文件名
            String realFileName = currentUserFolderName + fileSuffix;
            //真实文件路径
            targetFilePath = targetFolder.getPath() + "/" + realFileName;
            union(fileFolder.getPath(), targetFilePath, fileInfo.getFileName(), true);
            //根据后缀进行切割
            FileTypeEnums fileTypeEnum = FileTypeEnums.getFileTypeBySuffix(fileSuffix);
            if (fileTypeEnum == FileTypeEnums.VIDEO) {
                //视频生成缩略图
                cover = month + "/" + currentUserFolderName + Constants.IMAGE_PNG_SUFFIX;
                String coverPath = targetFilePath + "/" + cover;
                ScaleFilter.createCover4Video(new File(targetFilePath), Constants.LENGTH_150, new File(coverPath));
            } else if (FileTypeEnums.IMAGE == fileTypeEnum) {
                //生成缩略图
                cover = month + "/" + realFileName.replace(".", "_.");
                String coverPath = targetFolderName + "/" + cover;
                Boolean created = ScaleFilter.createThumbnailWidthFFmpeg(new File(targetFilePath), Constants.LENGTH_150, new File(coverPath), false);
                if (!created) {
                    FileUtil.copyFile(new File(targetFilePath), new File(coverPath));
                }
            }
        }catch (Exception e){
            logger.error("文件转码失败，文件Id:{},userId:{}", fileId, webUserDto.getUserId(), e);
            transferSuccess = false;
        }finally {
            FileInfo updateInfo = new FileInfo();
            updateInfo.setFileSize(new File(targetFilePath).length());
            updateInfo.setFileCover(cover);
            updateInfo.setStatus(transferSuccess ? FileStatusEnums.USING.getStatus() : FileStatusEnums.TRANSFER_FAIL.getStatus());
            fileInfoMapper.updateFileStatusWithOldStatus(fileId, webUserDto.getUserId(), updateInfo, FileStatusEnums.TRANSFER.getStatus());
        }

    }

    public static void union(String dirPath, String toFilePath, String fileName, boolean delSource) throws BusinessException {

    }

    @Override
    public PaginationResultVO<FileInfo> findListByPage(FileInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<FileInfo> list = this.findListByParam(param);
        PaginationResultVO<FileInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    @Override
    public List<FileInfo> findListByParam(FileInfoQuery fileInfoQuery) {
        return fileInfoMapper.selectList(fileInfoQuery);
    }
}
