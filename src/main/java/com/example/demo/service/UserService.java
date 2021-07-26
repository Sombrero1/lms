package com.example.demo.service;

import com.example.demo.domain.Course;
import com.example.demo.domain.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {

    User findById(Long id) throws NoSuchElementException;

    List<User> getUsers();

    void signUser(Long courseId, Long userId) throws NoSuchElementException;

    void unsignUser(Long courseId, Long userId) throws NoSuchElementException;

    List<User>  findUsersNotAssignedToCourse(Long courseId);

}
