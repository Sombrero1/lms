package com.example.demo.controller;

import com.example.demo.dao.RoleRepository;

import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
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

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceMock;

    private static final Role ROLE_ADMIN = new Role(1l,"ADMIN");
    private static final UserDto userDto = new UserDto(1L, "user", "password", Set.of(ROLE_ADMIN));

    @Test
    @WithMockUser(roles={"ADMIN"})
    void users() throws Exception {
        ModelAndView modelAndView = mockMvc
                .perform(get("/admin/users"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "users");
    }

    @Test
    @WithMockUser(roles={"STUDENT"})
    void userTableForNoAdmin() throws Exception {
        mockMvc
                .perform(get("/admin/user"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void userForm() throws Exception {
        Mockito.when(userServiceMock.findById(1L)).thenReturn(userDto);
        ModelAndView modelAndView = mockMvc
                .perform(get("/admin/users/{id}", userDto.getId()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "user_form");
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    void deleteUser() throws Exception {
        ModelAndView modelAndView = mockMvc
                .perform(delete("/admin/users/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andReturn().getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/admin/users");
    }

}
