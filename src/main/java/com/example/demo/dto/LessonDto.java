package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    @NotBlank(message = "Не заполнено название")
    private String title;
    @NotBlank(message = "Не заполнено содержание")
    private String text;
    @NotNull
    private Long courseId;

    public LessonDto(Long id, String title, Long courseId) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
    }

    public LessonDto(long courseId) {
        this.courseId = courseId;
    }
}
