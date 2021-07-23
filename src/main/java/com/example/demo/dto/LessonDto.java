package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LessonDto {
    private Long id;
    @NotBlank(message = "не заполнено")
    private String title;
    @NotBlank(message = "не заполнено")
    private String text;
    @NotNull
    private Long courseId;


    public LessonDto(long courseId) {
        this.courseId = courseId;
    }
}
