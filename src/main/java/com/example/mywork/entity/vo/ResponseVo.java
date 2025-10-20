package com.example.mywork.entity.vo;

import lombok.Data;

@Data
public class ResponseVo <T>{

    private Integer code;
    private String status;
    private String info;
    private T data;

    public static ResponseVo fail(String meg){
        ResponseVo vo = new ResponseVo();
        vo.setCode(200);
        vo.setInfo(meg);
        return vo;
    }

    public static ResponseVo ok(String meg){
        ResponseVo vo = new ResponseVo();
        vo.setCode(200);
        vo.setInfo(meg);
        return vo;
    }
    public static ResponseVo ok(String meg,Object data){
        ResponseVo vo = new ResponseVo();
        vo.setCode(200);
        vo.setInfo(meg);
        vo.setData(data);
        return vo;
    }

    public ResponseVo setData(T data) {
        this.data = data;
        return this;
    }

}
