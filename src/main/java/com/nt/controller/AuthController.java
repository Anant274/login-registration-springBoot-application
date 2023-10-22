package com.nt.controller;

import com.nt.dto.UserDto;
import com.nt.entity.User;
import com.nt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {
    //handle method to handle home page request

    private UserService userService;
    public AuthController(UserService userService)
    {
        this.userService=userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        UserDto userDto = new UserDto();
        model.addAttribute("user",userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Validated @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an acount registerd with the same email ");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/user")
    public String users(Model model)
    {
        List<UserDto> user = userService.findAllUsers();
        model.addAttribute("user",user);
        return "user";
    }
}