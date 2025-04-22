package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.service.Pcrt06_Service;
import com.pcrt.Pcrt.service.Pcrt07_Service;
import com.pcrt.Pcrt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class KsvRestController {

    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private UserService userService;


    @PostMapping("/ksv/pcrt-06/{pcrt06Id}/update")
    public String updatePCRT_06 (Model model, @PathVariable(name = "pcrt06Id") int pcrt06Id) throws IOException {
        pcrt06Service.updatePCRT06(pcrt06Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "ksv/pcrt_06";
    }

    @PostMapping("/ksv/pcrt-07/{pcrt07Id}/update")
    public String updatePCRT_07 (Model model, @PathVariable(name = "pcrt07Id") int pcrt07Id) throws IOException {
        pcrt07Service.updatePCRT07(pcrt07Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "ksv/pcrt_07";
    }
}
