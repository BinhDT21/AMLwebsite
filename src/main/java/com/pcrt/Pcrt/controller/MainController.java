package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.service.CustomerService;
import com.pcrt.Pcrt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/")
    public String index (Model model){

        model.addAttribute("currentUser", userService.getCurrentUser());

        return "index";
    }

    @GetMapping("/admin")
    public String admin (Model model){
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "admin/admin";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }


    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "error/403"; // Trả về trang 403.html
    }


}
