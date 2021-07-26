package com.example.demo.service;


import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;

import java.util.List;
import java.util.NoSuchElementException;


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
        if(titlePrefix == null) titlePrefix = "";
        return courseRepository.findByTitleLike(titlePrefix + "%");
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
