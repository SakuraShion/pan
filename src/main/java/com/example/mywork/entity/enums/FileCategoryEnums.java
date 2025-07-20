package com.example.mywork.entity.enums;

import lombok.Getter;

public enum FileCategoryEnums {
    VIDEO(1,"Video","视频"),
    MUSIC(2,"music","音频"),
    IMAGE(3,"image","图片"),
    DOC(4,"doc","文档"),
    OTHERS(5,"other","其他");

    @Getter
    private Integer category;
    @Getter
    private String code;
    private String desc;

    FileCategoryEnums(Integer category, String code, String desc) {
        this.category = category;
        this.code = code;
        this.desc = desc;
    }

    public static FileCategoryEnums getFileCategory(String code) {
        for (FileCategoryEnums fileCategory : FileCategoryEnums.values()) {
            if (fileCategory.getCode().equals(code.toString())) {
                return fileCategory;
            }
        }
        return null;
    }

}
