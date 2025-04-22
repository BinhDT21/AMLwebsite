package com.pcrt.Pcrt.dto.request;



public class CustomerSaveRequest {
    private int id;
    private String nationalId;
    private String name;
    private String address;
    private String city;
    private String country;
    private String passport;
    private String type;
    private String dob;
    private String riskClassification;
    private String status;

    public CustomerSaveRequest(String nationalId, String name, String address, String city, String country, String passport, String type, String dob, String riskClassification, String status) {
        this.nationalId = nationalId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.passport = passport;
        this.type = type;
        this.dob = dob;
        this.riskClassification = riskClassification;
        this.status = status;
    }

    public CustomerSaveRequest (CustomerCheckingRequest request, String riskClassification, String status){
        this.nationalId = request.getNationalId();
        this.name = request.getName();
        this.address = request.getAddress();
        this.city = request.getCity();
        this.country = request.getCountry();
        this.passport = request.getPassport();
        this.type = request.getType();
        this.dob = request.getDob();
        this.status = status;
        this.riskClassification = riskClassification;
    }

    public CustomerSaveRequest (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
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

    @Override
    public String toString() {
        return "Customer: " + name + "-" + address + "-" + city + "-" + country +
                "-" + nationalId + "-" + passport + "-" + type + "-" + dob + "-" + riskClassification + "-" + status;
    }
}
