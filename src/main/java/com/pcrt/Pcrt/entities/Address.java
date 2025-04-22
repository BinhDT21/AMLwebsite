package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String district;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String province;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String country;

    // permanent current
    private String type;

    public Address() {
    }

    public Address(String district, String province, String country, String type) {
        this.district = district;
        this.province = province;
        this.country = country;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
