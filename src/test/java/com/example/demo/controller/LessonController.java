package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonService;
import com.example.demo.service.mappers.MapperCourseDtoService;
import com.example.demo.service.mappers.MapperLessonDtoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class LessonController {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LessonService lessonServiceMock;

    @MockBean
    private MapperLessonDtoService mapperLessonDtoService;


    private static final LessonDto lessonDto = new LessonDto( 1L, "text", 1l);
    private static final Course course = new Course(1l,"title","author");
    private static final Lesson lesson = new Lesson( "text", "text", course);


    @Test
    @WithMockUser(roles={"ADMIN"})
    void createLesson() throws Exception {
        Mockito.when(lessonServiceMock.createTemplateLessonForCourse(1l)).thenReturn(lessonDto);
        ModelAndView modelAndView = mockMvc
                .perform(get("/lesson/new").param("course_id", "1"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "lesson_form");
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void lessonForm() throws Exception {
        Mockito.when(lessonServiceMock.findById(any())).thenReturn(lesson);
        Mockito.when(mapperLessonDtoService.convertToDTOLesson(lesson)).thenReturn(lessonDto);
        ModelAndView modelAndView = mockMvc
                .perform(get("/lesson/{id}", 1L))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "lesson_form");
    }

    @Test
    @WithMockUser(roles={"STUDENT"})
    void submitLessonFormByStudent() throws Exception {
        mockMvc
                .perform(post("/lesson").flashAttr("lesson", lessonDto))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void submitLessonFormByStudentFromAdmin() throws Exception {
        mockMvc
                .perform(post("/lesson").flashAttr("lesson", lessonDto).with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void deleteLesson() throws Exception {
        Mockito.when(lessonServiceMock.findById(1L)).thenReturn(lesson);
        ModelAndView modelAndView = mockMvc
                .perform(delete("/lesson/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/1"))
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/course/1");
    }



}
