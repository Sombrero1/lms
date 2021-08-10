package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.*;

import static com.example.demo.service.UserAuthService.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

public class AssignCourseToUserServiceTest {

    private static Course course1;
    private static Course course2;

    private static User user1;
    private static User user2;
    private static UserRepository userRepositoryMock;
    private static  CourseRepository courseRepositoryMock;

    private static AssignCourseToUserService assignCourseToUserService;


    @BeforeAll
    static void beforeAll() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        courseRepositoryMock = Mockito.mock(CourseRepository.class);


        course1 = new Course(1L, "Автор1", "Курс1");
        course2 = new Course(2L, "Автор2", "Курс2");

        user1 = new User(1l, "admin", "123");
        user2 = new User(2l, "student", "123");




        Mockito.when(courseRepositoryMock
                .findById(anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(courseRepositoryMock
                .findById(1L))
                .thenReturn(Optional.of(course1));
        Mockito.when(courseRepositoryMock
                .findById(2L))
                .thenReturn(Optional.of(course2));

        Mockito.when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMock.findById(1l)).thenReturn(Optional.of(user1));
        Mockito.when(userRepositoryMock.findById(2l)).thenReturn(Optional.of(user2));


        assignCourseToUserService = new AssignCourseToUserService(userRepositoryMock, courseRepositoryMock);

    }

    @BeforeEach
    void beforeEach() {
        HashSet<User> set1 = new HashSet<>();
        HashSet<User> set2 = new HashSet<>();
        set1.add(user1);
        set2.add(user2);
        course1.setUsers(set1);
        course2.setUsers(set2);


        HashSet<Course> set3 = new HashSet<>();
        HashSet<Course> set4 = new HashSet<>();
        set3.add(course1);
        set4.add(course2);
        user1.setCourses(set3);
        user2.setCourses(set4);

    }

    @ParameterizedTest
    @ValueSource(longs = {1,2})
    void findByIdExistUser(long id){
        assertEquals(Optional.of(assignCourseToUserService.findById(id)), userRepositoryMock.findById(id));
    }

    @ParameterizedTest
    @ValueSource(longs = {3,100})
    void findByIdNotExistUser(long id){
        assertThrows(NoSuchElementException.class, ()->assignCourseToUserService.findById(id));
    }

    @Test
    void signUser(){
        assignCourseToUserService.signUser(course1.getId(), user2.getId());
        assertEquals(course1.getUsers(), Set.of(user1, user2));
    }

    @Test
    void unsignUser(){
        assignCourseToUserService.unsignUser(course1.getId(), user1.getId());
        assertEquals(course1.getUsers(), Collections.emptySet());
    }



}
