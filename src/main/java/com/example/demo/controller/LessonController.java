package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonService;
import com.example.demo.service.MapperLessonDtoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final MapperLessonDtoService mapperLessonDtoService;

    public LessonController(LessonService lessonService, MapperLessonDtoService mapperLessonDtoService) {
        this.lessonService = lessonService;
        this.mapperLessonDtoService = mapperLessonDtoService;
    }


    @GetMapping("/new")
    public String lessonForm(Model model, @RequestParam("course_id") long courseId) {
        model.addAttribute("lessonDto", lessonService.createTemplateLessonForCourse(courseId));
        return "lesson_form";
    }

    @GetMapping("/{id}")
    public String lessonForm(Model model, @PathVariable Long id) {
        LessonDto lessonDto = mapperLessonDtoService.convertToDTOLesson(lessonService.findById(id));
        model.addAttribute("lessonDto", lessonDto);
        return "lesson_form";
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deleteLesson(@PathVariable("id") Long id){
        Long courseId = lessonService.findById(id).getCourse().getId();
        lessonService.delete(id);

        return String.format("redirect:/course/%d",courseId);
    }

    @PostMapping
    public String applyLessonForm(@Valid LessonDto lessonDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "lesson_form";
        Long courseId = lessonDto.getCourseId();
        Lesson lesson = mapperLessonDtoService.convertToEntityLesson(lessonDto);
        lessonService.save(courseId, lesson);
        return String.format("redirect:/course/%d",courseId);
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
