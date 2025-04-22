package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class PCRT_04_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "business_name")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String businessName;
    @Column(name = "foreign_name")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String foreignName;
    @Column(name = "short_name")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String shortName;
    @Column(name = "parent_organization_name")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String parentOrganizationName;
    @ManyToOne
    @JoinColumn(name = "address_id")
    @Valid
    private Address address;
    @ManyToOne
    @JoinColumn(name = "establishment_license_id")
    @Valid
    private License establishmentLicense;
    @ManyToOne
    @JoinColumn(name = "business_registration_license_id")
    @Valid
    private License businessRegistrationLicense;
    @Column(name = "tax_code")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    @Size(min = 10, max = 13, message = "Mã số thuế phải có tối thiểu 10 và tối đa 13 kí tự")
    private String taxCode;
    @Column(name = "business_occupation")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String businessOccupation;
    @Column(name = "phone_number")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String phoneNumber;
    @Column(name = "fax_number")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String faxNumber;
    @Column(name = "customer_occupation")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String customerOccupation;
    @ManyToOne
    @JoinColumn(name = "customer_permanent_address_id")
    @Valid
    private Address customerPermanentAddress;
    @ManyToOne
    @JoinColumn(name = "customer_current_address_id")
    @Valid
    private Address customerCurrentAddress;
    @Column(name = "customer_office_phone")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String customerOfficePhone;
    @Column(name = "customer_mobile_phone")
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String customerMobilePhone;
    @ManyToOne
    @JoinColumn(name = "pcrt_04_id")
    private PCRT_04 pcrt04;

    public PCRT_04_detail() {
        this.establishmentLicense = new License("establishment");
        this.businessRegistrationLicense = new License("business_registration");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public License getEstablishmentLicense() {
        return establishmentLicense;
    }

    public void setEstablishmentLicense(License establishmentLicense) {
        this.establishmentLicense = establishmentLicense;
    }

    public License getBusinessRegistrationLicense() {
        return businessRegistrationLicense;
    }

    public void setBusinessRegistrationLicense(License businessRegistrationLicense) {
        this.businessRegistrationLicense = businessRegistrationLicense;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getBusinessOccupation() {
        return businessOccupation;
    }

    public void setBusinessOccupation(String businessOccupation) {
        this.businessOccupation = businessOccupation;
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

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public Address getCustomerPermanentAddress() {
        return customerPermanentAddress;
    }

    public void setCustomerPermanentAddress(Address customerPermanentAddress) {
        this.customerPermanentAddress = customerPermanentAddress;
    }

    public Address getCustomerCurrentAddress() {
        return customerCurrentAddress;
    }

    public void setCustomerCurrentAddress(Address customerCurrentAddress) {
        this.customerCurrentAddress = customerCurrentAddress;
    }

    public String getCustomerOfficePhone() {
        return customerOfficePhone;
    }

    public void setCustomerOfficePhone(String customerOfficePhone) {
        this.customerOfficePhone = customerOfficePhone;
    }

    public String getCustomerMobilePhone() {
        return customerMobilePhone;
    }

    public void setCustomerMobilePhone(String customerMobilePhone) {
        this.customerMobilePhone = customerMobilePhone;
    }

    public PCRT_04 getPcrt04() {
        return pcrt04;
    }

    public void setPcrt04(PCRT_04 pcrt04) {
        this.pcrt04 = pcrt04;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getParentOrganizationName() {
        return parentOrganizationName;
    }

    public void setParentOrganizationName(String parentOrganizationName) {
        this.parentOrganizationName = parentOrganizationName;
    }

    public PCRT_04_detail(String businessName, String foreignName, Address address, License establishmentLicense, License businessRegistrationLicense, String taxCode, String businessOccupation, String phoneNumber, String faxNumber, String customerOccupation, Address customerPermanentAddress, Address customerCurrentAddress, String customerOfficePhone, String customerMobilePhone) {
        this.businessName = businessName;
        this.foreignName = foreignName;
        this.address = address;
        this.establishmentLicense = establishmentLicense;
        this.businessRegistrationLicense = businessRegistrationLicense;
        this.taxCode = taxCode;
        this.businessOccupation = businessOccupation;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.customerOccupation = customerOccupation;
        this.customerPermanentAddress = customerPermanentAddress;
        this.customerCurrentAddress = customerCurrentAddress;
        this.customerOfficePhone = customerOfficePhone;
        this.customerMobilePhone = customerMobilePhone;
    }
}
