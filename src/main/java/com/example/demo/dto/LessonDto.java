package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonDto lessonDto = (LessonDto) o;
        return Objects.equals(id, lessonDto.id) && Objects.equals(title, lessonDto.title) && Objects.equals(text, lessonDto.text) && Objects.equals(courseId, lessonDto.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, courseId);
    }
}
