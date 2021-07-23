package com.example.demo.service.mappers;
import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;
import com.example.demo.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MapperCourseDtoService {

    private ModelMapper modelMapper;
    private CourseService courseService;

    public MapperCourseDtoService(ModelMapper modelMapper, CourseService courseService) {
        this.modelMapper = modelMapper;
        this.courseService = courseService;
    }


    public Course convertToEntityCourse(CourseDto courseDTO) throws NoSuchElementException {
        Course course = null;
        if(courseDTO.getId() != null){
            course = courseService.findById(courseDTO.getId());
        }
        else{
            course = courseService.createTemplateCourse();
        }
        course.setTitle(courseDTO.getTitle());
        course.setAuthor(courseDTO.getAuthor());
        return course;
    }

    public CourseDto convertToDTOCourse(Course course) {
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        return courseDto;
    }

}
