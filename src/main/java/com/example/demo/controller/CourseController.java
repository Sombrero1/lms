package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.service.CourseService;
import com.example.demo.service.MapperLessonDtoService;
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

    public CourseController(CourseService courseService, MapperLessonDtoService mapperLessonDtoService, UserService userService) {
        this.courseService = courseService;
        this.mapperLessonDtoService = mapperLessonDtoService;
        this.userService = userService;
    }


    @GetMapping()
    public String courseTable(Model model, @RequestParam(value = "titlePrefix", required = false) String titlePrefix) {
        model.addAttribute("courses", courseService.findByTitleWithPrefix(titlePrefix));
        return "courses";
    }

    @GetMapping("/{id}")
    @Transactional
    public String courseForm(Model model, @PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", course.getLessons().stream()
                .map(l -> mapperLessonDtoService.convertToDTOLesson(l))
                .collect(Collectors.toList())
        );
        model.addAttribute("users", course.getUsers());
        return "course_form";
    }

    @PostMapping
    public String applyCourseForm(@Valid Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "course_form";
        courseService.save(course);
        return "redirect:/course";
    }

    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("course", courseService.createTemplateCourse());
        return "course_form";
    }
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return "course_form";
    }

    @GetMapping("/{id}/assign")
    public String assignCourse(Model model, @PathVariable("id") Long courseId) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("courseId",courseId);
        return "assign_course";
    }

    @PostMapping("/{courseId}/unassign")
    public String applyUnassignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        User user = userService.findById(id);
        Course course = courseService.findById(courseId);
        course.getUsers().remove(user);
        user.getCourses().remove(course);
        courseService.save(course);
        return String.format("redirect:/course/%d",courseId);
    }

    @PostMapping("/{courseId}/assign")
    public String applyAssignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        User user = userService.findById(id);
        Course course = courseService.findById(courseId);
        course.getUsers().add(user);
        user.getCourses().add(course);
        courseService.save(course);
        return String.format("redirect:/course/%d",courseId);
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}