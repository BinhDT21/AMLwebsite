package com.pcrt.Pcrt.dto.request.pcrt_04;

import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.PCRT_04_detail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BusinessCustomerUpdateRequest {

    @Valid
    private PCRT_04_detail pcrt04Detail;
    @Valid
    private Customer customer;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String nam_thu_nhat;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String nam_thu_hai;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String tong_doanh_thu;
    @NotNull(message = "Yêu cầu nhập thông tin")
    private BigDecimal doanh_thu_nam_nhat;
    @NotNull(message = "Yêu cầu nhập thông tin")
    private BigDecimal doanh_thu_nam_hai;
    private String thong_tin_lien_quan_nguon_tien;
    private List<HoiDongQuanTri> danh_sach_hdqt;
    private List<BanDieuHanh> danh_sach_bdh;
    @Valid
    private KeToanTruong ke_toan_truong;

    public BusinessCustomerUpdateRequest() {
        this.pcrt04Detail = new PCRT_04_detail();
        this.nam_thu_nhat = String.valueOf(LocalDate.now().getYear()-1);
        this.nam_thu_hai = String.valueOf(LocalDate.now().getYear()-2);
        this.danh_sach_hdqt = new ArrayList<>();
        this.danh_sach_bdh = new ArrayList<>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PCRT_04_detail getPcrt04Detail() {
        return pcrt04Detail;
    }

    public void setPcrt04Detail(PCRT_04_detail pcrt04Detail) {
        this.pcrt04Detail = pcrt04Detail;
    }

    public String getNam_thu_nhat() {
        return nam_thu_nhat;
    }

    public void setNam_thu_nhat(String nam_thu_nhat) {
        this.nam_thu_nhat = nam_thu_nhat;
    }

    public String getNam_thu_hai() {
        return nam_thu_hai;
    }

    public void setNam_thu_hai(String nam_thu_hai) {
        this.nam_thu_hai = nam_thu_hai;
    }

    public String getTong_doanh_thu() {
        return tong_doanh_thu;
    }

    public void setTong_doanh_thu(String tong_doanh_thu) {
        this.tong_doanh_thu = tong_doanh_thu;
    }

    public BigDecimal getDoanh_thu_nam_nhat() {
        return doanh_thu_nam_nhat;
    }

    public void setDoanh_thu_nam_nhat(BigDecimal doanh_thu_nam_nhat) {
        this.doanh_thu_nam_nhat = doanh_thu_nam_nhat;
    }

    public BigDecimal getDoanh_thu_nam_hai() {
        return doanh_thu_nam_hai;
    }

    public void setDoanh_thu_nam_hai(BigDecimal doanh_thu_nam_hai) {
        this.doanh_thu_nam_hai = doanh_thu_nam_hai;
    }

    public String getThong_tin_lien_quan_nguon_tien() {
        return thong_tin_lien_quan_nguon_tien;
    }

    public void setThong_tin_lien_quan_nguon_tien(String thong_tin_lien_quan_nguon_tien) {
        this.thong_tin_lien_quan_nguon_tien = thong_tin_lien_quan_nguon_tien;
    }

    public List<HoiDongQuanTri> getDanh_sach_hdqt() {
        return danh_sach_hdqt;
    }

    public void setDanh_sach_hdqt(List<HoiDongQuanTri> danh_sach_hdqt) {
        this.danh_sach_hdqt = danh_sach_hdqt;
    }

    public List<BanDieuHanh> getDanh_sach_bdh() {
        return danh_sach_bdh;
    }

    public void setDanh_sach_bdh(List<BanDieuHanh> danh_sach_bdh) {
        this.danh_sach_bdh = danh_sach_bdh;
    }

    public KeToanTruong getKe_toan_truong() {
        return ke_toan_truong;
    }

    public void setKe_toan_truong(KeToanTruong ke_toan_truong) {
        this.ke_toan_truong = ke_toan_truong;
    }


}
