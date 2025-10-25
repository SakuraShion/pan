package com.example.mywork.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SessionShareDto {

    private String shareId;

    private String shareUserId;

    private Date shareTime;

    private String fileId;
}
