package com.example.mywork.exception;

import com.example.mywork.entity.enums.ResponseCodeEnum;

public class BussionException extends RuntimeException {
    public BussionException(String message,Throwable e) {
        super(message,e);
        this.message = message;
    }

    public BussionException(String message) {
        super(message);
        this.message = message;
    }
    private ResponseCodeEnum responseCodeEnum;

    private String message;

    private Integer code;
    public BussionException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.responseCodeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public BussionException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ResponseCodeEnum getCodeEnum() {
        return responseCodeEnum;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 重写fillInStackTrace 业务异常不需要堆栈信息，提高效率.
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
