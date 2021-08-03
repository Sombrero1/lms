package com.example.demo.service;


import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public void save(Course course) {
        courseRepository.save(course);
    }


    public Course createTemplateCourse() {
        return new Course();
    }


    public List<Course> findByTitleWithPrefix(String titlePrefix) {
        if(titlePrefix == null) titlePrefix = "";
        return courseRepository.findByTitleLike(titlePrefix + "%");
    }


    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }



    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
