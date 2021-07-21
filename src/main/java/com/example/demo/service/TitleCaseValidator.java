package com.example.demo.service;



import com.example.demo.domain.TitleCase;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
@NoArgsConstructor
public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {
    private static TitleCase.Type type;
    private static final String eng = "([A-Za-z',\"' ]+)";
    private static final String rus = "([А-Яа-я',\"' ]+)";
    private static final String []conjs = new String[]{"a", "but","for", "or", "not", "the", "an", "and"};



    public TitleCaseValidator(TitleCase.Type type) {
        this.type = type;
    }

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        type = constraintAnnotation.typeOfTitle();
    }


    private static boolean isRusTitle(String s){
        if (s != "" && !s.contains("  ") && s.strip().equals(s)){
            if (Character.isUpperCase(s.charAt(0)) && s.substring(1).equals(s.substring(1).toLowerCase())
                    && isMatchingRegexp(s, rus)) return true;
        }
        return false;
    }

    private static boolean isEngTitle(String s){
        if (s != "" && !s.contains("  ") && s.strip().equals(s) && isMatchingRegexp(s, eng)){
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
        return true;
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
                return (isRusTitle(s));
            case EN:
                return isEngTitle(s);
            default:
                return ((isEngTitle(s) ^ isRusTitle(s)));
            }
    }
}
