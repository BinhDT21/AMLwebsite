package com.pcrt.Pcrt.dto.request.pcrt_04;

public class HoiDongQuanTri {
    private String ten;
    private String chucVu;
    private String diaChi;

    public HoiDongQuanTri(String ten, String chucVu, String diaChi) {
        this.ten = ten;
        this.chucVu = chucVu;
        this.diaChi = diaChi;
    }

    public HoiDongQuanTri() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return "HoiDongQuanTri{" +
                "ten='" + ten + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
