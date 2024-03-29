package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class AssignCourseToUserService {
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    public AssignCourseToUserService(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void unsignUser(Long courseId, Long userId) throws NoSuchElementException {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchElementException::new);
        course.getUsers().remove(user);
        user.getCourses().remove(course);
        courseRepository.save(course);
    }


    public List<User>findUsersNotAssignedToCourse(Long courseId) {
        return userRepository.findUsersNotAssignedToCourse(courseId);
    }


    public void signUser(Long courseId, Long userId) throws NoSuchElementException {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(NoSuchElementException::new);
        course.getUsers().add(user);
        user.getCourses().add(course);
        courseRepository.save(course);
    }
}



