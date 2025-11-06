package com.example.auth.entity;

public enum Role {
    ADMIN(1),
    USER(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Phương thức tiện ích để chuyển đổi từ số nguyên sang Role
    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Giá trị Role không hợp lệ: " + value);
    }
}