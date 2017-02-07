package com.vpage.vcars.tools.utils;



import com.vpage.vcars.pojos.ValidationStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {


    private static final String TAG = ValidationUtils.class.getName();

    static public ValidationStatus isValidLoginUserNamePassword(String userPhoneNumber, String userName, String password) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setStatus(true);
        validationStatus.setMessage("");

        if (null == userName || null == password ||null == userPhoneNumber|| userName.length() == 0 || password.length() == 0||userPhoneNumber.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid phoneNumber, username and password");
            return validationStatus;
        }

        if (null == userPhoneNumber || userPhoneNumber.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid phone number");
            return validationStatus;
        }

        if (null == userName || userName.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid username");
            return validationStatus;
        }

        if (null == password || password.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid password");
            return validationStatus;
        }

        return validationStatus;
    }


    static public ValidationStatus isValidLoginUserNamePassword( String userName, String password) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setStatus(true);
        validationStatus.setMessage("");

        if (null == userName || null == password || userName.length() == 0 || password.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid username and password");
            return validationStatus;
        }


        if (null == userName || userName.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid username");
            return validationStatus;
        }

        if (null == password || password.length() == 0) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Invalid password");
            return validationStatus;
        }

        return validationStatus;
    }

    static public ValidationStatus isValidUserPhoneNumber(String userPhoneNumber) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setStatus(true);
        validationStatus.setMessage("");

        String PHONE_NUMBER_PATTERN = "^[0-9]{3,20}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        matcher = pattern.matcher(userPhoneNumber);

        if (userPhoneNumber == null || (userPhoneNumber.length() < 10 || userPhoneNumber.length() > 10)) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Phone Number : should have 10 digits");
        }

        if (!matcher.matches()) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Phone Number : should have 10 digits");
        }

        return validationStatus;
    }

    static public ValidationStatus isValidUserUserName(String userName) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setStatus(true);
        validationStatus.setMessage("");

        String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{3,20}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(userName);

        if (!matcher.matches()) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Username : starts with alphabet, can have numbers, dash, underscore, min 3 and max 20 characters");
        }

        return validationStatus;
    }


    static public ValidationStatus isValidPassword(String password) {
        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setStatus(true);
        validationStatus.setMessage("");
        if (password == null || (password.length() < 6 || password.length() > 20)) {
            validationStatus.setStatus(false);
            validationStatus.setMessage("Password :  min 6 and max 20 characters");
        }


        return validationStatus;
    }



}
