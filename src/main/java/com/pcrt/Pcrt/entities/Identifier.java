package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "identifier")
public class Identifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    @Size(min = 6, max = 20, message = "Thông tin không hợp lệ")
    private String number;
    private String type;
    @Column(name = "issued_date")
    private LocalDate issuedDate;
    @Column(name = "issued_place")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String issuedPlace;
    @Transient
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String issuedDateTmp;

    public Identifier() {
    }

    public Identifier(String number, String type){
        this.number = number;
        this.type   = type;
    }


    public Identifier(int id, String number, String type, LocalDate issuedDate, String issuedPlace) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.issuedDate = issuedDate;
        this.issuedPlace = issuedPlace;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIssuedDateTmp() {
        return issuedDateTmp;
    }

    public void setIssuedDateTmp(String issuedDateTmp) {
        this.issuedDateTmp = issuedDateTmp;
    }
}
