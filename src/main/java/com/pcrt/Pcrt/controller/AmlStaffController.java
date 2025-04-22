package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.dto.response.ReviewRecordsByNumberTransactionResponse;
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/aml-staff")
public class AmlStaffController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private ReportService reportService;
    @Autowired
    private Pcrt04_Service pcrt04Service;

    @GetMapping("/review-records")
    public String reviewRecords(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        List<Transaction> reviewRecordsByAmount = transactionService.reviewRecordsByAmount(params);
        DateTimeFormatter formatterObject = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Transaction t : reviewRecordsByAmount) {
            t.setCreatedDateTmp(formatterObject.format(t.getCreatedDate()));
        }
        // bind danh sách transaction được query theo amount >= 400 triệu
        model.addAttribute("reviewRecordsByAmount", reviewRecordsByAmount);

        // Object[2]
        List<Object[]> reviewRecordsByNumberTransaction = transactionService.reviewRecordsByNumberTransaction(params);
        List<ReviewRecordsByNumberTransactionResponse> reviewRecordsByNumberTransactionList = new ArrayList<>();


        for (Object[] o : reviewRecordsByNumberTransaction) {
            Customer c = (Customer) o[0];
            Long l = (Long) o[1];
            reviewRecordsByNumberTransactionList.add(new ReviewRecordsByNumberTransactionResponse(c, l));
        }

        //bind danh sách transaction query theo số lần trong ngày > 20 lần
        model.addAttribute("reviewRecordsByNumber", reviewRecordsByNumberTransactionList);

        return "aml-staff/review_records";
    }

    @GetMapping("/pcrt-06/create")
    public String createPCRT_06(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        //bind reviewList
        List<Review> reviewList = reviewService.reviewList("pass");
        DateTimeFormatter formatterObject = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Review r: reviewList){
            r.setCreatedDateTmp(formatterObject.format(r.getCreatedDate()));
        }

        reviewList.removeIf(r ->
                (r.getCustomer().getType().equals("cá nhân") && r.getCustomer().getPcrt_03_forms().isEmpty()) ||
                        (!r.getCustomer().getType().equals("cá nhân") && r.getCustomer().getPcrt_04_forms().isEmpty())
        );

        model.addAttribute("reviewList", reviewList);
        return "aml-staff/pcrt_06_create";
    }

    @GetMapping("/pcrt-07/create")
    public String createPCRT_07(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        //bind reviewList
        List<Review> reviewList = reviewService.reviewList("pass");
        DateTimeFormatter formatterObject = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Review r: reviewList){
            r.setCreatedDateTmp(formatterObject.format(r.getCreatedDate()));
        }

        reviewList.removeIf(r -> (r.getCustomer().getType().equals("cá nhân") && !r.getCustomer().getPcrt_03_forms().isEmpty()) ||
                (!r.getCustomer().getType().equals("cá nhân") && !r.getCustomer().getPcrt_04_forms().isEmpty()));
        model.addAttribute("reviewList", reviewList);
        return "aml-staff/pcrt_07_create";
    }

    @GetMapping("/pcrt-06")
    public String PCRT_06 (Model model, @RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_06> pcrt06List = pcrt06Service.getListPCRT_06(params);
        model.addAttribute("pcrt06List", pcrt06List);
        model.addAttribute("totalPages", pcrt06List.getTotalPages());
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);

        return "aml-staff/pcrt_06";
    }

    @GetMapping("/pcrt-07")
    public String PCRT_07 (Model model, @RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_07> pcrt07List = pcrt07Service.getListPCRT_07(params);
        model.addAttribute("pcrt07List", pcrt07List);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pcrt07List.getTotalPages());

        return "aml-staff/pcrt_07";
    }


    @GetMapping("/reports")
    public String reports (Model model, @RequestParam Map<String, String> params){
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<Report> reportList = reportService.getReportList(params);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for(Report r : reportList){
            r.setCreatedDateTmp(formatter.format(r.getCreatedDate()));
        }
        model.addAttribute("reports", reportList);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reportList.getTotalPages());

        return "aml-staff/reports";
    }

    @GetMapping("/reports/report-detail/{reportId}")
    public String reportDetail (Model model, @PathVariable(value = "reportId") int id){
        model.addAttribute("currentUser", userService.getCurrentUser());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Report report = reportService.getReportById(id);

        //format transaction createdDate
        report.getTransaction().setCreatedDateTmp(formatter2.format(report.getTransaction().getCreatedDate()));
        //format report createdDate
        report.setCreatedDateTmp(formatter.format(report.getCreatedDate()));

        //get List<Kqks> List<PCRT_03> List<PCRT_04>
        List<Kqks> kqksList = report.getTransaction().getCustomer().getKqksList();
        List<PCRT_03> pcrt_03List = report.getTransaction().getCustomer().getPcrt_03_forms();
        List<PCRT_04> pcrt_04List = report.getTransaction().getCustomer().getPcrt_04_forms();
        kqksList.forEach(i -> i.setCreatedDateTmp(formatter.format(i.getCreatedDate())));
        pcrt_03List.forEach(i -> i.setCreatedDateTmp(formatter.format(i.getCreatedDate())));
        pcrt_04List.forEach(i -> i.setCreatedDateTmp(formatter.format(i.getCreatedDate())));
        //

        model.addAttribute("report", report);

        return "aml-staff/report_detail";
    }

    @GetMapping("/reports/report/{reportId}/create")
    public String createReport (Model model,
                                @PathVariable(value = "reportId") int reportId) throws IOException {


        //lấy report được lập báo cáo ra
        Report report = reportService.getReportById(reportId);

        //đưa report và detail vào để lập báo cáo
        //report.setStatus = "reported" và report.setFilePath = "......";
        File file = reportService.createPCRT_01(report);
        reportService.uploadFile(report, file);

        return "redirect:/aml-staff/reports";
    }


}
