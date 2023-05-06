package ru.team2.lookingforhouse.util;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {

    public static final String DOG_BUTTON = "DOG_BUTTON";
    public static final String CAT_BUTTON = "CAT_BUTTON";
    public static final String ADDRESS_DOG_BUTTON = "ADDRESS_DOG_BUTTON";
    public static final String ADDRESS_CAT_BUTTON = "ADDRESS_CAT_BUTTON";
    public static final String DRIVING_DIRECTIONS_DOG_BUTTON = "DRIVING_DIRECTIONS_DOG_BUTTON";
    public static final String DRIVING_DIRECTIONS_DOG_LINK = "Схема проезда по ссылке:\nhttps://clck.ru/33rhcp";
    public static final String DRIVING_DIRECTIONS_CAT_LINK = "Схема проезда по ссылке: \nhttps://clck.ru/33rhma";
    public static final String DRIVING_DIRECTIONS_CAT_BUTTON = "DRIVING_DIRECTIONS_CAT_BUTTON";
    public static final String SAFETY_PRECAUTIONS_DOG_BUTTON = "SAFETY_PRECAUTIONS_DOG_BUTTON";
    public static final String SAFETY_PRECAUTIONS_CAT_BUTTON = "SAFETY_PRECAUTIONS_CAT_BUTTON";
    public static final String CALL_VOLUNTEER_BUTTON = "CALL_VOLUNTEER_BUTTON";
    public static final String INFO_DOG_BUTTON = "INFO_DOG_BUTTON";
    public static final String INFO_CAT_BUTTON = "INFO_CAT_BUTTON";
    public static final String TO_ADOPT_DOG_BUTTON = "TO_ADOPT_DOG_BUTTON";
    public static final String TO_ADOPT_CAT_BUTTON = "TO_ADOPT_CAT_BUTTON";
    public static final String SUBMIT_REPORT_DOG_BUTTON = "SUBMIT_REPORT_DOG_BUTTON";
    public static final String SUBMIT_REPORT_CAT_BUTTON = "SUBMIT_REPORT_CAT_BUTTON";
    public static final String RULES_DOG_BUTTON = "RULES_DOG_BUTTON";
    public static final String RULES_CAT_BUTTON = "RULES_CAT_BUTTON";
    public static final String DOC_DOG_BUTTON = "DOC_DOG_BUTTON";
    public static final String DOC_CAT_BUTTON = "DOC_CAT_BUTTON";
    public static final String TRANSPORTATION_DOG_BUTTON = "TRANSPORTATION_DOG_BUTTON";
    public static final String TRANSPORTATION_CAT_BUTTON = "TRANSPORTATION_CAT_BUTTON";
    public static final String RECOMMENDATION_PUPPY_BUTTON = "RECOMMENDATION_PUPPY_BUTTON";
    public static final String RECOMMENDATION_KITTY_BUTTON = "RECOMMENDATION_KITTY_BUTTON";
    public static final String RECOMMENDATION_CAT_BUTTON = "RECOMMENDATION_CAT_BUTTON";
    public static final String RECOMMENDATION_DOG_BUTTON = "RECOMMENDATION_DOG_BUTTON";
    public static final String REC_HANDICAPPED_DOG_BUTTON = "REC_HANDICAPPED_DOG_BUTTON";
    public static final String REC_HANDICAPPED_CAT_BUTTON = "REC_HANDICAPPED_CAT_BUTTON";
    public static final String ADVICES_CYNOLOGISTS_BUTTON = "ADVICES_CYNOLOGISTS_BUTTON";
    public static final String CYNOLOGISTS_BUTTON = "CYNOLOGISTS_BUTTON";
    public static final String VET_CENTER_BUTTON = "VET_CENTER_BUTTON";
    public static final String REASONS_FOR_REFUSAL_BUTTON = "REASONS_FOR_REFUSAL_BUTTON";
    public static final String SAVE_CONTACT_BUTTON = "SAVE_CONTACT_BUTTON";


    public static final String REGEX_MESSAGE_DOG = """
            (Отчёт для приюта собак:
            Рацион:)(\\s)(\\W+)(;)
            (Самочувствие:)(\\s)(\\W+)(;)
            (Поведение:)(\\s)(\\W+)(;)""";

    public static final String REGEX_MESSAGE_CAT = """
            (Отчёт для приюта кошек:
            Рацион:)(\\s)(\\W+)(;)
            (Самочувствие:)(\\s)(\\W+)(;)
            (Поведение:)(\\s)(\\W+)(;)""";



}
