package com.example.mywork.controller;

import com.example.mywork.entity.enums.ResponseCodeEnum;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.utils.CopyTools;

import java.util.logging.Logger;

public class ABaseController {

    private static final Logger log = Logger.getLogger(ABaseController.class.getName());

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    protected <T> ResponseVo getSuccessResponseVO(T data) {
        ResponseVo<T> responseVo = new ResponseVo();
        responseVo.setData(data);
        responseVo.setStatus(STATUC_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        return responseVo;
    }

    public <S, T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result,Class<T> clazz) {
        PaginationResultVO<T> paginationResultVO = new PaginationResultVO<>();
        paginationResultVO.setList(CopyTools.copyList(result.getList(), clazz));
        paginationResultVO.setPageNo(result.getPageNo());
        paginationResultVO.setPageSize(result.getPageSize());
        paginationResultVO.setPageTotal(result.getPageTotal());
        paginationResultVO.setTotalCount(result.getTotalCount());
        return paginationResultVO;
    }

}
