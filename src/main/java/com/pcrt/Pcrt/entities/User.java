package com.pcrt.Pcrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "[user]")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "office_phone")
    private String officePhone;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private String username;
    private String password;

    // GDV KSV AML_MANAGER AML_STAFF
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Kqks> kqksList;
    @OneToMany(mappedBy = "user")
    private List<PCRT_03> pcrt_03List;
    @OneToMany(mappedBy = "user")
    private List<UserCustomer> userCustomerList;
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactionList;
    @OneToMany(mappedBy = "staff")
    private List<Review> staffReviewList;
    @OneToMany(mappedBy = "manager")
    private List<Review> managerReviewList;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Report> report;


    public User(String name, String email, String phoneNumber,Address address,  String username, String role, Branch branch, String officePhone) {

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.role = role;
        this.branch = branch;
        this.officePhone = officePhone;
    }

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
