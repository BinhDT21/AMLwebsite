package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.dto.request.CustomerSaveRequest;
import com.pcrt.Pcrt.dto.response.CustomerDetailResponse;
import com.pcrt.Pcrt.dto.response.CustomerSaveResponse;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Kqks;
import com.pcrt.Pcrt.entities.PCRT_03;
import com.pcrt.Pcrt.entities.PCRT_04;
import com.pcrt.Pcrt.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

//    @GetMapping("/customers")
//    public List<Customer> getCustomers (@RequestParam Map<String, String> params){
//        return this.customerService.getCustomers(params);
//    }
    @PostMapping("/save-customer")
    public CustomerSaveResponse saveCustomer (@RequestBody CustomerSaveRequest request,
                                              @RequestParam Map<String, String> params){

        return this.customerService.saveCustomer(request, params);
    }
    @DeleteMapping("/gdv/customer/delete/{id}")
    public void removeCheckedCustomer(Model model, @PathVariable(value = "id") int id) throws IOException {

        Customer customer = customerService.getCustomerById(id);
        customerService.removeCustomer(customer);
    }

    @GetMapping("/gdv/customer-detail/{customerId}")
    public CustomerDetailResponse getCustomerDetail(@PathVariable(value = "customerId") int customerId){

        CustomerDetailResponse customerDetailResponses = customerService.customerDetailResponse(customerId);


        //formatdate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Kqks k : customerDetailResponses.getKqksList()){
            k.setCreatedDateTmp(dateTimeFormatter.format(k.getCreatedDate()));
        }
        //get class name of field additionalInfoForm
        if(!customerDetailResponses.getAdditionalInfoForm().isEmpty()){
            String className = customerDetailResponses.getAdditionalInfoForm().getFirst().getClass().getSimpleName();
            switch (className){
                case "PCRT_03":{
                    for(Object p: customerDetailResponses.getAdditionalInfoForm()){
                        ((PCRT_03) p).setCreatedDateTmp(dateTimeFormatter.format(((PCRT_03) p).getCreatedDate()));
                    }
                    break;
                }
                default:{
                    for(Object p: customerDetailResponses.getAdditionalInfoForm()){
                        ((PCRT_04) p).setCreatedDateTmp(dateTimeFormatter.format(((PCRT_04) p).getCreatedDate()));
                    }
                    break;
                }
            }
        }
        return customerDetailResponses;
    }


    @GetMapping("/customer/customer-detail/{nationalId}")
    public ResponseEntity<Customer> getCustomerDetail (@PathVariable(name = "nationalId") String nationalId){
        Customer customer = customerService.getCustomerByNationalId(nationalId);
        return ResponseEntity.ok(customer);
    }

}
