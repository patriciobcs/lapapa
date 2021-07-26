package com.lapapa.app.chatBotForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_RUT_REGEX =
            Pattern.compile("^(\\d{1,3}(?:\\d{1,3}){2}-[\\dkK])$", Pattern.CASE_INSENSITIVE);

    public static boolean validatePattern(String emailStr, Pattern pattern) {
        Matcher matcher = pattern.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validate(String value, String type) {
        boolean valid = true;
        switch (type) {
            case "mail":
                valid = validatePattern(value, VALID_EMAIL_ADDRESS_REGEX);
                break;
            case "rut":
                valid = validatePattern(value, VALID_RUT_REGEX);
                break;
            case "age":
                valid = value.length() > 0 && value.length() < 3;
                break;
            default:
                valid = value.length() > 0;
                break;
        }
        return valid;
    }
}
