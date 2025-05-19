package com.example.mywork.entity.constants;

import java.util.Objects;

public enum UserStatusEnum {
    DISABLE(0,"禁用"),
    ENABLE(1,"启用");

    private Integer status;

    private String desc;
    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public static UserStatusEnum getStatus(Integer status) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (Objects.equals(userStatusEnum.status, status)) {
                return userStatusEnum;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {}
}
