package com.example.mywork.controller;

import com.example.mywork.dto.SessionShareDto;
import com.example.mywork.dto.SessionWebUserDto;
import com.example.mywork.entity.constants.Constants;
import com.example.mywork.entity.enums.ResponseCodeEnum;
import com.example.mywork.entity.vo.PaginationResultVO;
import com.example.mywork.entity.vo.ResponseVo;
import com.example.mywork.utils.CopyTools;
import com.example.mywork.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(ABaseController.class.getName());

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

    protected SessionWebUserDto getUserInfoFromSession(HttpSession session){
        return (SessionWebUserDto)session.getAttribute(Constants.SESSION_KEY);
    }

    protected SessionShareDto getSessionShareFromSession(HttpSession session, String shareId) {
        SessionShareDto sessionShareDto = (SessionShareDto) session.getAttribute(Constants.SESSION_SHARE_KEY + shareId);
        return sessionShareDto;
    }

    protected void readFile(HttpServletResponse response, String filePath) throws IOException {
        if (!StringUtils.pathIsOk(filePath)){
            return;
        }
        Path path = Paths.get(filePath).normalize();
        File file = path.toFile();
        OutputStream out = null;
        FileInputStream in = null;
        Files.copy(path, response.getOutputStream());
//        try {
//            File file=new File(filePath);
//            if (!file.exists()){
//                return;
//            }
//            in=new FileInputStream(file);
//            byte[] buffer=new byte[1024];
//            out=response.getOutputStream();
//            int len=0;
//            while((len=in.read(buffer))!=-1){
//                out.write(buffer,0,len);
//            }
//            out.flush();
//        }catch (Exception e){
//            logger.error("读取文件异常", e);
//        }finally {
//            if (out!=null){
//                try {
//                    out.close();
//                }catch (Exception e){
//                    logger.error("IO异常",e);
//                }
//            }
//            if (in!=null){
//                try {
//                 in.close();
//                }catch (Exception e){
//                    logger.error("IO异常",e);
//                }
//            }
//        }

    }

}
