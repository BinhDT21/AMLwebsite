package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.dto.response.CustomerCheckingResponse;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.service.BlacklistService;
import com.pcrt.Pcrt.service.KqksService;
import com.pcrt.Pcrt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/kqks")
public class KqksController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private KqksService kqksService;

    @GetMapping("/createFile")
    public String createKqksForm(Model model, @ModelAttribute(value = "customer") Customer customer) {


        try {
            CustomerCheckingResponse response = blacklistService.getBlacklistByCustomer(customer);
            File f = kqksService.createFile(response, customer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(customer));
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "/gdv/customer_checking_result";
    }
}
