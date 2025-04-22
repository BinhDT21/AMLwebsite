package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.dto.request.CreateReviewRequest;
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.repository.*;
import com.pcrt.Pcrt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AmlStaffRestController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private Pcrt06Repository pcrt06Repository;
    @Autowired
    private Pcrt06ItemRepository pcrt06ItemRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07Repository pcrt07Repository;
    @Autowired
    private Pcrt07ItemRepository pcrt07ItemRepository;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private UserService userService;

    @PostMapping("/aml-staff/transaction/{transactionId}/update-status")
    public void updateTransactionStatus(@PathVariable(value = "transactionId") int transactionId){
        transactionService.updateTransactionStatus(transactionId);
    }
    @PostMapping("/aml-staff/transaction/list/{nationalId}/update-status")
    public void updateListTransactionStatus(@PathVariable(value = "nationalId") String nationalId){
        transactionService.updateListTransactionStatus(nationalId);
    }

    @PostMapping("/aml-staff/create-review")
    public Review createReview (@RequestBody CreateReviewRequest createReviewRequest){
        return reviewService.createReview(createReviewRequest);
    }

    @GetMapping("/aml-staff/transaction/list/{customerId}")
    public List<Transaction> getTransactionListByCustomer (@PathVariable(value = "customerId") int customerId){
        List<Transaction> transactionList = transactionService.getTransactionListByCustomer(customerId);

        //format date for ui
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Transaction t: transactionList){

            t.setCreatedDateTmp(dateTimeFormatter.format(t.getCreatedDate()));
        }

        return transactionList;
    }


    @PutMapping("/aml-staff/customer/{customerId}/update-info")
    public ResponseEntity<Void> updateStatusRequest (@PathVariable(name = "customerId") int customerId){
        Customer customer = customerService.getCustomerById(customerId);
        customer.setStatus("checked");
        customerService.saveCustomer(customer, null);

        return ResponseEntity.ok().build();
    }

    //param key: mev
    @PutMapping("/aml-staff/review/{reviewId}/update")
    public ResponseEntity<Review> updateReviewManagerInfo (@PathVariable(name = "reviewId") int reviewId,
                                                         @RequestParam Map<String, String> params){
        Review review = reviewService.updateReview(reviewId, params);
        return ResponseEntity.ok(review);
    }


    //api tạo pcrt-06
    @PostMapping("/aml-staff/pcrt-06")
    public void createPCRT_06 () throws IOException {

        PCRT_06 pcrt_06 = new PCRT_06();
        pcrt_06.setCreatedDate(LocalDate.now());
        pcrt_06 = pcrt06Repository.save(pcrt_06);

        List<Review> reviewList = reviewService.reviewList("pass");
        reviewList.removeIf(r ->
                (r.getCustomer().getType().equals("cá nhân") && r.getCustomer().getPcrt_03_forms().isEmpty()) ||
                        (!r.getCustomer().getType().equals("cá nhân") && r.getCustomer().getPcrt_04_forms().isEmpty())
        );

        List<PCRT_06_item> items = new ArrayList<>();

        for(Review r : reviewList){
            PCRT_06_item item = new PCRT_06_item();
            item.setPcrt_06(pcrt_06);
            item.setCustomer(r.getCustomer());
            item.setAmount(transactionService.getTransactionAmountOfDateByCustomerId(r.getCustomer().getId()));
            item.setFrequency(transactionService.getFrequencyOfDateByCustomerId(r.getCustomer().getId()));

            pcrt06ItemRepository.save(item);
            items.add(item);

            r.setStatus("is_used");
            reviewRepository.save(r);
        }
        pcrt_06.setItemList(items);
        pcrt_06.setFilePath(pcrt06Service.uploadPCRT06(pcrt_06));
        pcrt06Repository.save(pcrt_06);
    }

    //api tạo pcrt-07
    @PostMapping("/aml-staff/pcrt-07")
    public void createPCRT_07() throws IOException {

        PCRT_07 pcrt_07 = new PCRT_07();
        pcrt_07.setCreatedDate(LocalDate.now());
        pcrt_07 = pcrt07Repository.save(pcrt_07);

        List<Review> reviewList = reviewService.reviewList("pass");
        reviewList.removeIf(r ->
                (r.getCustomer().getType().equals("cá nhân") && !r.getCustomer().getPcrt_03_forms().isEmpty()) ||
                        (!r.getCustomer().getType().equals("cá nhân") && !r.getCustomer().getPcrt_04_forms().isEmpty())
        );

        List<PCRT_07_item> items = new ArrayList<>();

        for(Review r : reviewList){
            PCRT_07_item item = new PCRT_07_item();
            item.setPcrt_07(pcrt_07);
            item.setCustomer(r.getCustomer());
            item.setAmount(transactionService.getTransactionAmountOfDateByCustomerId(r.getCustomer().getId()));
            item.setFrequency(transactionService.getFrequencyOfDateByCustomerId(r.getCustomer().getId()));

            pcrt07ItemRepository.save(item);
            items.add(item);

            r.setStatus("is_used");
            reviewRepository.save(r);
        }
        pcrt_07.setItemList(items);
        pcrt_07.setFilePath(pcrt07Service.uploadPCRT07(pcrt_07));
        pcrt07Repository.save(pcrt_07);
    }


    @PostMapping("/aml-manager/pcrt-06/{pcrt06Id}/update")
    public String updatePCRT_06 (Model model, @PathVariable(name = "pcrt06Id") int pcrt06Id) throws IOException {
        pcrt06Service.updatePCRT06(pcrt06Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "aml-manager/pcrt_06";
    }

    @PostMapping("/aml-manager/pcrt-07/{pcrt07Id}/update")
    public String updatePCRT_07 (Model model, @PathVariable(name = "pcrt07Id") int pcrt07Id) throws IOException {
        pcrt07Service.updatePCRT07(pcrt07Id);
        System.out.println("Controller: update thành công !");
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "aml-manager/pcrt_07";
    }


}
