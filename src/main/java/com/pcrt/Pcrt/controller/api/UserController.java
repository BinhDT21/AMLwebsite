package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.dto.request.LoginRequest;
import com.pcrt.Pcrt.dto.request.UserCreateRequest;
import com.pcrt.Pcrt.dto.response.LoginResponse;
import com.pcrt.Pcrt.dto.response.UserCreateResponse;
import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers (){
        return userService.getUsers();
    }

    @PostMapping("/create-user")
    public UserCreateResponse createUser (@RequestBody UserCreateRequest request){
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login (@ModelAttribute LoginRequest request){

        return userService.login(request);
    }
}
