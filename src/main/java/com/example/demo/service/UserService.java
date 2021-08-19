package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.service.mappers.MapperUserDtoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.service.mappers.MapperUserDtoService.convertToDTOUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MapperUserDtoService mapperUserDtoService;
    private CourseRepository courseRepository;

    private final PasswordEncoder encoder;



    @Lazy
    public UserService(UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder, MapperUserDtoService mapperUserDtoService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.encoder = passwordEncoder;
        this.mapperUserDtoService = mapperUserDtoService;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(usr -> convertToDTOUser(usr))
                .collect(Collectors.toList());
    }

    public UserDto findById(long id) {
        User usr = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return convertToDTOUser(usr);
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
        Optional<User> userOld = userRepository.findUserByUsername(userDto.getUsername());
        if(!userOld.isEmpty()) userDto.setAvatarImage(userOld.get().getAvatarImage());
        userRepository.save(mapperUserDtoService.convertToEntityUser(userDto));
    }


    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(NoSuchElementException::new);
    }
}