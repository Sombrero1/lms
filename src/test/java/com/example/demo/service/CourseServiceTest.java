package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseServiceTest {

    private static CourseService courseService;
    private static Course course;

    @BeforeAll
    static void init(){
        CourseRepository courseRepositoryMock = Mockito.mock(CourseRepository.class);

        course = new Course(1L, "Автор", "Курс");

        Mockito.when(courseRepositoryMock
                .findById(org.mockito.ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(courseRepositoryMock
                .findById(1L))
                .thenReturn(Optional.of(course));
        courseService = new CourseService(courseRepositoryMock);
    }

    @Test
    void createTemplateCourse(){
        assertEquals(new Course(), courseService.createTemplateCourse());
    }

    @Test
    void findByIdExistCourse(){
        assertEquals(course, courseService.findById(1l));
    }


    @ParameterizedTest()
    @ValueSource(longs = {2l,3l,100000000l})
    void findByIdNotExistCourse(Long id){
        assertThrows(NoSuchElementException.class,  ()-> courseService.findById(id));;
    }

}
