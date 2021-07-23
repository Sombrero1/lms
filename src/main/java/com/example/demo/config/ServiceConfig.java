package com.example.demo.config;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.service.*;
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

    @Bean
    public UserService userService(UserRepository userRepository, CourseRepository courseRepository){
        return new UserServiceImpl(userRepository, courseRepository);
    }
}
