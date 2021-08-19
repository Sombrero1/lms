package com.example.demo.service.mappers;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MapperUserDtoService   {


    private PasswordEncoder passwordEncoder;

    public MapperUserDtoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public User convertToEntityUser(UserDto userDto) {
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

}
