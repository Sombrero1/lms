package com.example.demo.config;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.MemoryBasedCourseRepository;
import com.example.demo.service.StatisticsCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsConfig {
    @Bean
    public StatisticsCounter statisticsCounter() {
        return new StatisticsCounter();
    }

}
