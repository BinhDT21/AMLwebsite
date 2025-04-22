package com.pcrt.Pcrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    @Column(name = "created_date")
    private LocalDate createdDate;
    private String district;
    private String province;
    private String country;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "fax_number")
    private String faxNumber;
    @Transient
    private String createdDateTmp;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Transaction> transactionList;
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<User> userList;

    public Branch() {
    }

    public Branch(String name, String address, LocalDate createdDate, String createdDateTmp, List<Transaction> transactionList, List<User> userList) {
        this.name = name;
        this.address = address;
        this.createdDate = createdDate;
        this.createdDateTmp = createdDateTmp;
        this.transactionList = transactionList;
        this.userList = userList;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDateTmp() {
        return createdDateTmp;
    }

    public void setCreatedDateTmp(String createdDateTmp) {
        this.createdDateTmp = createdDateTmp;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "faxNumber='" + faxNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", createdDate=" + createdDate +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
