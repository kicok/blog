package com.nanum.blog.model;


import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("admin", 	"관리자"),
    USER("user", 		"일반회원"),
    ;

    private final String value;
    private final String message;

    RoleType(String value, String message) {
        this.value = value;
        this.message = message;
    }
}
