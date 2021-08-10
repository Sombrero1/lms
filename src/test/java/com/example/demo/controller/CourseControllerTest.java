package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.dto.CourseDto;
import com.example.demo.service.AssignCourseToUserService;
import com.example.demo.service.CourseService;
import com.example.demo.service.mappers.MapperCourseDtoService;
import com.example.demo.service.mappers.MapperUserDtoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CourseControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CourseService courseServiceMock;
    @MockBean
    private AssignCourseToUserService assignCourseToUserServiceMock;
    
   @MockBean
    private MapperCourseDtoService mapperCourseDtoService;

   private static final CourseDto courseDto = new CourseDto(1l,"title","author");
   private static final Course course = new Course(1l,"title","author");

    @Test
    void courseTable() throws Exception {
        ModelAndView modelAndView = mockMvc
                .perform(get("/course"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"courses");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getExistCourse() throws Exception {
        Mockito.when(courseServiceMock.createTemplateCourse()).thenReturn(course);
        Mockito.when(mapperCourseDtoService.convertToDTOCourse(any())).thenReturn(courseDto);
        ModelAndView modelAndView = mockMvc
                .perform(get("/course/new"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "course_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"STUDENT"})
    void assignUserToCourse() throws Exception {
        mockMvc
                .perform(post("/course/{id}/assign", 1L).param("userId", "1", "courseId","1"))
                .andExpect(status().is4xxClientError())
                .andReturn().getModelAndView();
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void assignUserToCourseFromAdmin() throws Exception {
        mockMvc
                .perform(post("/course/{courseId}/assign", 1L).param("userId","1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"))
                .andReturn().   getModelAndView();
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void deleteCourse() throws Exception {
        ModelAndView modelAndView = mockMvc
                .perform(delete("/course/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"))
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/course");
    }


    @Test
    @WithMockUser(roles={"STUDENT"})
    void courseForm() throws Exception {
        Mockito.when(courseServiceMock.findById(1L)).thenReturn(course);
        ModelAndView modelAndView = mockMvc
                .perform(get("/course/{id}", 1L))
                .andExpect(status().is4xxClientError())
                .andReturn().getModelAndView();
    }
    @Test
    @WithMockUser(roles={"ADMIN"})
    void courseFormFromAdmin() throws Exception {
        Mockito.when(courseServiceMock.findById(1L)).thenReturn(course);
        Mockito.when(mapperCourseDtoService.convertToDTOCourse(course)).thenReturn(courseDto);
        ModelAndView modelAndView = mockMvc
                .perform(get("/course/{id}", 1L))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "course_form");
    }


}
