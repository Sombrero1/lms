package com.example.demo.dto;

import com.example.demo.domain.Lesson;
import com.example.demo.domain.TitleCase;
import com.example.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CourseDto {
    private Long id;
    @NotBlank(message = "Course author has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    private String author;


    @NotBlank(message = "Course title has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    @TitleCase()
    private String title;

    public CourseDto(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }
}