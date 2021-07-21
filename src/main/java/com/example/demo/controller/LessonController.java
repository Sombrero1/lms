package com.example.demo.controller;

import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonService;
import com.example.demo.service.MapperLessonDtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("courseId", courseId);
        model.addAttribute("lessonDto", new LessonDto("hello","hello2",courseId));
        return "lesson_form";
    }

    @PostMapping
    public String applyLessonForm(LessonDto lessonDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "lesson_form";
        Lesson lesson = mapperLessonDtoService.convertToEntityLesson(lessonDto);
        try{
            lessonService.save(lessonDto.getCourseId(), lesson);
        }
        catch (NoSuchElementException e){
            //
        }
        return "redirect:/course";
    }
}
