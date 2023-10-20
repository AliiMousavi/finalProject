package com.example.phase2.validator;

import java.util.regex.Pattern;

public class PasswordValidator {
    public static boolean isValidPassword(String password){
        String pattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8}$";
        return Pattern.matches(pattern,password);
    }
}
