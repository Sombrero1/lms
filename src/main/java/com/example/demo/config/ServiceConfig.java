package com.example.demo.config;

import com.example.demo.dao.CourseRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.CourseServiceImpl;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    public CourseService courseService(CourseRepository courseRepository){
        return new CourseServiceImpl(courseRepository);
    }
}
