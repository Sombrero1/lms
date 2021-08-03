package com.example.demo.controller;

import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Course;

import com.example.demo.domain.User;
import com.example.demo.dto.CourseDto;
import com.example.demo.service.*;
import com.example.demo.service.mappers.MapperCourseDtoService;
import com.example.demo.service.mappers.MapperLessonDtoService;
import com.example.demo.service.mappers.MapperUserDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.demo.service.UserAuthService.*;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final AssignCourseToUserService assignCourseToUserService;
    private final MapperCourseDtoService mapperCourseDtoService;
    private final LessonService lessonService;

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final UserService userService;


    public CourseController(CourseService courseService, AssignCourseToUserService assignCourseToUserService,
                            MapperCourseDtoService mapperCourseDtoService, LessonService lessonService,
                             UserService userService) {
        this.courseService = courseService;
        this.assignCourseToUserService = assignCourseToUserService;
        this.mapperCourseDtoService = mapperCourseDtoService;
        this.lessonService = lessonService;
        this.userService = userService;
    }




    @GetMapping()
    public String courseTable(Model model, @RequestParam(value = "titlePrefix", required = false) String titlePrefix, Principal principal) {
        if(principal != null){
            logger.info("Request from user '{}'",principal.getName());
        }
        model.addAttribute("courses",
                courseService.
                        findByTitleWithPrefix(titlePrefix).
                        stream().
                        map(l -> mapperCourseDtoService.convertToDTOCourse(l))
                        .collect(Collectors.toList())
        );
        return "courses";
    }

    private void fillModelAdditionalCourse(Model model, Course course){
        model.addAttribute("lessons", lessonService.findAllForLessonIdWithoutText(course.getId()));
        model.addAttribute("users", course.getUsers());
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/{id}")
    @Transactional
    public String courseForm(Model model, @PathVariable("id") Long id) {
        Course course = courseService.findById(id);
        model.addAttribute("courseDto", mapperCourseDtoService.convertToDTOCourse(course));
        fillModelAdditionalCourse(model, course);
        return "course_form";
    }

    @Secured(ROLE_ADMIN)
    @PostMapping
    @Transactional
    public String applyCourseForm(@Valid CourseDto courseDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if(courseDto.getId()!=null){
                Course course = mapperCourseDtoService.convertToEntityCourse(courseDto);
                fillModelAdditionalCourse(model, course);
            }
            return "course_form";
        }
        courseService.save(mapperCourseDtoService.convertToEntityCourse(courseDto));
        return "redirect:/course";
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("courseDto", mapperCourseDtoService.convertToDTOCourse(courseService.createTemplateCourse()));
        return "course_form";
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return "redirect:/course";
    }

    @PreAuthorize(IS_PRINCIPAL)
    @GetMapping("/{id}/assign")
    public String assignCourse(Model model, @PathVariable("id") Long courseId, HttpServletRequest request) {
        if(request.isUserInRole("ROLE_ADMIN")){
            model.addAttribute("users", assignCourseToUserService.findUsersNotAssignedToCourse(courseId));
        }
        else{
            User user = userService.findUserByUsername(request.getRemoteUser());
            Course course = user.getCourses().stream().filter(x -> x.getId() == courseId).findAny().orElse(null);
            if (course != null){
                return "redirect:/course";
            }
            model.addAttribute("users", Collections.singletonList(user));
        }
        model.addAttribute("courseId",courseId);
        return "assign_course";
    }

    @PreAuthorize(IS_PRINCIPAL)
    @PostMapping("/{courseId}/unassign")
    public String applyUnsignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        assignCourseToUserService.unsignUser(courseId,id);
        return "redirect:/course";
    }

    @PreAuthorize(IS_PRINCIPAL)
    @PostMapping("/{courseId}/assign")
    public String applyAssignUserForm(@PathVariable("courseId") Long courseId,
                                 @RequestParam("userId") Long id) {
        assignCourseToUserService.signUser(courseId, id);
        return "redirect:/course";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }



//
//    @PostMapping
//    public String submitUserForm(@Valid @ModelAttribute("user") UserDto user,
//                                 BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "user_form";
//        }
//
//        userService.save(user);
//        return "redirect:/user";
//    }
}