package com.example.demo.domain;


import com.example.demo.service.validators.TitleCaseValidator;
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

    enum Type {
        RU,
        EN,
        ANY
    }

    Type typeOfTitle() default Type.ANY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
