package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.dto.request.*;
import com.pcrt.Pcrt.dto.request.pcrt_03.IndividualCustomerUpdateRequest;
import com.pcrt.Pcrt.dto.request.pcrt_04.BanDieuHanh;
import com.pcrt.Pcrt.dto.request.pcrt_04.BusinessCustomerUpdateRequest;
import com.pcrt.Pcrt.dto.request.pcrt_04.HoiDongQuanTri;
import com.pcrt.Pcrt.dto.response.CustomerSaveResponse;
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.repository.TransactionRepository;
import com.pcrt.Pcrt.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/gdv")
public class GDVController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private KqksService kqksService;
    @Autowired
    private Pcrt03_Service pcrt03Service;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private Pcrt04_Service pcrt04Service;
    @Autowired
    private Pcrt06_Service pcrt06Service;
    @Autowired
    private Pcrt07_Service pcrt07Service;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/customer-checking")
    public String customerChecking(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("customer", new CustomerCheckingRequest());
        return "gdv/customer_checking";
    }


    @PostMapping("/customer-checking/result")
    public String checking(Model model, @ModelAttribute(value = "customer") @Valid CustomerCheckingRequest request,
                           BindingResult bindingResult) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        //validate unique customer//
        Customer customerGetByNationalId = customerService.getCustomerByNationalId(request.getNationalId());
        Customer customerGetByPassport = customerService.getCustomerByPassport(request.getPassport());

        if (customerGetByNationalId != null) {
            model.addAttribute("duplicateNationalId", "Khách hàng với số CCCD này đã tồn tại !");
        }
        if (customerGetByPassport != null) {
            model.addAttribute("duplicatePassport", "Khách hàng với số hộ chiếu này đã tồn tại !");
        }
        if (customerGetByPassport != null || customerGetByNationalId != null) {
            return "gdv/customer_checking";
        }
        //end validate unique customer//

        if (!bindingResult.hasErrors()) {

            model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(request));

            CustomerSaveRequest customerSaveRequest = new CustomerSaveRequest(request, "", "");
            model.addAttribute("customer", customerSaveRequest);

            return "gdv/customer_checking_result";
        }

        return "gdv/customer_checking";
    }

    @PostMapping("/save-customer")
    public String saveCustomer(Model model, @ModelAttribute(value = "customer") CustomerSaveRequest customer,
                               @RequestParam Map<String, String> params) throws IOException {

        model.addAttribute("currentUser", userService.getCurrentUser());
        CustomerSaveResponse customerSaveResponse = customerService.saveCustomer(customer, params);

        // lưu đối tượng UserCustomer
        UserCustomer userCustomer = userCustomerService.saveUserCustomer(customerSaveResponse.getCustomer(), userService.getCurrentUser());

        //lưu khách hàng và kết quả khảo sát vào hồ sơ của khách hàng
        File f = kqksService.createFile(blacklistService
                .getBlacklistByCustomer(customerSaveResponse.getCustomer()), customerSaveResponse.getCustomer());
        kqksService.uploadCloud(f, customerSaveResponse.getCustomer());

        return "redirect:/gdv/customers";
    }

    //bam nut yeu cau nhan dang se vao controller nay
    @PostMapping("/checking-request")
    public String checkingRequest(Model model, @ModelAttribute(value = "customer") Customer customer) throws IOException {
        if (customerService.getCustomerById(customer.getId()) != null) {
            System.out.println("Khách hàng này đã tồn tại");
            customer.setStatus("waiting");
            customerService.saveCustomer(customer, null);//lưu khách hàng sau đó lưu đối tượng UserCustomer
            userCustomerService.saveUserCustomer(customer, userService.getCurrentUser());// đối tượng hiện tại sẽ có role GDV
            //lưu khách hàng và kết quả khảo sát vào hồ sơ của khách hàng
            File f = kqksService.createFile(blacklistService
                    .getBlacklistByCustomer(customer), customer);
            kqksService.uploadCloud(f, customer);
        } else {
            System.out.println("Khách hàng này chưa tồn tại");
            Map<String, String> params = new HashMap<>();
            params.put("status", "waiting");
            CustomerSaveResponse response = customerService.saveCustomer(customer, params);
            userCustomerService.saveUserCustomer(response.getCustomer(), userService.getCurrentUser());// đối tượng hiện tại sẽ có role GDV
            //lưu khách hàng và kết quả khảo sát vào hồ sơ của khách hàng
            File f = kqksService.createFile(blacklistService
                    .getBlacklistByCustomer(response.getCustomer()), response.getCustomer());
            kqksService.uploadCloud(f, response.getCustomer());
        }
        return "redirect:/gdv/customers";
    }


    @GetMapping("/customers")
    public String customers(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;
        Page<Customer> customers = customerService.getCustomers(params);

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages",customers.getTotalPages() );
        return "gdv/customers";
    }

    @GetMapping("/customers/result/{id}")
    public String customerCheckedDetail(Model model, @PathVariable(value = "id") int id) {
        Customer customer = customerService.getCustomerById(id);

        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("customer", customer);
        model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(customer));
        return "gdv/ksv_checking_result";
    }

    @PostMapping("/customer/checked/save")
    public String saveCustomerCheckedDetail(Model model, @ModelAttribute(value = "customer") Customer customer,
                                            @RequestParam Map<String, String> params) {

        CustomerSaveResponse response = customerService.saveCustomer(customer, params);
        userCustomerService.saveUserCustomer(response.getCustomer(), userService.getCurrentUser());

        model.addAttribute("currentUser", userService.getCurrentUser());

        return "redirect:/gdv/customers";
    }


    @GetMapping("/individual-customer/update/{id}")
    public String getUpdateIndividualCustomer(Model model, @PathVariable(value = "id") int id) {

        Customer customer = customerService.getCustomerById(id);

        if (customer.getType().equals("cá nhân")) {
            // nếu khách hàng cá nhân add model sau đó trả về View "gdv/individual_customer_update_info"

            //transient PCRT_03
            PCRT_03 pcrt03 = new PCRT_03(customer, "", userService.getCurrentUser(), LocalDateTime.now());
            PCRT_03_detail updateRequest = new PCRT_03_detail();
            updateRequest.setPcrt03(pcrt03);
            updateRequest.setNationalIdentify(new Identifier(customer.getNationalId(), "national_id"));
            updateRequest.setPassportIdentify(new Identifier(customer.getPassport(), "passport"));

            model.addAttribute("customerUpdateRequest", updateRequest);
        } else {
            return "redirect:/gdv/business-customer/update/" + id;
        }

        model.addAttribute("currentUser", userService.getCurrentUser());

        return "gdv/individual_customer_update_info";
    }

    @PostMapping("/individual-customer/update")
    public String postUpdateIndividualCustomer(Model model,
                                               @ModelAttribute(value = "customerUpdateRequest") @Valid PCRT_03_detail updateRequest,
                                               BindingResult result,
                                               @RequestParam Map<String, String> params) throws IOException {

        Customer customer = customerService.getCustomerById(updateRequest.getPcrt03().getCustomer().getId());
        model.addAttribute("currentUser", userService.getCurrentUser());

        //validate unique customer//
        Customer customerGetByNationalId = customerService.getCustomerByNationalId(updateRequest.getNationalIdentify().getNumber());
        Customer customerGetByPassport = customerService.getCustomerByPassport(updateRequest.getPassportIdentify().getNumber());

        if (customerGetByNationalId != null && customerGetByNationalId.getId() != customer.getId()) {
            model.addAttribute("duplicateNationalId", "Khách hàng với số CCCD này đã tồn tại !");
        }
        if (customerGetByPassport != null && customerGetByPassport.getId() != customer.getId()) {
            model.addAttribute("duplicatePassport", "Khách hàng với số hộ chiếu này đã tồn tại !");
        }
        if (model.getAttribute("duplicateNationalId") != null || model.getAttribute("duplicatePassport") != null) {

            return "gdv/individual_customer_update_info";
        }
        //end validate unique customer//

        if (!result.hasErrors()) {

            customer.setName(updateRequest.getPcrt03().getCustomer().getName());
            customer.setNationalId(updateRequest.getNationalIdentify().getNumber());
            customer.setAddress(updateRequest.getCurrentAddress().getDistrict());
            customer.setCity(updateRequest.getCurrentAddress().getProvince());
            customer.setCountry(updateRequest.getCurrentAddress().getCountry());
            customer.setPassport(updateRequest.getPassportIdentify().getNumber());
            customer.setDob(updateRequest.getPcrt03().getCustomer().getDob());

            //updateRequest == pcrt-03-detail
            File f = pcrt03Service.createFile(updateRequest, customer);  //Tạo file local
            PCRT_03 savedPCRT_03 = pcrt03Service.uploadCloud(f, customer); //upload cloud và lưu PCRT-03, PCRT-03 detail

            pcrt03Service.savePcrt03Detail(updateRequest, savedPCRT_03); //save detail

            //tao request de lay danh sach blacklist
            CustomerCheckingRequest request = new CustomerCheckingRequest(customer);
            //lay danh sach blacklist
            model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(request));

            model.addAttribute("customer", customer);

            model.addAttribute("currentUser", userService.getCurrentUser());
            return "gdv/customer_checking_result";
        } else {
            return "gdv/individual_customer_update_info";
        }

    }

    // chuyển đến View nhập thông tin khách hàng doanh nghiệp
    @GetMapping("/business-customer/update/{customerId}")
    public String getUpdateBusinessCustomer(Model model,
                                            @PathVariable(value = "customerId") int customerId) {


        model.addAttribute("currentUser", userService.getCurrentUser());

        Customer customer = customerService.getCustomerById(customerId);
        BusinessCustomerUpdateRequest request = new BusinessCustomerUpdateRequest();
        request.setCustomer(customer);

        model.addAttribute("request", request);

        return "gdv/business_customer_update_info";
    }

    // Nhận thông tin cập nhật
    @PostMapping("/business-customer/update")
    public String postUpdateBusinessCustomer(Model model, @ModelAttribute(name = "request") @Valid BusinessCustomerUpdateRequest request, BindingResult result) throws IOException {
        model.addAttribute("currentUser", userService.getCurrentUser());


        //validate unique customer//
        Customer customerGetByNationalId = customerService.getCustomerByNationalId(request.getCustomer().getNationalId());
        Customer customerGetByPassport = customerService.getCustomerByPassport(request.getCustomer().getPassport());

        if (customerGetByNationalId != null && customerGetByNationalId.getId() != request.getCustomer().getId()) {
            model.addAttribute("duplicateNationalId", "Khách hàng với số CCCD này đã tồn tại !");
        }
        if (customerGetByPassport != null && customerGetByPassport.getId() != request.getCustomer().getId()) {
            model.addAttribute("duplicatePassport", "Khách hàng với số hộ chiếu này đã tồn tại !");
        }
        if (model.getAttribute("duplicateNationalId") != null || model.getAttribute("duplicatePassport") != null) {

            return "gdv/business_customer_update_info";
        }
        //end validate unique customer//

        if (!result.hasErrors()) {
            Customer customer = customerService.getCustomerById(request.getCustomer().getId());
            customer.setName(request.getCustomer().getName());
            customer.setDob(request.getCustomer().getDob());
            customer.setCountry(request.getCustomer().getCountry());
            customer.setNationalId(request.getCustomer().getNationalId());
            customer.setPassport(request.getCustomer().getPassport());

            PCRT_04 pcrt04 = new PCRT_04(customer, "", LocalDateTime.now());
            pcrt04.setGdv(userService.getCurrentUser());

            File f = pcrt04Service.createFile(request, customer, pcrt04);  //tạo file
            PCRT_04 savedPCRT04 = pcrt04Service.uploadCloud(f, customer, pcrt04);         //lưu: updated customer và pcr04

            pcrt04Service.savePcrt04Detail(request, savedPCRT04);//lưu pcrt04 detail

            model.addAttribute("checkingResult", blacklistService.getBlacklistByCustomer(request.getCustomer()));
            model.addAttribute("customer", customer);

            return "gdv/customer_checking_result";
        } else {
            Customer customer = customerService.getCustomerById(request.getCustomer().getId());
            request.setCustomer(customer);

            return "gdv/business_customer_update_info";
        }
    }


    @GetMapping("/pcrt-06")
    public String PCRT_06(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_06> pcrt06List = pcrt06Service.getListPCRT_06(params);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        model.addAttribute("pcrt06List", pcrt06List);
        model.addAttribute("totalPages", pcrt06List.getTotalPages());
        model.addAttribute("currentPage", page);

        return "gdv/pcrt_06";
    }

    @GetMapping("/pcrt-07")
    public String PCRT_07(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<PCRT_07> pcrt07List = pcrt07Service.getListPCRT_07(params);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;

        model.addAttribute("pcrt07List", pcrt07List);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pcrt07List.getTotalPages());

        return "gdv/pcrt_07";
    }

    @GetMapping("/transaction")
    public String createTransaction(Model model, @RequestParam Map<String, String> params) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("createdDate", formatter.format(LocalDate.now()));


        if (params.get("transaction-id") != null && !params.get("transaction-id").isEmpty()) {
            int transactionId = Integer.parseInt(params.get("transaction-id"));
            Transaction transaction = transactionService.getTransactionById(transactionId);
            CreateTransactionRequest request = new CreateTransactionRequest(transaction.getCustomer().getNationalId(), String.valueOf(transaction.getAmount()), transactionId);
            model.addAttribute("transaction", request);
        } else {
            //bind transacion
            model.addAttribute("transaction", new CreateTransactionRequest());
        }

        return "gdv/transaction_create";
    }


    @PostMapping("/transaction/create")
    public String createTransaction(Model model, @ModelAttribute(value = "transaction") @Valid CreateTransactionRequest createRequest,
                                    BindingResult bindingResult) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        if(bindingResult.hasErrors()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            model.addAttribute("createdDate", formatter.format(LocalDate.now()));

            return "gdv/transaction_create";
        }

        if (createRequest.getTransactionId() == 0) {
            Transaction transaction = new Transaction();

            transaction.setUser(userService.getCurrentUser());
            transaction.setCreatedDate(LocalDate.now());
            transaction.setBranch(userService.getCurrentUser().getBranch());
            transaction.setAmount(new BigDecimal(createRequest.getAmount()));
            transaction.setCustomer(customerService.getCustomerByNationalId(createRequest.getNationalId()));

            int frequency = transactionService.getFrequencyOfDateByCustomerId2(transaction.getCustomer().getId());
            BigDecimal amount = transactionService.getTransactionAmountOfDateByCustomerId2(transaction.getCustomer().getId());

            if (frequency >= 20) {
                System.out.println("Cảnh báo quá số lần giao dịch trong ngày");
                transaction.setStatus("risk");
                Transaction savedTransaction = transactionRepository.save(transaction);
                return "redirect:/gdv/transactions/" + savedTransaction.getId() + "/create/warning?error=true&frequency=" + frequency;
            } else if (amount.compareTo(new BigDecimal("400000000")) >= 0) {
                System.out.println("Cảnh báo quá số tiền giao dịch trong ngày");
                transaction.setStatus("risk");
                Transaction savedTransaction = transactionRepository.save(transaction);
                return "redirect:/gdv/transactions/" + savedTransaction.getId() + "/create/warning?error=true&totalAmount=" + amount;
            } else {
                System.out.println("Không có cảnh báo");
                transaction.setStatus("pending");
                transactionRepository.save(transaction);

                return "redirect:/gdv/transactions?status=finish";
            }
        } else {
            Transaction transaction = transactionService.getTransactionById(createRequest.getTransactionId());
            transaction.setStatus("pending");
            transactionRepository.save(transaction);

            return "redirect:/gdv/transactions?status=finish";
        }

    }


    @GetMapping("/transactions")
    public String transactions(Model model, @RequestParam Map<String, String> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("currentUser", userService.getCurrentUser());

        Page<Transaction> transactions = transactionService.getTransactionList(params);
        for (Transaction t : transactions) {
            t.setCreatedDateTmp(formatter.format(t.getCreatedDate()));
        }

        //format amount
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        for(Transaction t: transactions){
            double amount = t.getAmount().doubleValue();
            t.setAmountTmp(numberFormat.format(amount));
        }

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int totalPages = transactions.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("transactions", transactions);

        return "gdv/transactions";
    }

    @GetMapping("/transactions/{transactionId}/create/warning")
    public String transactionWarning(Model model, @PathVariable(value = "transactionId") int transactionId) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("không tìm thấy Transaction"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        transaction.setCreatedDateTmp(formatter.format(transaction.getCreatedDate()));
        model.addAttribute("transaction", transaction);

        return "gdv/transaction_warning";
    }

    @GetMapping("/report/create/{transactionId}")
    public String createReport(Model model, @PathVariable(value = "transactionId") int transactionId) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        model.addAttribute("createdDate", formatter.format(LocalDateTime.now()));

        Transaction transaction = transactionService.getTransactionById(transactionId);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        transaction.setCreatedDateTmp(formatter2.format(transaction.getCreatedDate()));

        //format các kqks.createdDate
        for (Kqks k : transaction.getCustomer().getKqksList()) {
            k.setCreatedDateTmp(formatter.format(k.getCreatedDate()));
        }
        for (PCRT_03 form : transaction.getCustomer().getPcrt_03_forms()) {
            form.setCreatedDateTmp(formatter.format(form.getCreatedDate()));
        }
        for (PCRT_04 form : transaction.getCustomer().getPcrt_04_forms()) {
            form.setCreatedDateTmp(formatter.format(form.getCreatedDate()));
        }
        model.addAttribute("transaction", transaction);


        return "gdv/report_create";
    }


    @GetMapping("/transactions/transaction-detail/{transactionId}")
    public String transactionDetail(Model model, @PathVariable(value = "transactionId") int id) {
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

        return "gdv/transaction_detail";
    }



}
