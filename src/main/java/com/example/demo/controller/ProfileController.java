package com.example.demo.controller;

import com.example.demo.service.ImageStorageService;
import com.example.demo.service.UserService;
import com.example.demo.service.mappers.MapperUserDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String profile(Model model, Principal principal){
        model.addAttribute("user",
                MapperUserDtoService.convertToDTOUser(userService.findUserByUsername(principal.getName()))
        );
        return "profile";
    }

    @PostMapping("/avatar")
    public String updateAvatarImage(Principal principal,
                                    @RequestParam("avatar") MultipartFile avatar) throws IOException, IllegalAccessException {
        logger.info("File name {}, file content type {}, file size {}", avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());

        imageStorageService.save(principal.getName(), avatar.getContentType(), avatar.getInputStream());

        return "redirect:/profile";
    }

    @GetMapping("/avatar")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(Authentication auth) {
        String contentType = imageStorageService.getContentTypeByUser(auth.getName())
                .orElseThrow(NoSuchElementException::new);
        byte[] data = imageStorageService.getAvatarImageByUser(auth.getName())
                .orElseThrow(NoSuchElementException::new);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }

    @ExceptionHandler
    public ResponseEntity<Void> notFoundExceptionHandler(NoSuchElementException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> ExceptionHandler(Exception ex) {
        return ResponseEntity.internalServerError().build();
    }

}
