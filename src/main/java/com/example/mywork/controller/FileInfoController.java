package com.example.mywork.controller;

import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.entity.enums.FileCategoryEnums;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.service.FileInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("file")
public class FileInfoController {

    @Resource
    private FileInfoService fileInfoService;

    /**
     * 分页查询
     */
    @RequestMapping("loadDataList")
    @GlobalInterceptor
    public ResponseVo loadDataList(HttpSession session, String category,int pageOn) {
        FileCategoryEnums fileCategory= FileCategoryEnums.getFileCategory(category);
        if (null!=category){
            return ResponseVo.ok("成功").setData(fileInfoService.findListByPage(fileCategory.getCategory(),session.getAttribute("userId"),pageOn));
        }
        return ResponseVo.fail("失败");
    }
}
