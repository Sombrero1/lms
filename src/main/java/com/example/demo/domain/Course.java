package com.example.demo.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Course {
    private Long id;
    @NotBlank(message = "Course author has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    private String author;
    @NotBlank(message = "Course title has to be filled")
    @Size(max=32, message = "Max length 32 chars")
    private String title;

    public Course() {
    }

    public Course(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}