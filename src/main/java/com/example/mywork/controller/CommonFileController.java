package com.example.mywork.controller;

import com.example.mywork.commpone.RedisCommpont;
import com.example.mywork.entity.config.AppConfig;
import com.example.mywork.service.FileInfoService;

import javax.annotation.Resource;

public class CommonFileController extends ABaseController {

    @Resource
    protected FileInfoService fileInfoService;

    @Resource
    protected AppConfig appConfig;

    @Resource
    private RedisCommpont redisComponent;

}
