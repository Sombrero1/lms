package com.example.demo.service;


import com.example.demo.controller.NotFoundException;
import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import org.springframework.stereotype.Service;

import java.util.List;


public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public Course createTemplateCourse() {
        return new Course();
    }

    @Override
    public List<Course> findByTitleWithPrefix(String titlePrefix) {
        return courseRepository.findByTitleWithPrefix(titlePrefix == null ? "" : titlePrefix);
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        courseRepository.delete(id);
    }
}
