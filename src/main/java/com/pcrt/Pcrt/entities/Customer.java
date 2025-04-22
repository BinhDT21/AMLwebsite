package com.pcrt.Pcrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String name;
    private String address;
    private String city;
    private String country;
    @Column(name = "national_id")
    private String nationalId;
    private String passport;
    private String type;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String dob;
    @Column(name = "risk_classification")
    private String riskClassification; //low, medium, high
    @OneToOne(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Opinion opinion;
    // status : saved, waiting, checked
    private String status;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<PCRT_03> pcrt_03_forms;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<PCRT_04> pcrt_04_forms;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Kqks> kqksList;
    @OneToMany(mappedBy = "customer")
    private List<UserCustomer> userCustomerList;
    @OneToMany(mappedBy = "customer")
    private List<Transaction> transactionList;
    @OneToMany(mappedBy = "customer")
    private List<Review> reviewList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Opinion getOpinion() {
        return opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public String getRiskClassification() {
        return riskClassification;
    }

    public void setRiskClassification(String riskClassification) {
        this.riskClassification = riskClassification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<PCRT_03> getPcrt_03_forms() {
        return pcrt_03_forms;
    }

    public void setPcrt_03_forms(List<PCRT_03> pcrt_03_forms) {
        this.pcrt_03_forms = pcrt_03_forms;
    }

    public List<PCRT_04> getPcrt_04_forms() {
        return pcrt_04_forms;
    }

    public void setPcrt_04_forms(List<PCRT_04> pcrt_04_forms) {
        this.pcrt_04_forms = pcrt_04_forms;
    }

    public List<Kqks> getKqksList() {
        return kqksList;
    }

    public void setKqksList(List<Kqks> kqksList) {
        this.kqksList = kqksList;
    }

    @Override
    public String toString() {

        String result = "Customer =>" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", passport='" + passport + '\'' +
                ", type='" + type + '\'' +
                ", dob='" + dob + '\'' +
                ", riskClassification='" + riskClassification + '\'' +
                ", opinion=" + opinion +
                ", status='" + status + '\'';

        if(type.equals("cá nhân")){
            result += ", additionalInfoForms='" + pcrt_03_forms + '\'';
        }else{
            result += ", additionalInfoForms='" + pcrt_04_forms + '\'';
        }

        return result;
    }
}
