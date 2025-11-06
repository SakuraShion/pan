package com.example.mywork.task;

import com.example.mywork.entity.enums.FileDelFlagEnums;
import com.example.mywork.entity.query.FileInfoQuery;
import com.example.mywork.service.FileInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FileCleanTask {
    @Resource
    private FileInfoService fileInfoService;
    @Scheduled(fixedDelay = 1000 * 60 * 3)
    public void execute() {
        FileInfoQuery fileInfoQuery=new FileInfoQuery();
        fileInfoQuery.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
        fileInfoQuery.setQueryExpire(true);
        fileInfoService.findListByParam(fileInfoQuery);
    }
}
