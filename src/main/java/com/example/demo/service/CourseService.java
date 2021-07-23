package com.example.demo.service;

import com.example.demo.domain.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public interface CourseService {
    void save(Course course);

    Course createTemplateCourse();

    List<Course> findByTitleWithPrefix(String titlePrefix);

    Course findById(Long id) throws NoSuchElementException;

    void delete(Long id);

}
