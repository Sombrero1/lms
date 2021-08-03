package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private CourseRepository courseRepository;

    private final PasswordEncoder encoder;

    @Lazy
    public UserService(UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.encoder = passwordEncoder;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(usr -> new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles()))
                .collect(Collectors.toList());
    }

    public UserDto findById(long id) {
        User usr = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles());
    }

    public void deleteById(long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        for (Course course:user.getCourses()
             ) {
            course.getUsers().remove(user);
            user.getCourses().remove(course);
            courseRepository.save(course);
        }

        userRepository.delete(user);
    }

    public void save(UserDto userDto) {
        userRepository.save(new User(userDto.getId(),
                userDto.getUsername(),
                encoder.encode(userDto.getPassword()),
                userDto.getRoles()
        ));
    }


    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(NoSuchElementException::new);
    }
}