package com.example.demo.config;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.MemoryBasedCourseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public CourseRepository courseRepository(){
        return new MemoryBasedCourseRepository();
    }
}
