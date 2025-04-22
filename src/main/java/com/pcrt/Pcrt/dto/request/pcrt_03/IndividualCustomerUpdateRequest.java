package com.pcrt.Pcrt.dto.request.pcrt_03;

import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Opinion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IndividualCustomerUpdateRequest {
    private int customerId;
    @NotBlank(message = "Vui lòng nhập tên khách hàng")
    private String name;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String address;   // địa chỉ hiện tại
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String city;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String country;
    @Size(min = 10, max = 20, message = "Số căn cước công dân tối thiểu 10 và tối đa 20 ký tự")
    private String nationalId;
    @Size(min = 6, max = 20, message = "Hộ chiếu tối thiểu 6 đến 20 ký tự")
    private String passport;
    private String type;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String dob;
    private String riskClassification;
    private Opinion opinion;
    private String status;

    @NotBlank(message = "Vui lòng nhập thông tin")
    private String nationalIdStartDate;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String permanentAddress;
    @NotNull(message = "Vui lòng nhập thông tin")
    private Long avgSalary;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String workspaceName;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String workspaceAddress;
    @NotBlank(message = "Vui lòng nhập thông tin")
    private String workspacePhoneNumber;
    private String additionalInfo;

    //full constructor
    public IndividualCustomerUpdateRequest(int customerId, String name, String address, String city, String country, String nationalId, String passport, String type, String dob, String riskClassification, Opinion opinion, String status, String nationalIdStartDate, String permanentAddress, Long avgSalary, String workspaceName, String workspaceAddress, String workspacePhoneNumber, String additionalInfo) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.nationalId = nationalId;
        this.passport = passport;
        this.type = type;
        this.dob = dob;
        this.riskClassification = riskClassification;
        this.opinion = opinion;
        this.status = status;
        this.nationalIdStartDate = nationalIdStartDate;
        this.permanentAddress = permanentAddress;
        this.avgSalary = avgSalary;
        this.workspaceName = workspaceName;
        this.workspaceAddress = workspaceAddress;
        this.workspacePhoneNumber = workspacePhoneNumber;
        this.additionalInfo = additionalInfo;
    }

    public IndividualCustomerUpdateRequest(Customer customer, String nationalIdStartDate, String permanentAddress, Long avgSalary, String workspaceName, String workspaceAddress, String workspacePhoneNumber, String additionalInfo) {
        this.customerId = customer.getId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.city = customer.getCity();
        this.country = customer.getCountry();
        this.nationalId = customer.getNationalId();
        this.passport = customer.getPassport();
        this.type = customer.getType();
        this.dob = customer.getDob();
        this.riskClassification = customer.getRiskClassification();
        this.opinion = customer.getOpinion();
        this.status = customer.getStatus();

        this.nationalIdStartDate = nationalIdStartDate;
        this.permanentAddress = permanentAddress;
        this.avgSalary = avgSalary;
        this.workspaceName = workspaceName;
        this.workspaceAddress = workspaceAddress;
        this.workspacePhoneNumber = workspacePhoneNumber;
        this.additionalInfo = additionalInfo;
    }

    public IndividualCustomerUpdateRequest(Customer customer) {
        this.customerId = customer.getId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.city = customer.getCity();
        this.country = customer.getCountry();
        this.nationalId = customer.getNationalId();
        this.passport = customer.getPassport();
        this.type = customer.getType();
        this.dob = customer.getDob();
        this.riskClassification = customer.getRiskClassification();
        this.opinion = customer.getOpinion();
        this.status = customer.getStatus();
    }

    public IndividualCustomerUpdateRequest(){

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRiskClassification() {
        return riskClassification;
    }

    public void setRiskClassification(String riskClassification) {
        this.riskClassification = riskClassification;
    }

    public Opinion getOpinion() {
        return opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNationalIdStartDate() {
        return nationalIdStartDate;
    }

    public void setNationalIdStartDate(String nationalIdStartDate) {
        this.nationalIdStartDate = nationalIdStartDate;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Long getAvgSalary() {
        return avgSalary;
    }

    public void setAvgSalary(Long avgSalary) {
        this.avgSalary = avgSalary;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getWorkspaceAddress() {
        return workspaceAddress;
    }

    public void setWorkspaceAddress(String workspaceAddress) {
        this.workspaceAddress = workspaceAddress;
    }

    public String getWorkspacePhoneNumber() {
        return workspacePhoneNumber;
    }

    public void setWorkspacePhoneNumber(String workspacePhoneNumber) {
        this.workspacePhoneNumber = workspacePhoneNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
