package com.example.demo.service;



import com.example.demo.domain.TitleCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {
    public TitleCase.type type;
    private static String eng = "([A-Za-z',\"' ]+)";
    private static String rus = "([А-Яа-я',\"' ]+)";
    private static String []conjs = new String[]{"a", "but","for", "or", "not", "the", "an", "and"};

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        type = constraintAnnotation.typeOfTitle();
    }

    private static boolean isContainsSpace(String s){
        String[]words = s.split(" ");
        for (String word:words
             ) {
            if(word.length() == 0) return true;
        }
        return false;
    }

    private static boolean isRusTitle(String s){
        if (!s.contains("\r\t\n") && !isContainsSpace(s) && s.strip().equals(s)){
            if (Character.isUpperCase(s.charAt(0)) && s.substring(1).equals(s.substring(1).toLowerCase())
                    && isMatchingRegexp(s, rus)) return true;
        }
        return false;
    }

    private static boolean isEngTitle(String s){
        if (!s.contains("\r\t\n") && !isContainsSpace(s) && s.strip().equals(s)){
            String[]words = s.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (i != 0 && i != words.length - 1){
                    if(!Arrays.asList(conjs).contains(words[i]) && !Character.isUpperCase(words[i].charAt(0))) return false;
                }
                else{
                    if(!Character.isUpperCase(words[i].charAt(0))) return false;
                }
            }
        }
        else{
            return false;
        }

        return true; //если не сработало обратное
    }


    private static boolean isMatchingRegexp(final String text, final String template)
    {
        Pattern pattern = null;
        try
        {
            pattern = Pattern.compile(template);
        }
        catch(PatternSyntaxException e)
        {
            e.printStackTrace();
        }
        if(pattern == null)
        {
            return false;
        }
        final Matcher regexp = pattern.matcher(text);
        return regexp.matches();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        switch (type){
            case RU:
                if (isRusTitle(s)) return true;
                break;
            case EN:
                if (isEngTitle(s)) return true;
                break;
            case ANY:
                if ((isEngTitle(s) || isRusTitle(s))) return true;
                break;
            }

        return false;
    }
}
