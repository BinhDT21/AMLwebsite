package com.pcrt.Pcrt.dto.request.pcrt_04;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class KeToanTruong {
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String ten;
    @NotBlank(message = "Yêu cầu nhập thông tin")
    private String diaChi;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public KeToanTruong(String ten, String diaChi) {
        this.ten = ten;
        this.diaChi = diaChi;
    }

    public KeToanTruong (){

    }
    @Override
    public String toString() {
        return "KeToanTruong{" +
                "ten='" + ten + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
