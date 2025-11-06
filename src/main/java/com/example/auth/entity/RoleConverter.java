package com.example.auth.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    // Chuyển từ Enum (Entity) sang Integer (Database Column)
    @Override
    public Integer convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.getValue(); // Trả về 1 cho ADMIN, 2 cho USER
    }

    // Chuyển từ Integer (Database Column) sang Enum (Entity)
    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Role.fromValue(dbData); // Trả về Role tương ứng (ADMIN hoặc USER)
    }
}