package com.example.demo.controller;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;


    @ModelAttribute("roles")
    public List<Role> rolesAttribute() {
        return roleRepository.findAll();
    }

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users",userService.findAll());
        return "users";
    }

    @GetMapping("/{user_id}")
    public String findById(Model model, @PathVariable("user_id") long id) {
        model.addAttribute("user",userService.findById(id));

        return "user_form";
    }

    @DeleteMapping("/{user_id}")
    public String deleteById(@PathVariable("user_id") long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("user") UserDto userDto) {
        userService.save(userDto);
        return "redirect:/admin/users";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}
