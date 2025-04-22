package com.pcrt.Pcrt.dto.response;

import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Kqks;

import java.util.List;

public class CustomerDetailResponse {

    private Customer customer;
    private List<Kqks> kqksList;
    private List additionalInfoForm;

    public CustomerDetailResponse(Customer customer, List<Kqks> kqksList, List additionalInfoForm) {
        this.customer = customer;
        this.kqksList = kqksList;
        this.additionalInfoForm = additionalInfoForm;
    }

    public CustomerDetailResponse() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Kqks> getKqksList() {
        return kqksList;
    }

    public void setKqksList(List<Kqks> kqksList) {
        this.kqksList = kqksList;
    }

    public List getAdditionalInfoForm() {
        return additionalInfoForm;
    }

    public void setAdditionalInfoForm(List additionalInfoForm) {
        this.additionalInfoForm = additionalInfoForm;
    }

    @Override
    public String toString() {
        return "CustomerDetailResponse{" +
                "customer=" + customer +
                ", kqksList=" + kqksList +
                ", additionalInfoForm=" + additionalInfoForm +
                '}';
    }
}
