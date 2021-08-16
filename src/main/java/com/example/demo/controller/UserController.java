package com.example.demo.controller;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.example.demo.service.UserAuthService.ROLE_ADMIN;

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

    @Secured(ROLE_ADMIN)
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users",userService.findAll());
        return "users";
    }

    @GetMapping("/{user_id}")
    public String findById(Model model, @PathVariable("user_id") long id, Principal principal) {
        UserDto user = userService.findById(id);
        if (!user.getUsername().equals(principal.getName()))  return "redirect:/access_denied";
        model.addAttribute("user",userService.findById(id));
        return "user_form";
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{user_id}")
    public String deleteById(@PathVariable("user_id") long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("user") UserDto userDto, Authentication authentication) {
        if (authentication.getName().equals(userDto.getUsername())){
            userService.save(userDto);
            return "redirect:/profile";
        }
        if(authentication.getAuthorities().contains(ROLE_ADMIN)){
            userService.save(userDto);
            return "redirect:/admin/users";
        }
        return "redirect:/access_denied";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NoSuchElementException ex){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}
