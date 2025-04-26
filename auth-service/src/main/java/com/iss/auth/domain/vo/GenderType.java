package com.iss.auth.domain.vo;

public enum GenderType {
    Male,
    Female;

    public static GenderType fromOrdinal(int ordinal) {
        GenderType[] values = GenderType.values();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IllegalArgumentException("Invalid GenderType ordinal: " + ordinal);
        }
        return values[ordinal];
    }

    // UserType -> int
    public int toOrdinal() {
        return this.ordinal();
    }
}
