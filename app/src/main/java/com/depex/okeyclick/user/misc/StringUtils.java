package com.depex.okeyclick.user.misc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

    public static boolean isEmpty(String value){
        return value.isEmpty();
    }

    public static boolean isNumeric(String numricValue){
        Pattern pattern=Pattern.compile("[0-9]*");
        Matcher matcher=pattern.matcher(numricValue);
        return matcher.matches();
    }




    public static boolean isMobile(String number){
        Pattern pattern=Pattern.compile("[0-9]{9,12}");
        Matcher matcher=pattern.matcher(number);
        return matcher.matches();
    }

    public static String changeToProperCase(String name){
        String lower=name.toLowerCase();
        char ch=lower.charAt(0);
        ch-=32;
        StringBuilder sb=new StringBuilder(lower);
        sb.setCharAt(0, ch);
        return sb.toString();
    }

    public static boolean isValid(String  name){
        Pattern pattern=Pattern.compile("[A-Za-z//s]*");
        Matcher matcher=pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPin(String pin){
        Pattern pattern=Pattern.compile("[0-9]{6}");
        Matcher matcher=pattern.matcher(pin);
        return matcher.matches();
    }
}