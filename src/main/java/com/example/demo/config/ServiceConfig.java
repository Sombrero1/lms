package com.example.demo.config;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.CourseServiceImpl;
import com.example.demo.service.LessonService;
import com.example.demo.service.LessonServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public CourseService courseService(CourseRepository courseRepository){
        return new CourseServiceImpl(courseRepository);
    }

    @Bean
    public LessonService lessonService(LessonRepository lessonRepository, CourseRepository courseRepository){
        return new LessonServiceImpl(lessonRepository, courseRepository);
    }
}
