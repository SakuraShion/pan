package com.example.mywork.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSpaceDto implements Serializable {
    private Long userSpace;

    private Long totalSpace;
}
