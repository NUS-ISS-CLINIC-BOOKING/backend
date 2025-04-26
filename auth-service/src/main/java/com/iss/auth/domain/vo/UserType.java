package com.iss.auth.domain.vo;

public enum UserType {
    PATIENT,
    DOCTOR,
    ADMIN,
    CLINIC_STAFF;

    // int -> UserType
    public static UserType fromOrdinal(int ordinal) {
        UserType[] values = UserType.values();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IllegalArgumentException("Invalid UserType ordinal: " + ordinal);
        }
        return values[ordinal];
    }

    // UserType -> int
    public int toOrdinal() {
        return this.ordinal();
    }
}
