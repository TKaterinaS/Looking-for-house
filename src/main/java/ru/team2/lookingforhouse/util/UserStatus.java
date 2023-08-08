package ru.team2.lookingforhouse.util;

public enum UserStatus {
    JUST_USER("простой пользователь"),
    GREAT_ADOPTER("великий усыновитель");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
