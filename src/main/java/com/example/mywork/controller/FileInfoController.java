package com.example.mywork.controller;

import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.entity.enums.FileCategoryEnums;
import com.example.mywork.entity.enums.ResponseCodeEnum;
import com.example.mywork.entity.query.FileInfoQuery;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("file")
public class FileInfoController extends CommonFileController {

    @Resource
    private FileInfoService fileInfoService;


    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVo loadDataList(HttpSession session, FileInfoQuery query, String category) {
        FileCategoryEnums categoryEnum = FileCategoryEnums.getByCode(category);
        if (null != categoryEnum) {
            query.setFileCategory(categoryEnum.getCategory());
        }
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
    }
}
