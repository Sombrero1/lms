package com.example.demo.controller;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping()
    public String courseTable(Model model, @RequestParam(value = "titlePrefix", required = false) String titlePrefix) {
        model.addAttribute("courses", courseService.findByTitleWithPrefix(titlePrefix));
        return "courses";
    }

    @GetMapping("/{id}")
    public String courseForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("course", courseService.findById(id));
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
        return "redirect:/course";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}