package com.example.mywork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mywork.dto.SessionShareDto;
import com.example.mywork.entity.User;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.PageSize;
import com.example.mywork.entity.enums.ResponseCodeEnum;
import com.example.mywork.entity.enums.ShareValidTypeEnums;
import com.example.mywork.entity.po.FileShare;
import com.example.mywork.entity.query.FileShareQuery;
import com.example.mywork.entity.query.SimplePage;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.exception.BusinessException;
import com.example.mywork.mapper.FileShareMapper;
import com.example.mywork.mapper.UserMapper;
import com.example.mywork.service.FileShareService;
import com.example.mywork.utils.DateUtil;
import com.example.mywork.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import sun.util.calendar.LocalGregorianCalendar;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 分享信息 业务接口实现
 */

public class FileShareServiceImpl extends ServiceImpl<FileShareMapper, FileShare> implements FileShareService {

    @Resource
    private FileShareMapper fileShareMapper;


    @Override
    public List<FileShare> findListByParam(FileShareQuery param) {
        return this.fileShareMapper.selectList(param);
    }

    @Override
    public Integer findCountByParam(FileShareQuery param) {
        return fileShareMapper.selectCount(param);
    }

    @Override
    public PaginationResultVO<FileShare> findListByPage(FileShareQuery param) {
        Integer i = fileShareMapper.selectCount(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();
        SimplePage simplePage = new SimplePage(param.getPageNo(), i,pageSize);
        param.setSimplePage(simplePage);
        List<FileShare> list = this.findListByParam(param);
        PaginationResultVO<FileShare> paginationResultVO = new PaginationResultVO<>(i, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
        return paginationResultVO;
    }

    @Override
    public Integer add(FileShare bean) {
        return this.fileShareMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<FileShare> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileShareMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<FileShare> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileShareMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public FileShare getFileShareByShareId(String shareId) {
        return this.fileShareMapper.selectByShareId(shareId);
    }

    @Override
    public Integer updateFileShareByShareId(FileShare bean, String shareId) {
        return this.fileShareMapper.updateByShareId(bean, shareId);
    }

    @Override
    public Integer deleteFileShareByShareId(String shareId) {
        return this.fileShareMapper.deleteByShareId(shareId);
    }

    @Override
    public void saveShare(FileShare share) {
        ShareValidTypeEnums typeEnum = ShareValidTypeEnums.getEnum(share.getValidType());
        if (typeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (typeEnum!=ShareValidTypeEnums.FOREVER){
            share.setExpireTime(DateUtil.getAfterDate(typeEnum.getDays()));
        }
        Date curDate=new Date();
        share.setShareTime(curDate);
        if (StringUtils.isEmpty(share.getCode())) {
            share.setCode(StringUtils.getRandomString(Constants.LENGTH));
        }
        share.setShareId(StringUtils.getRandomString(Constants.LENGTH_20));
        fileShareMapper.insert(share);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileShareBatch(String[] shareIdArray, String userId) {
        Integer count=fileShareMapper.deleteFileShareBatch(shareIdArray, userId);
        if (count!=shareIdArray.length) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    @Override
    public SessionShareDto checkShareCode(String shareId, String code) {
        FileShare share = this.fileShareMapper.selectByShareId(shareId);
        if (share==null||(share.getExpireTime()!=null&&share.getExpireTime().getTime()<new Date().getTime())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902);
        }
        if (!share.getCode().equals(code)){
            throw new BusinessException("提取码错误");
        }

        this.fileShareMapper.updateShareShowCount(shareId);
        SessionShareDto sessionShareDto = new SessionShareDto();
        sessionShareDto.setShareId(shareId);
        sessionShareDto.setShareUserId(share.getUserId());
        sessionShareDto.setFileId(share.getFileId());
        sessionShareDto.setShareTime(share.getShareTime());
        return sessionShareDto;
    }
}
