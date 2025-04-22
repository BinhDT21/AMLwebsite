package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "license")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    @Size(max = 10, message = "Mã số tối đa 10 kí tự")
    private String number;
    @Column(name = "issued_date")
    private LocalDate issuedDate;
    @Column(name = "issued_place")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String issuedPlace;

    // establishment / business_registration
    private String type;

    @Transient
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String issuedDateTmp;

    public License(String number, LocalDate issuedDate, String issuedPlace, String type) {
        this.number = number;
        this.issuedDate = issuedDate;
        this.issuedPlace = issuedPlace;
        this.type = type;
    }

    public License() {
    }

    public License(String type ) {
        this.type = type;
    }

    public License(int id, String number, LocalDate issuedDate, String issuedPlace, String type) {
        this.id = id;
        this.number = number;
        this.issuedDate = issuedDate;
        this.issuedPlace = issuedPlace;
        this.type = type;
    }

    public String getIssuedDateTmp() {
        return issuedDateTmp;
    }

    public void setIssuedDateTmp(String issuedDateTmp) {
        this.issuedDateTmp = issuedDateTmp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssuedPlace() {
        return issuedPlace;
    }

    public void setIssuedPlace(String issuedPlace) {
        this.issuedPlace = issuedPlace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
