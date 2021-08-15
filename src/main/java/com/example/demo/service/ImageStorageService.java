package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.ImageRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Image;
import com.example.demo.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class ImageStorageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    public ImageStorageService(ImageRepository imageRepository, UserRepository userRepository,
                               CourseRepository courseRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Value("${file.storage.path}")
    private static String path = "images";

    @Transactional
    public void save(String username, String contentType, InputStream is) throws IllegalAccessException {
        Optional<User> opt =  userRepository.findUserByUsername(username);
        User user = opt.orElseThrow(NoSuchElementException::new);
        Image avatar =  user.getAvatarImage();
        String filename;
        if (avatar == null){
            filename = UUID.randomUUID().toString();
            avatar = new Image(null, contentType, filename);
            user.setAvatarImage(avatar);
        } else{
            filename = avatar.getFilename();
            avatar.setContentType(contentType);
        }
        imageRepository.save(avatar);
        userRepository.save(user);
        try(OutputStream os = Files.newOutputStream(Path.of(path,filename),CREATE, WRITE, TRUNCATE_EXISTING)){
            is.transferTo(os);
        } catch (Exception ex){
            logger.error("Can't write to file {}",filename, ex);
            throw new IllegalAccessException(ex.toString());
        }
    }

    @Transactional
    public void saveCourseImage(Long id, String contentType, InputStream is) throws IllegalAccessException {
        Optional<Course> opt =  courseRepository.findById(id);
        Course course = opt.orElseThrow(NoSuchElementException::new);
        Image avatar =  course.getAvatarImage();
        String filename;
        if (avatar == null){
            filename = UUID.randomUUID().toString();
            avatar = new Image(null, contentType, filename);
            course.setAvatarImage(avatar);
        } else{
            filename = avatar.getFilename();
            avatar.setContentType(contentType);
        }
        imageRepository.save(avatar);
        courseRepository.save(course);
        try(OutputStream os = Files.newOutputStream(Path.of(path,filename),CREATE, WRITE, TRUNCATE_EXISTING)){
            is.transferTo(os);
        } catch (Exception ex){
            logger.error("Can't write to file {}",filename, ex);
            throw new IllegalAccessException(ex.toString());
        }
    }

    public Optional<String> getContentTypeByUser(String username) {
        return userRepository.findUserByUsername(username)
                .map(User::getAvatarImage)
                .map(Image::getContentType);
    }

    public Optional<byte[]> getAvatarImageByUser(String username) {
        return userRepository.findUserByUsername(username)
                .map(User::getAvatarImage)
                .map(Image::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }

    public Optional<String> getContentTypeByCourseId(Long courseId) {
        return courseRepository.findById(courseId).map(Course::getAvatarImage).map(Image::getContentType);
    }

    public Optional<byte[]> getCourseImageByCourseId(Long courseId) {
        return courseRepository.findById(courseId)
                .map(Course::getAvatarImage)
                .map(Image::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }



}
