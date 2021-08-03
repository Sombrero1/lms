package com.example.demo.service.mappers;

import com.example.demo.domain.Lesson;
import com.example.demo.domain.User;
import com.example.demo.dto.LessonDto;
import com.example.demo.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MapperUserDtoService {



    @Autowired
    private ModelMapper modelMapper;

    public User convertToEntityUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }
    public static UserDto convertToDTOUser(User user) {
        return new UserDto(user.getId(),user.getUsername(),user.getRoles());
    }

}
