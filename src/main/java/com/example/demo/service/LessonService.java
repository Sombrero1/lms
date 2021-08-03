package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class LessonService {
    private LessonRepository lessonRepository;
    private CourseRepository courseRepository;


    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }


    public void save(Long courseId, Lesson lesson) throws NoSuchElementException {
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchElementException::new);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }


    public LessonDto createTemplateLessonForCourse(Long courseId) {
        return new LessonDto(courseId);
    }


    public Lesson findById (Long id) throws NoSuchElementException {
        return lessonRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public void delete(Long id) {
            lessonRepository.deleteById(id);
    }


    public List<LessonDto> findAllForLessonIdWithoutText(Long courseId) {
        return lessonRepository.findAllForLessonIdWithoutText(courseId);
    }


}
