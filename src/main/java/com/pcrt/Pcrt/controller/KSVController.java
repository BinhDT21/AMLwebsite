package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.dto.request.CustomerSaveRequest;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Opinion;
import com.pcrt.Pcrt.entities.PCRT_06;
import com.pcrt.Pcrt.entities.PCRT_07;
import com.pcrt.Pcrt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ksv")
public class KSVController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private OpinionService opinionService;
    @Autowired
    private KqksService kqksService;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07_Service pcrt07Service;


    @GetMapping("/customers")
    public String customers (Model model, @RequestParam Map<String, String> params){

        model.addAttribute("currentUser", userService.getCurrentUser());

        params.put("status", "waiting");
        Page<Customer> customerList = customerService.loadCustomerByStatus(params);
        model.addAttribute("customers", customerList);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customerList.getTotalPages());

        return "ksv/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String customer (Model model, @PathVariable(value = "id") int id){
        Customer customer = customerService.getCustomerById(id);

        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("customer", customer);
        model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(customer));
        model.addAttribute("kqksList", kqksService.getListKqksByCustomer(id));

        model.addAttribute("additionalForms", customerService.getAdditionalInfoFormByCustomer(id));
        // neu khách hàng là cá nhân thì trả qua danh sách pcrt03 nếu là doanh nghiệp thì trả ra pcrt04
        // trong customer service có phương thức lấy danh sách form bổ sung theo customer

        return "ksv/customer_detail";
    }

    @PostMapping("/save-customer")
    public String saveCustomer(Model model, @ModelAttribute(value = "customer") Customer customerObj,
                               @RequestParam Map<String, String> params) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        Customer customer = customerService.getCustomerById(customerObj.getId());

        if(customer.getOpinion()==null){
            Opinion opinion = new Opinion(customerObj.getOpinion().getMessage());
            opinion.setCustomer(customer);
            this.opinionService.saveOpinion(opinion);

            customer.setOpinion(opinion);
        }else{
            Opinion opinion = customer.getOpinion();
            opinion.setMessage(customerObj.getOpinion().getMessage());
        }

        customer.setStatus("checked");
        customerService.saveCustomer(customer, null);

        //Khi ksv xac nhan, se luu lai ksv nao da xac nhan customer nay
        userCustomerService.saveUserCustomer(customer, userService.getCurrentUser());

        return "redirect:/ksv/customers";
    }

    @GetMapping("/pcrt-06")
    public String PCRT_06 (Model model, @RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_06> pcrt06List = pcrt06Service.getListPCRT_06(params);
        model.addAttribute("pcrt06List", pcrt06List);
        model.addAttribute("totalPages", pcrt06List.getTotalPages());
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);

        return "ksv/pcrt_06";
    }

    @GetMapping("/pcrt-07")
    public String PCRT_07 (Model model,@RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_07> pcrt07List = pcrt07Service.getListPCRT_07(params);
        model.addAttribute("pcrt07List", pcrt07List);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pcrt07List.getTotalPages());

        return "ksv/pcrt_07";
    }
}
