package com.pcrt.Pcrt.dto.request;

import com.pcrt.Pcrt.entities.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CustomerCheckingRequest {


    @Size(min = 10, max = 20, message = "Số căn cước công dân tối thiểu 10 và tối đa 20 ký tự")
    private String nationalId;
    @NotNull(message = "Vui lòng nhập tên khách hàng")
    @NotBlank(message = "Vui lòng nhập tên khách hàng")
    private String name;
    @NotNull(message = "Vui lòng nhập địa chỉ")
    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;
    @NotNull(message = "Vui lòng nhập thành phố")
    @NotBlank(message = "Vui lòng nhập thành phố")
    private String city;
    @NotNull(message = "Vui lòng nhập quốc tịch")
    @NotBlank(message = "Vui lòng nhập quốc tịch")
    private String country;
    @Size(min = 6, max = 20, message = "Hộ chiếu tối thiểu 6 đến 20 ký tự")
    private String passport;
    private String type;
    @NotNull(message = "Vui lòng nhập ngày sinh")
    @NotBlank(message = "Vui lòng nhập ngày sinh")
    private String dob;

    public CustomerCheckingRequest(String nationalId, String name, String address, String city, String country, String passport, String type, String dob) {
        this.nationalId = nationalId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.passport = passport;
        this.type = type;
        this.dob = dob;
    }

    public CustomerCheckingRequest (Customer customer){
        this.nationalId = customer.getNationalId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.city = customer.getCity();
        this.country = customer.getCountry();
        this.passport = customer.getPassport();
        this.type = customer.getType();
        this.dob = customer.getDob();
    }

    public CustomerCheckingRequest(){

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

    @Override
    public String toString() {
        return "CustomerCheckingRequest: " + this.getName() + " " + this.getDob() + " " + this.getAddress() + " " + this.getCity() + " "
                + this.getCountry() + " " + this.getNationalId() + " " + this.getPassport() + " " + this.getType();


    }
}
