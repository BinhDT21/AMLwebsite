package com.pcrt.Pcrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "pcrt_03_detail")
public class PCRT_03_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "pcrt_03_id")
    @Valid
    private PCRT_03 pcrt03;

    @ManyToOne
    @JoinColumn(name = "national_identify_id")
    @Valid
    private Identifier nationalIdentify;

    @ManyToOne
    @JoinColumn(name = "passport_id")
    @Valid
    private Identifier passportIdentify;

    @ManyToOne
    @JoinColumn(name = "current_address_id")
    @Valid
    private Address currentAddress;

    @ManyToOne
    @JoinColumn(name = "permanent_address_id")
    @Valid
    private Address permanentAddress;

    @Column(name = "average_salary")
    @NotNull(message = "Yêu cầu nhập thông tin")
    private BigDecimal averageSalary;

    @ManyToOne
    @JoinColumn(name = "office_id")
    @Valid
    private CustomerOffice office;

    private String additionalInfo;

    @NotBlank(message = "Yêu cầu nhập thông tin")
    @Size(min = 10, max=10, message = "Số điện thoại yêu cầu 10 kí tự")
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String occupation;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PCRT_03 getPcrt03() {
        return pcrt03;
    }

    public void setPcrt03(PCRT_03 pcrt03) {
        this.pcrt03 = pcrt03;
    }

    public Identifier getNationalIdentify() {
        return nationalIdentify;
    }

    public void setNationalIdentify(Identifier nationalIdentify) {
        this.nationalIdentify = nationalIdentify;
    }

    public Identifier getPassportIdentify() {
        return passportIdentify;
    }

    public void setPassportIdentify(Identifier passportIdentify) {
        this.passportIdentify = passportIdentify;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Address getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(Address permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(BigDecimal averageSalary) {
        this.averageSalary = averageSalary;
    }

    public CustomerOffice getOffice() {
        return office;
    }

    public void setOffice(CustomerOffice office) {
        this.office = office;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public PCRT_03_detail() {
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public  String getOccupation() {
        return occupation;
    }

    public void setOccupation( String occupation) {
        this.occupation = occupation;
    }

    public PCRT_03_detail(int id, PCRT_03 pcrt03, Identifier nationalIdentify, Identifier passportIdentify, Address currentAddress, Address permanentAddress, BigDecimal averageSalary, CustomerOffice office, String additionalInfo) {
        this.id = id;
        this.pcrt03 = pcrt03;
        this.nationalIdentify = nationalIdentify;
        this.passportIdentify = passportIdentify;
        this.currentAddress = currentAddress;
        this.permanentAddress = permanentAddress;
        this.averageSalary = averageSalary;
        this.office = office;
        this.additionalInfo = additionalInfo;
    }
}
