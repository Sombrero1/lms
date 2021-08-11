package com.example.demo.service.mappers;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.demo.domain.Lesson;
import com.example.demo.domain.User;
import com.example.demo.dto.LessonDto;
import com.example.demo.dto.UserDto;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MapperUserDtoService   {

    private static PasswordEncoder passwordEncoder;

    static {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public static User convertToEntityUser(UserDto userDto) {
       return new User(userDto.getId(),
               userDto.getUsername(),
               passwordEncoder.encode(userDto.getPassword()),
               userDto.getRoles(),
               userDto.getAvatarImage()
       );
    }
    public static UserDto convertToDTOUser(User user) {
        return new UserDto(user.getId(),user.getUsername(),user.getRoles());
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        MapperUserDtoService.passwordEncoder = passwordEncoder;
    }
}
