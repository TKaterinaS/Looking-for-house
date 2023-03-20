package ru.team2.lookingforhouse.util;

import javax.persistence.AttributeConverter;

public class StatusAttributeConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus userStatus) {
        if (userStatus == null)
            return null;
        return switch (userStatus) {
            case JUST_USER -> "простой пользователь";
            case GREAT_ADOPTER -> "великий усыновитель";
        };
    }

    @Override
    public UserStatus convertToEntityAttribute(String status) {
        if (status == null)
            return null;
        return switch (status) {
            case "простой пользователь" -> UserStatus.JUST_USER;
            case "великий усыновитель" -> UserStatus.GREAT_ADOPTER;
            default -> throw new IllegalArgumentException(status + " не поддерживается.");
        };
    }
}
