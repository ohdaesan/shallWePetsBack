package com.ohdaesan.shallwepets.member.domain.entity;

public enum RoleType {
//    USER, ADMIN

    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
