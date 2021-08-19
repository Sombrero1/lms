package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.service.mappers.MapperUserDtoService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;


public class UserServiceTest{
    private static final Role ROLE_STUDENT = new Role(1L, "ROLE_STUDENT");
    private static final Role ROLE_ADMIN = new Role(2L, "ROLE_ADMIN");
    private static Course course1;
    private static Course course2;

    private static UserDto user1;
    private static UserDto user2;
    private static UserRepository userRepositoryMock;
    private static CourseRepository courseRepositoryMock;

    private static UserService userService;


    @BeforeAll
    static void beforeAll() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        courseRepositoryMock = Mockito.mock(CourseRepository.class);


        course1 = new Course(1L, "Автор1", "Курс1");
        course2 = new Course(2L, "Автор2", "Курс2");

        user1 = new UserDto(1l, "admin", "123", Set.of(ROLE_ADMIN));
        user2 = new UserDto(2l, "student", "123", Set.of(ROLE_STUDENT));


        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        MapperUserDtoService mapperUserDtoService = new MapperUserDtoService(passwordEncoder);



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
        Mockito.when(userRepositoryMock.findById(1l)).thenReturn(
                Optional.of(mapperUserDtoService.convertToEntityUser(user1)));
        Mockito.when(userRepositoryMock.findById(2l)).thenReturn(
                Optional.of(mapperUserDtoService.convertToEntityUser(user2)));




        userService = new UserService(userRepositoryMock, courseRepositoryMock, passwordEncoder, mapperUserDtoService);
    }

    @Test
    void findByExistUser(){
        assertEquals(userService.findById(user1.getId()), user1);
        assertEquals(userService.findById(user2.getId()), user2);
    }
    @ParameterizedTest
    @ValueSource(longs = {3,4,100})
    void findByExistUser(long id){
        assertThrows(NoSuchElementException.class, ()->userService.findById(id));
    }


}
