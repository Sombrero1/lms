package com.example.demo.service;

import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;

import java.util.List;
import java.util.NoSuchElementException;

public interface LessonService {

    void save(Long courseId, Lesson lesson) throws NoSuchElementException;

    Lesson createTemplateLesson();

    Lesson findById(Long id);

    void delete(Long id);
}
