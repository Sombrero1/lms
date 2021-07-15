package com.example.demo.dao;

import com.example.demo.domain.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository {
    List<Course> findAll();

    Optional<Course> findById(long id);

    void save(Course course);

    void delete(long id);

    List<Course> findByTitleWithPrefix(String prefix);
}