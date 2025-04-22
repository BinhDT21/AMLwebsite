package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.dto.request.CustomerSaveRequest;
import com.pcrt.Pcrt.dto.response.CustomerDetailResponse;
import com.pcrt.Pcrt.dto.response.CustomerSaveResponse;
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.repository.CustomerRepository;
import com.pcrt.Pcrt.repository.query.CustomerRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerRepositoryQuery customerRepositoryCriteria;
    @Autowired
    private KqksService kqksService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCustomerService userCustomerService;
    @Autowired
    private Pcrt03_Service pcrt03Service;
    @Autowired
    private Pcrt04_Service pcrt04Service;



    public Page<Customer> getCustomers(Map<String, String> params) {

        List<Customer> customers = customerRepositoryCriteria.getCustomers(params);
        long totalCustomers = customerRepositoryCriteria.countCustomer(params);
        int page = params.get("page") != null ? Integer.parseInt(params.get("page")):0;

        Page<Customer> pageCustomer = new PageImpl<>(customers, PageRequest.of(page, 10), totalCustomers );
        return pageCustomer;
    }

    public CustomerSaveResponse saveCustomer(CustomerSaveRequest request, Map<String, String> params) {
        Customer customer = new Customer();
        {
            customer.setId(request.getId());
            customer.setNationalId(request.getNationalId());
            customer.setName(request.getName());
            customer.setAddress(request.getAddress());
            customer.setCity(request.getCity());
            customer.setCountry(request.getCountry());
            customer.setPassport(request.getPassport());
            customer.setType(request.getType());
            customer.setDob(request.getDob());
            customer.setRiskClassification(request.getRiskClassification());
            customer.setStatus(request.getStatus());
        }
        if (this.getCustomerById(request.getId()) == null) {
            //set status by param
            String statusState = params.get("status");
            if (!statusState.isEmpty()) {
                switch (statusState) {
                    case "waiting":
                        customer.setStatus("waiting");
                        break;
                    case "checked":
                        customer.setStatus("checked");
                        break;
                    default:
                        customer.setStatus("saved");
                        break;
                }
            }
        }

        this.customerRepository.save(customer);

        return new CustomerSaveResponse("Lưu khách hàng thành công", customer);
    }

    public CustomerSaveResponse saveCustomer(Customer customer, Map<String, String> params) {

        if (this.getCustomerById(customer.getId()) == null) {
            //set status by param
            String statusState = params.get("status");
            if (!statusState.isEmpty()) {
                switch (statusState) {
                    case "waiting":
                        customer.setStatus("waiting");
                        break;
                    case "checked":
                        customer.setStatus("checked");
                        break;
                    default:
                        customer.setStatus("saved");
                        break;
                }
            }
        }

        customerRepository.save(customer);


        return new CustomerSaveResponse("Lưu khách hàng thành công", customer);
    }


    public Page<Customer> loadCustomerByStatus(Map<String,String> params) {

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        long totalPages = customerRepositoryCriteria.countCustomer(params);
        List<Customer> customerList = customerRepositoryCriteria.getCustomers(params);

        return new PageImpl<>(customerList, PageRequest.of(page, 10), totalPages);
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer getCustomerByNationalId(String nationalId) {
        return customerRepositoryCriteria.getCustomerByNationalId(nationalId)
                .orElse(null);
    }

    public Customer getCustomerByPassport (String passport){
        return customerRepositoryCriteria.getCustomerByPassport(passport)
                .orElse(null);
    }

    public void removeCustomer(Customer customer) throws IOException {
        pcrt04Service.deletePCRT_04ByCustomer(customer.getId());
        pcrt03Service.deletePCRT_03ByCustomer(customer.getId());
        kqksService.deleteKqksByCustomer(customer.getId());
        userCustomerService.deleteUserCustomerByCustomer(customer.getId());
        customerRepository.delete(customer);
    }

    public List getAdditionalInfoFormByCustomer(int customerId) {
        Customer customer = getCustomerById(customerId);
        if (customer != null) {
            switch (customer.getType()) {
                case "cá nhân":
                    return pcrt03Service.getPCRT_03ListByCustomerId(customerId);
                case "doanh nghiệp":
                    return pcrt04Service.getPCRT_04ListByCustomerId(customerId);
            }
        }
        return null;
    }

    public Set<User> getUsersByCustomer(int customerId) {
        return customerRepositoryCriteria.getUsersByCustomer(customerId);
    }

    public CustomerDetailResponse customerDetailResponse(int customerId) {

        Customer customer = getCustomerById(customerId);
        List<Kqks> kqksList = kqksService.getListKqksByCustomer(customerId);
        List additionalInfoForm = new ArrayList();
        if (customer.getType().equals("cá nhân")) {
            additionalInfoForm = pcrt03Service.getPCRT_03ListByCustomerId(customerId);
        } else if (customer.getType().equals("doanh nghiệp")) {
            additionalInfoForm = pcrt04Service.getPCRT_04ListByCustomerId(customerId);
        }

        return new CustomerDetailResponse(customer, kqksList, additionalInfoForm);
    }

}
