package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;
import com.example.demo.service.CourseService;
import com.example.demo.service.mappers.MapperCourseDtoService;
import com.example.demo.service.mappers.MapperLessonDtoService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final MapperLessonDtoService mapperLessonDtoService;
    private final UserService userService;
    private final MapperCourseDtoService mapperCourseDtoService;

    public CourseController(CourseService courseService, MapperLessonDtoService mapperLessonDtoService, UserService userService, MapperCourseDtoService mapperCourseDtoService) {
        this.courseService = courseService;
        this.mapperLessonDtoService = mapperLessonDtoService;
        this.userService = userService;
        this.mapperCourseDtoService = mapperCourseDtoService;
    }


    @GetMapping()
    public String courseTable(Model model, @RequestParam(value = "titlePrefix", required = false) String titlePrefix) {
        model.addAttribute("courses",
                courseService.
                        findByTitleWithPrefix(titlePrefix).
                        stream().
                        map(l -> mapperCourseDtoService.convertToDTOCourse(l))
                        .collect(Collectors.toList())
        );
        return "courses";
    }

    @GetMapping("/{id}")
    @Transactional
    public String courseForm(Model model, @PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        model.addAttribute("courseDto", mapperCourseDtoService.convertToDTOCourse(course));
        model.addAttribute("lessons", course.getLessons().stream()
                .map(l -> mapperLessonDtoService.convertToDTOLesson(l))
                .collect(Collectors.toList())
        );
        model.addAttribute("users", course.getUsers());
        return "course_form";
    }

    @PostMapping
    @Transactional
    public String applyCourseForm(@Valid CourseDto courseDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if(courseDto.getId()!=null){ //в случае неправильного редактирования существующего курса
                                        // (т.к. не входит в один form)
                Course course = mapperCourseDtoService.convertToEntityCourse(courseDto);
                model.addAttribute("lessons", course.getLessons().stream()
                        .map(l -> mapperLessonDtoService.convertToDTOLesson(l))
                        .collect(Collectors.toList())
                );
                model.addAttribute("users", course.getUsers());
            }
            return "course_form";
        }
        courseService.save(mapperCourseDtoService.convertToEntityCourse(courseDto));
        return "redirect:/course";
    }

    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("courseDto", mapperCourseDtoService.convertToDTOCourse(courseService.createTemplateCourse()));
        return "course_form";
    }
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return "redirect:/course";
    }

    @GetMapping("/{id}/assign")
    public String assignCourse(Model model, @PathVariable("id") Long courseId) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("courseId",courseId);
        return "assign_course";
    }

    @PostMapping("/{courseId}/unassign")
    public String applyUnsignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        userService.unsignUser(courseId,id);
        return String.format("redirect:/course/%d",courseId);
    }

    @PostMapping("/{courseId}/assign")
    public String applyAssignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        userService.signUser(courseId, id);
        return String.format("redirect:/course/%d",courseId);
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}