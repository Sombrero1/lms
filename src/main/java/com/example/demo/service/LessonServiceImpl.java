package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;

import java.util.NoSuchElementException;

public class LessonServiceImpl implements  LessonService {
    private LessonRepository lessonRepository;
    private CourseRepository courseRepository;


    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void save(Long courseId, Lesson lesson) throws NoSuchElementException {
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchElementException::new);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }

    @Override
    public Lesson createTemplateLesson() {
        return null;
    }

    @Override
    public Lesson findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
