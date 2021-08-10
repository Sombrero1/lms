package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LessonServiceTest {
    private static LessonService lessonService;
    private static CourseService courseService;

    private static Course course1;
    private static Course course2;

    private static Lesson lesson1;
    private static Lesson lesson2;

    @BeforeAll
    static void init(){
        LessonRepository lessonRepositoryMock = Mockito.mock(LessonRepository.class);
        CourseRepository courseRepositoryMock = Mockito.mock(CourseRepository.class);

        course1 = new Course(1L, "Автор1", "Курс1");
        course2 = new Course(2L, "Автор2", "Курс2");

        Mockito.when(courseRepositoryMock
                .findById(org.mockito.ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(courseRepositoryMock
                .findById(1L))
                .thenReturn(Optional.of(course1));
        Mockito.when(courseRepositoryMock
                .findById(2L))
                .thenReturn(Optional.of(course2));
        courseService = new CourseService(courseRepositoryMock);

        lesson1 = new Lesson("title1","text1");
        lesson2 = new Lesson("title2","text2");

        Mockito.when(lessonRepositoryMock.findById(org.mockito.ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        Mockito.when(lessonRepositoryMock.findById(1L)).thenReturn(Optional.of(lesson1));
        Mockito.when(lessonRepositoryMock.findById(2L)).thenReturn(Optional.of(lesson2));

        lessonService = new LessonService(lessonRepositoryMock, courseRepositoryMock);
    }

    @Test
    void save_lesson(){
        lessonService.save(1l, lesson1);
        lessonService.save(2l, lesson2);
        assertEquals(course1, lesson1.getCourse());
        assertEquals(course2, lesson2.getCourse());
    }
    @Test
    void findByIdExistLesson(){
        assertEquals(lesson1, lessonService.findById(1L));
        assertEquals(lesson2, lessonService.findById(2L));
    }

    @ParameterizedTest()
    @ValueSource(longs = {3l,100000000l})
    void findByIdNotExistCourse(Long id){
        assertThrows(NoSuchElementException.class,  ()-> lessonService.findById(id));;
    }

    @Test
    void createTemplateLesson(){
        assertEquals(new LessonDto(course1.getId()), lessonService.createTemplateLessonForCourse(1l));
    }
}
