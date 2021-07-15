package com.example.demo.service;

import com.example.demo.domain.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CourseService {
    void save(Course course);

    Course createTemplateCourse();

    List<Course> findByTitleWithPrefix(String titlePrefix);

    Course findById(Long id);

    void delete(Long id);
}
