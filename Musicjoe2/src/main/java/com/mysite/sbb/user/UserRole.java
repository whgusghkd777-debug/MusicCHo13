package com.mysite.sbb.user;

import lombok.Getter;

@Getter
public enum UserRole {
    /* ADMIN という名前で定義し、内部値を ROLE_ADMIN に設定 */
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}