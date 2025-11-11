package com.example.mywork.service;

import com.example.mywork.entity.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mywork.entity.query.EmailCodeQuery;
import com.example.mywork.entity.vo.PaginationResultVO;

import javax.mail.MessagingException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hb
 * @since 2025-03-23
 */
public interface EmailCodeService {
    /**
     * 根据条件查询列表
     */
    List<EmailCode> findListByParam(EmailCodeQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(EmailCodeQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param);



    void sendEmailCode(String email,Integer type) throws MessagingException;

    boolean checkCode(String email,String code);


    /**
     * 新增
     */
    Integer add(EmailCode bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<EmailCode> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<EmailCode> listBean);

    /**
     * 根据EmailAndCode查询对象
     */
    EmailCode getEmailCodeByEmailAndCode(String email, String code);


    /**
     * 根据EmailAndCode修改
     */
    Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code);


    /**
     * 根据EmailAndCode删除
     */
    Integer deleteEmailCodeByEmailAndCode(String email, String code);

}
