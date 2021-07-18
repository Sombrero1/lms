package com.example.demo.domain;


import com.example.demo.service.TitleCaseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TitleCaseValidator.class)
public @interface TitleCase {
    String message() default "Не соотвествует правилам задания заголовков";

    enum type {
        RU,
        EN,
        ANY
    }

    type typeOfTitle() default type.ANY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
