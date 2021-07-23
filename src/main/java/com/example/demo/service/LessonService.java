package com.example.demo.service;

import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;

import java.util.List;
import java.util.NoSuchElementException;

public interface LessonService {

    void save(Long courseId, Lesson lesson) throws NoSuchElementException;

    LessonDto createTemplateLessonForCourse(Long courseId);

    Lesson findById(Long id);

    void delete(Long id);
}
