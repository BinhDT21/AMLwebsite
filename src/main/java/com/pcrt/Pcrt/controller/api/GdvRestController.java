package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.entities.Report;
import com.pcrt.Pcrt.entities.Transaction;
import com.pcrt.Pcrt.repository.ReportRepository;
import com.pcrt.Pcrt.repository.TransactionRepository;
import com.pcrt.Pcrt.service.Pcrt06_Service;
import com.pcrt.Pcrt.service.Pcrt07_Service;
import com.pcrt.Pcrt.service.TransactionService;
import com.pcrt.Pcrt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class GdvRestController {
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ReportRepository reportRepository;


    @PostMapping("/gdv/pcrt-06/{pcrt06Id}/update")
    public String updatePCRT_06 (Model model, @PathVariable(name = "pcrt06Id") int pcrt06Id) throws IOException {
        pcrt06Service.updatePCRT06(pcrt06Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "gdv/pcrt_06";
    }

    @PostMapping("/gdv/pcrt-07/{pcrt07Id}/update")
    public String updatePCRT_07 (Model model, @PathVariable(name = "pcrt07Id") int pcrt07Id) throws IOException {
        pcrt07Service.updatePCRT07(pcrt07Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "gdv/pcrt_07";
    }

    @PostMapping("/gdv/report/create/{transactionId}")
    public ResponseEntity<Report> createReport (@PathVariable(value = "transactionId") int id ){
        Transaction transaction = transactionService.getTransactionById(id);
        transaction.setStatus("archived");
        transactionRepository.save(transaction);

        Report report = new Report();
        report.setCreatedDate(LocalDateTime.now());
        report.setUser(userService.getCurrentUser());
        report.setTransaction(transaction);
        report.setStatus("pending");
        reportRepository.save(report);

        return ResponseEntity.ok(report);
    }
}
