package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.entities.PCRT_06;
import com.pcrt.Pcrt.entities.PCRT_07;
import com.pcrt.Pcrt.entities.Review;
import com.pcrt.Pcrt.entities.Transaction;
import com.pcrt.Pcrt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/aml-manager")
public class AmlManagerController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/review-records")
    public String reviewRecords(Model model) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        DateTimeFormatter formatterObject = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Review r: reviewService.reviewList()){
            r.setCreatedDateTmp(formatterObject.format(r.getCreatedDate()));
        }
        model.addAttribute("reviewList", reviewService.reviewList());

        model.addAttribute("reviewPassList", reviewService.reviewList("pass"));

        model.addAttribute("reviewFailList", reviewService.reviewList("fail"));

        return "aml-manager/review_records";
    }

    @GetMapping("/pcrt-06")
    public String PCRT_06 (Model model, Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_06> pcrt06List = pcrt06Service.getListPCRT_06(params);
        model.addAttribute("pcrt06List", pcrt06List);
        model.addAttribute("totalPages", pcrt06List.getTotalPages());
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);

        return "aml-manager/pcrt_06";
    }

    @GetMapping("/pcrt-07")
    public String PCRT_07 (Model model, @RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_07> pcrt07List = pcrt07Service.getListPCRT_07(params);
        model.addAttribute("pcrt07List", pcrt07List);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pcrt07List.getTotalPages());

        return "aml-manager/pcrt_07";
    }

    @GetMapping("/transactions")
    public String transactions(Model model, @RequestParam Map<String, String> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<Transaction> transactions = transactionService.getTransactionList(params);
        for (Transaction t : transactions) {
            t.setCreatedDateTmp(formatter.format(t.getCreatedDate()));
        }
        model.addAttribute("transactions", transactions);

        int page = params.get("page")!=null ? Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transactions.getTotalPages());

        return "aml-manager/transactions";
    }

    @GetMapping("/transactions/transaction-detail/{transactionId}")
    public String transactionDetail (Model model, @PathVariable(value = "transactionId") int id){
        model.addAttribute("currentUser", userService.getCurrentUser());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Transaction transaction = transactionService.getTransactionById(id);
        transaction.setCreatedDateTmp(formatter.format(transaction.getCreatedDate()));

        model.addAttribute("transaction", transaction);

        //Lỗi amount
        BigDecimal amount = transactionService.getTransactionAmountOfDateByCustomerId2(transaction.getCustomer().getId());
        if (amount.compareTo(new BigDecimal("400000000")) >= 0)
            model.addAttribute("amountErr", amount);
        //Lỗi frequency
        int fre = transactionService.getFrequencyOfDateByCustomerId2(transaction.getCustomer().getId());
        if (fre >= 20)
            model.addAttribute("frequencyErr", fre);

        return "aml-manager/transaction_detail";
    }

}
