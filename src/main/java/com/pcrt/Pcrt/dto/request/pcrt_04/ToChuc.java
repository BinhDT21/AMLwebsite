package com.pcrt.Pcrt.dto.request.pcrt_04;

import java.time.LocalDateTime;

public class ToChuc {
    private String ten;
    private String tenNuocNgoai;
    private String tenDonViChuQuan;
    private String diaChiTruSoGiaoDich;
    private String nganhNgheKinhDoanh;
    private long tongDoanhThu;
    private String namThuNhat;
    private String namThuHai;
    private Long doanhThuNamNhat;
    private Long doanhThuNamHai;
    private String thongTinThem;

    public ToChuc(String ten, String tenNuocNgoai, String tenDonViChuQuan, String diaChiTruSoGiaoDich, String nganhNgheKinhDoanh, long tongDoanhThu, Long doanhThuNamNhat, Long doanhThuNamHai, String thongTinThem) {
        this.ten = ten;
        this.tenNuocNgoai = tenNuocNgoai;
        this.tenDonViChuQuan = tenDonViChuQuan;
        this.diaChiTruSoGiaoDich = diaChiTruSoGiaoDich;
        this.nganhNgheKinhDoanh = nganhNgheKinhDoanh;
        this.tongDoanhThu = tongDoanhThu;
        this.namThuNhat = String.valueOf(LocalDateTime.now().getYear()-1);
        this.namThuHai = String.valueOf(LocalDateTime.now().getYear()-2);
        this.doanhThuNamNhat = doanhThuNamNhat;
        this.doanhThuNamHai = doanhThuNamHai;
        this.thongTinThem = thongTinThem;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTenNuocNgoai() {
        return tenNuocNgoai;
    }

    public void setTenNuocNgoai(String tenNuocNgoai) {
        this.tenNuocNgoai = tenNuocNgoai;
    }

    public String getTenDonViChuQuan() {
        return tenDonViChuQuan;
    }

    public void setTenDonViChuQuan(String tenDonViChuQuan) {
        this.tenDonViChuQuan = tenDonViChuQuan;
    }

    public String getDiaChiTruSoGiaoDich() {
        return diaChiTruSoGiaoDich;
    }

    public void setDiaChiTruSoGiaoDich(String diaChiTruSoGiaoDich) {
        this.diaChiTruSoGiaoDich = diaChiTruSoGiaoDich;
    }

    public String getNganhNgheKinhDoanh() {
        return nganhNgheKinhDoanh;
    }

    public void setNganhNgheKinhDoanh(String nganhNgheKinhDoanh) {
        this.nganhNgheKinhDoanh = nganhNgheKinhDoanh;
    }

    public long getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(long tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getNamThuNhat() {
        return namThuNhat;
    }

    public void setNamThuNhat(String namThuNhat) {
        this.namThuNhat = namThuNhat;
    }

    public String getNamThuHai() {
        return namThuHai;
    }

    public void setNamThuHai(String namThuHai) {
        this.namThuHai = namThuHai;
    }

    public Long getDoanhThuNamNhat() {
        return doanhThuNamNhat;
    }

    public void setDoanhThuNamNhat(Long doanhThuNamNhat) {
        this.doanhThuNamNhat = doanhThuNamNhat;
    }

    public Long getDoanhThuNamHai() {
        return doanhThuNamHai;
    }

    public void setDoanhThuNamHai(Long doanhThuNamHai) {
        this.doanhThuNamHai = doanhThuNamHai;
    }

    public String getThongTinThem() {
        return thongTinThem;
    }

    public void setThongTinThem(String thongTinThem) {
        this.thongTinThem = thongTinThem;
    }

    public ToChuc() {
    }

    @Override
    public String toString() {
        return "ToChuc{" +
                "ten='" + ten + '\'' +
                ", tenNuocNgoai='" + tenNuocNgoai + '\'' +
                ", tenDonViChuQuan='" + tenDonViChuQuan + '\'' +
                ", diaChiTruSoGiaoDich='" + diaChiTruSoGiaoDich + '\'' +
                ", nganhNgheKinhDoanh='" + nganhNgheKinhDoanh + '\'' +
                ", tongDoanhThu=" + tongDoanhThu +
                ", namThuNhat='" + namThuNhat + '\'' +
                ", namThuHai='" + namThuHai + '\'' +
                ", doanhThuNamNhat=" + doanhThuNamNhat +
                ", doanhThuNamHai=" + doanhThuNamHai +
                ", thongTinThem='" + thongTinThem + '\'' +
                '}';
    }
}
