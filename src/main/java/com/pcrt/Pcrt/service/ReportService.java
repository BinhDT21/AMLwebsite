package com.pcrt.Pcrt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.PCRT_03_detail;
import com.pcrt.Pcrt.entities.PCRT_04_detail;
import com.pcrt.Pcrt.entities.Report;
import com.pcrt.Pcrt.repository.ReportRepository;
import com.pcrt.Pcrt.repository.query.ReportRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportRepositoryQuery reportRepositoryQuery;
    @Autowired
    private Pcrt04_Service pcrt04Service;
    @Autowired
    private Pcrt03_Service pcrt03Service;
    @Autowired
    private Cloudinary cloudinary;

    public Page<Report> getReportList (Map<String, String> params){

        List<Report> reportList = reportRepositoryQuery.getReportList(params);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        long totalPages = reportRepositoryQuery.countReport(params);

        return new PageImpl<>(reportList, PageRequest.of(page, 10), totalPages);
    }

    public Report getReportById (int id){
        return reportRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy report"));
    }

    public File createPCRT_01 (Report report) throws IOException {
        Customer customer = report.getTransaction().getCustomer();

        PCRT_03_detail pcrt03Detail = pcrt03Service.getPCRT_03DetailByCustomerId(customer.getId());
        PCRT_04_detail pcrt04Detail = pcrt04Service.getPCRT_04DetailByCustomerId(customer.getId());


        // Tạo các biến đối với khách hàng cá nhân
        String ind_name = "",ind_dob = "",ind_occupation= "", ind_country= "", ind_p_address_d= "", ind_p_address_p= "", ind_p_c= "", ind_c_address_d= "",
                ind_c_address_p= "", ind_c_address_c= "", ind_ni_num= "", ind_ni_isd= "", ind_ni_isp= "", ind_pp_num= "", ind_pp_isd= "", ind_pp_isp= "", ind_phone_number = "";

        // Tạo các biến đối với khách hàng doanh nghiệp
        String bs_name = "", bs_f_name = "", bs_s_name = "", bs_address_d = "", bs_address_p= "", bs_address_c = "", bs_establishment_num= "", bs_establishment_isd= "",
                bs_establishment_isp= "", bs_br_num= "", bs_br_isd= "", bs_br_isp= "", bs_tax= "", bs_occupation= "", bs_phone= "", bs_fax= "", bs_customer_name= "",
                bs_cus_dob= "", bs_cus_occ= "", bs_cus_country= "", bs_cus_p_add_d= "", bs_cus_p_add_p= "", bs_cus_p_add_c= "", bs_cus_c_add_d= "", bs_cus_c_add_p= "", bus_c_add_c= "",
                bs_cus_ni_num= "", bs_cus_ni_isd= "", bs_cus_ni_isp= "", bs_cus_pp_num= "", bs_cus_pp_isd= "", bs_cus_pp_isp= "", bs_cus_office_phone= "", bs_cus_mb_phone= "";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if(pcrt03Detail!=null){
            ind_name = customer.getName();
            ind_dob = customer.getDob();
            ind_occupation = pcrt03Detail.getOccupation();
            ind_country = customer.getCountry();
            ind_p_address_d = pcrt03Detail.getPermanentAddress().getDistrict();
            ind_p_address_p = pcrt03Detail.getPermanentAddress().getProvince();
            ind_p_c = pcrt03Detail.getPermanentAddress().getCountry();
            ind_c_address_d = pcrt03Detail.getCurrentAddress().getDistrict();
            ind_c_address_p = pcrt03Detail.getCurrentAddress().getProvince();
            ind_c_address_c = pcrt03Detail.getCurrentAddress().getCountry();
            ind_ni_num = pcrt03Detail.getNationalIdentify().getNumber();
            ind_ni_isd = formatter.format(pcrt03Detail.getNationalIdentify().getIssuedDate());
            ind_ni_isp = pcrt03Detail.getNationalIdentify().getIssuedPlace();
            ind_pp_num = pcrt03Detail.getPassportIdentify().getNumber();
            ind_pp_isd = formatter.format(pcrt03Detail.getPassportIdentify().getIssuedDate());
            ind_pp_isp = pcrt03Detail.getPassportIdentify().getIssuedPlace();
            ind_phone_number = pcrt03Detail.getPhoneNumber();
        }
        if(pcrt04Detail!=null){
            bs_name = pcrt04Detail.getBusinessName();
            bs_f_name = pcrt04Detail.getForeignName();
            bs_s_name = pcrt04Detail.getShortName();
            bs_address_d = pcrt04Detail.getAddress().getDistrict();
            bs_address_p = pcrt04Detail.getAddress().getProvince();
            bs_address_c = pcrt04Detail.getAddress().getCountry();
            bs_establishment_num = pcrt04Detail.getEstablishmentLicense().getNumber();
            bs_establishment_isd = formatter.format(pcrt04Detail.getEstablishmentLicense().getIssuedDate());
            bs_establishment_isp = pcrt04Detail.getEstablishmentLicense().getIssuedPlace();
            bs_br_num = pcrt04Detail.getBusinessRegistrationLicense().getNumber();
            bs_br_isd = formatter.format(pcrt04Detail.getBusinessRegistrationLicense().getIssuedDate());
            bs_br_isp = pcrt04Detail.getBusinessRegistrationLicense().getIssuedPlace();
            bs_tax = pcrt04Detail.getTaxCode();
            bs_occupation = pcrt04Detail.getBusinessOccupation();
            bs_phone = pcrt04Detail.getPhoneNumber();
            bs_fax = pcrt04Detail.getFaxNumber();
            bs_customer_name = customer.getName();
            bs_cus_dob = customer.getDob();
            bs_cus_occ = pcrt04Detail.getCustomerOccupation();
            bs_cus_country = customer.getCountry();
            bs_cus_p_add_d = pcrt04Detail.getCustomerPermanentAddress().getDistrict();
            bs_cus_p_add_p = pcrt04Detail.getCustomerPermanentAddress().getProvince();
            bs_cus_p_add_c = pcrt04Detail.getCustomerPermanentAddress().getCountry();
            bs_cus_c_add_d = pcrt04Detail.getCustomerCurrentAddress().getDistrict();
            bs_cus_c_add_p = pcrt04Detail.getCustomerCurrentAddress().getProvince();
            bus_c_add_c = pcrt04Detail.getCustomerCurrentAddress().getCountry();
            bs_cus_ni_num = customer.getNationalId();
            bs_cus_pp_num = customer.getPassport();
            bs_cus_office_phone = pcrt04Detail.getCustomerOfficePhone();
            bs_cus_mb_phone = pcrt04Detail.getCustomerMobilePhone();
        }


        String transaction_createdDate = "", amount = "";
        transaction_createdDate = formatter.format(report.getTransaction().getCreatedDate());
        amount = report.getTransaction().getAmount().toString();


        String outputPath = "form/pcrt-01/" + customer.getName() + "_" + customer.getNationalId() + ".pdf";

        // Tạo tài liệu PDF
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Sử dụng font Unicode để hỗ trợ tiếng Việt
        PdfFont font = PdfFontFactory.createFont("static\\font\\arial.ttf", "Identity-H");

        // tiêu đề tổ chức
        ImageData imageData = ImageDataFactory.create("https://res.cloudinary.com/dwdvnztnn/image/upload/v1742350980/PCRT/aml_form_header_uj0l0n.png");
        Image image = new Image(imageData);
        document.add(image);

        Table pcrt_01 = new Table(3);
        pcrt_01.setWidth(UnitValue.createPercentValue(100));

        Cell headerCell = new Cell(1, 2);
        headerCell.add(new Paragraph("Báo cáo giao dịch đáng ngờ").setFont(font).setBold())
                .setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER);

        Cell cell_ngay_tao = new Cell(1, 2);
        cell_ngay_tao.add(new Paragraph("Ngày "+ LocalDateTime.now().getDayOfMonth() + " tháng " + LocalDateTime.now().getMonthValue()  + " năm " + LocalDateTime.now().getYear()).setFont(font))
                .setTextAlignment(TextAlignment.CENTER).setBorderTop(Border.NO_BORDER);


        Cell cell_so_bao_cap = new Cell(2, 1);
        cell_so_bao_cap.add(new Paragraph("Số báo cáo: " + report.getId()).setFont(font).setItalic().setBold().setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));


        pcrt_01.addCell(headerCell);
        pcrt_01.addCell(cell_so_bao_cap);
        pcrt_01.addCell(cell_ngay_tao);

        Cell cell_checkbox_khong = new Cell(1, 1);
        cell_checkbox_khong.add(new Paragraph("□ Không").setFont(font)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell cell_checkbox_co = new Cell(1, 2);
        cell_checkbox_co.add(new Paragraph("□ Có: \n- Số của báo cáo được sửa đổi:\n- Ngày của báo cáo được sửa đổi:\n- Nội dung sữa đổi:").setFont(font));

        pcrt_01.addCell(cell_checkbox_khong);
        pcrt_01.addCell(cell_checkbox_co);

        Cell cell_phan_mot = new Cell(1, 1);
        cell_phan_mot.add(new Paragraph("Phần I").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER);

        Cell cell_thong_tin_ve_doi_tuong = new Cell(1, 2);
        cell_thong_tin_ve_doi_tuong.add(new Paragraph("THÔNG TIN VỀ ĐỐI TƯỢNG BÁO CÁO").setBold().setFont(font));

        pcrt_01.addCell(cell_phan_mot);
        pcrt_01.addCell(cell_thong_tin_ve_doi_tuong);

        Cell thong_tin_ve_doi_tuong_bao_cao = new Cell(1, 3);
        thong_tin_ve_doi_tuong_bao_cao.add(new Paragraph("1. Thông tin về đối tượng báo cáo").setFont(font).setBold());

        pcrt_01.addCell(thong_tin_ve_doi_tuong_bao_cao);

        Cell ten_doi_tuong_bao_cao = new Cell(1, 3);
        ten_doi_tuong_bao_cao.add(new Paragraph("a. Tên đối tượng báo cáo: Tổ chức tài chính vi mô CEP").setFont(font));
        pcrt_01.addCell(ten_doi_tuong_bao_cao);

        Cell dia_chi = new Cell(1, 3);
        dia_chi.add(new Paragraph("b. Địa chỉ (số nhà, đường/phố): ").setFont(font));
        pcrt_01.addCell(dia_chi);

        Cell quan_huyen = new Cell();
        quan_huyen.add(new Paragraph("Quận/Huyện: " + report.getUser().getAddress().getDistrict()).setFont(font));
        pcrt_01.addCell(quan_huyen);

        Cell tinh_tp = new Cell();
        tinh_tp.add(new Paragraph("Tỉnh/ Thành phố: " + report.getUser().getAddress().getProvince()).setFont(font));
        pcrt_01.addCell(tinh_tp);

        Cell quoc_gia = new Cell();
        quoc_gia.add(new Paragraph("Quốc gia: " + report.getUser().getAddress().getCountry()).setFont(font));
        pcrt_01.addCell(quoc_gia);

        Cell dien_thoai = new Cell(1, 1);
        dien_thoai.add(new Paragraph("c. Điện thoại: " + report.getUser().getPhoneNumber()).setFont(font));
        pcrt_01.addCell(dien_thoai);

        Cell fax = new Cell(1, 2);
        fax.add(new Paragraph("d. Fax: ").setFont(font));
        pcrt_01.addCell(fax);

        Cell ten_diem_phat_sinh_gd = new Cell(1, 3);
        ten_diem_phat_sinh_gd.add(new Paragraph("e. Tên điểm phát sinh giao dịch: " + report.getTransaction().getBranch().getName()).setFont(font));
        pcrt_01.addCell(ten_diem_phat_sinh_gd);

        Cell dia_chi_diem_psgd = new Cell(1, 3);
        dia_chi_diem_psgd.add(new Paragraph("f. Địa chỉ điểm phát sinh giao dịch: " + report.getTransaction().getBranch().getAddress()).setFont(font));
        pcrt_01.addCell(dia_chi_diem_psgd);

        Cell quan_huyen2 = new Cell();
        quan_huyen2.add(new Paragraph("Quận/Huyện: " + report.getTransaction().getBranch().getDistrict()).setFont(font));
        pcrt_01.addCell(quan_huyen2);

        Cell tinh_tp2 = new Cell();
        tinh_tp2.add(new Paragraph("Tỉnh/ Thành phố: " + report.getTransaction().getBranch().getProvince()).setFont(font));
        pcrt_01.addCell(tinh_tp2);

        Cell quoc_gia2 = new Cell();
        quoc_gia2.add(new Paragraph("Quốc gia: " + report.getTransaction().getBranch().getCountry()).setFont(font));
        pcrt_01.addCell(quoc_gia2);

        Cell dien_thoai2 = new Cell(1, 1);
        dien_thoai2.add(new Paragraph("c. Điện thoại: " + report.getTransaction().getBranch().getPhoneNumber()).setFont(font));
        pcrt_01.addCell(dien_thoai2);

        Cell fax2 = new Cell(1, 2);
        fax2.add(new Paragraph("d. Fax: " + report.getTransaction().getBranch().getFaxNumber()).setFont(font));
        pcrt_01.addCell(fax2);

        Cell thong_tin_nguoi_lap_bao_cao = new Cell(1, 3);
        thong_tin_nguoi_lap_bao_cao.add(new Paragraph("2. Thông tin người lập báo cáo:").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_nguoi_lap_bao_cao);

        Cell ho_ten = new Cell(1, 3);
        ho_ten.add(new Paragraph("a. Họ và tên: " + report.getUser().getName()).setFont(font));
        pcrt_01.addCell(ho_ten);

        Cell dien_thoai_co_dinh = new Cell(1, 1);
        dien_thoai_co_dinh.add(new Paragraph("b. Điện thoại cố định: " ).setFont(font));
        pcrt_01.addCell(dien_thoai_co_dinh);

        Cell dien_thoai_di_dong = new Cell(1, 1);
        dien_thoai_di_dong.add(new Paragraph("c. Điện thoại di động: " + report.getUser().getPhoneNumber()).setFont(font));
        pcrt_01.addCell(dien_thoai_di_dong);

        Cell bo_phan_cong_tac = new Cell(1, 3);
        bo_phan_cong_tac.add(new Paragraph("d. Bộ phận công tác: chi nhánh "+ report.getUser().getBranch().getName() ).setFont(font));
        pcrt_01.addCell(bo_phan_cong_tac);

        Cell phan_hai = new Cell(1, 1);
        phan_hai.add(new Paragraph("Phần II").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        pcrt_01.addCell(phan_hai);

        Cell tua_de_phan_hai = new Cell(1, 2);
        tua_de_phan_hai.add(new Paragraph("THÔNG TIN VỀ CÁ NHÂN, TỔ CHỨC THỰC HIỆN GIAO DỊCH ĐÁNG NGỜ").setFont(font).setBold()).setVerticalAlignment(VerticalAlignment.MIDDLE);
        pcrt_01.addCell(tua_de_phan_hai);

        Cell thong_tin_ca_nhan = new Cell(1, 3);
        thong_tin_ca_nhan.add(new Paragraph("1. Thông tin về cá nhân thực hiện giao dịch").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_ca_nhan);

        //thông tin về cá nhân thực hiện giao dịch
        Cell ho_ten2 = new Cell(1, 3);
        ho_ten2.add(new Paragraph("a. Họ và tên: " + ind_name ).setFont(font));
        pcrt_01.addCell(ho_ten2);

        Cell ngay_sinh = new Cell(1, 3);
        ngay_sinh.add(new Paragraph("b. Ngày, tháng, năm sinh:" + ind_dob).setFont(font));
        pcrt_01.addCell(ngay_sinh);

        Cell nghe_nghiep = new Cell(1, 3);
        nghe_nghiep.add(new Paragraph("c. Nghề nghiệp: " + ind_occupation).setFont(font));
        pcrt_01.addCell(nghe_nghiep);

        Cell quoc_tich = new Cell(1, 3);
        quoc_tich.add(new Paragraph("d. Quốc tịch: " + ind_country).setFont(font));
        pcrt_01.addCell(quoc_tich);

        Cell noi_dang_ky_thuong_tru = new Cell(1, 3);
        noi_dang_ky_thuong_tru.add(new Paragraph("e. Nơi đăng ký thường trú: ").setFont(font));
        pcrt_01.addCell(noi_dang_ky_thuong_tru);

        Cell quan_huyen3 = new Cell();
        quan_huyen3.add(new Paragraph("Quận/Huyện: " + ind_p_address_d).setFont(font));
        pcrt_01.addCell(quan_huyen3);

        Cell tinh_tp3 = new Cell();
        tinh_tp3.add(new Paragraph("Tỉnh/ Thành phố: " + ind_p_address_p).setFont(font));
        pcrt_01.addCell(tinh_tp3);

        Cell quoc_gia3 = new Cell();
        quoc_gia3.add(new Paragraph("Quốc gia: " + ind_p_c).setFont(font));
        pcrt_01.addCell(quoc_gia3);


        Cell noi_o_hien_tai = new Cell(1, 3);
        noi_o_hien_tai.add(new Paragraph("f. Nơi ở hiện tại:").setFont(font));
        pcrt_01.addCell(noi_o_hien_tai);

        Cell quan_huyen4 = new Cell();
        quan_huyen4.add(new Paragraph("Quận/Huyện: " + ind_c_address_d).setFont(font));
        pcrt_01.addCell(quan_huyen4);

        Cell tinh_tp4 = new Cell();
        tinh_tp4.add(new Paragraph("Tỉnh/ Thành phố: " + ind_c_address_p).setFont(font));
        pcrt_01.addCell(tinh_tp4);

        Cell quoc_gia4 = new Cell();
        quoc_gia4.add(new Paragraph("Quốc gia: "+ ind_c_address_c).setFont(font));
        pcrt_01.addCell(quoc_gia4);

        Cell so_cmt = new Cell(1, 1);
        so_cmt.add(new Paragraph("g. Số CMT: " + ind_ni_num).setFont(font));
        pcrt_01.addCell(so_cmt);

        Cell ngay_cap_cmt = new Cell(1, 1);
        ngay_cap_cmt.add(new Paragraph("Ngày cấp: "+ ind_ni_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_cmt);

        Cell noi_cap_cmt = new Cell(1, 1);
        noi_cap_cmt.add(new Paragraph("Nơi cấp: " + ind_ni_isp).setFont(font));
        pcrt_01.addCell(noi_cap_cmt);


        Cell ho_chieu = new Cell(1, 1);
        ho_chieu.add(new Paragraph("h. Số hộ chiếu (còn hiệu lực): " + ind_pp_num).setFont(font));
        pcrt_01.addCell(ho_chieu);

        Cell ngay_cap_hc = new Cell(1, 1);
        ngay_cap_hc.add(new Paragraph("Ngày cấp: " + ind_pp_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_hc);

        Cell noi_cap_hc = new Cell(1, 1);
        noi_cap_hc.add(new Paragraph("Nơi cấp: " + ind_pp_isp).setFont(font));
        pcrt_01.addCell(noi_cap_hc);


        Cell dien_thoai_co_dinh2 = new Cell(1, 1);
        dien_thoai_co_dinh2.add(new Paragraph("i. Điện thoại cố định: ").setFont(font));
        pcrt_01.addCell(dien_thoai_co_dinh2);

        Cell dien_thoai_di_dong2 = new Cell(1, 2);
        dien_thoai_di_dong2.add(new Paragraph("k. Điện thoại di động: " + ind_phone_number).setFont(font));
        pcrt_01.addCell(dien_thoai_di_dong2);

        Cell so_tai_khoan = new Cell(1, 3);
        so_tai_khoan.add(new Paragraph("l. Số tài khoản:").setFont(font));
        pcrt_01.addCell(so_tai_khoan);

        Cell loai_tai_khoan = new Cell(1, 3);
        loai_tai_khoan.add(new Paragraph("m. Loại tài khoản:").setFont(font));
        pcrt_01.addCell(loai_tai_khoan);

        Cell ngay_mo_tai_khoan = new Cell(1, 3);
        ngay_mo_tai_khoan.add(new Paragraph("n. Ngày mở tài khoản:").setFont(font));
        pcrt_01.addCell(ngay_mo_tai_khoan);

        Cell tinh_trang_tai_khoan = new Cell(1, 3);
        tinh_trang_tai_khoan.add(new Paragraph("o. Tình trạng tài khoản:").setFont(font));
        pcrt_01.addCell(tinh_trang_tai_khoan);

        Cell checkbox_hoat_dong_bth = new Cell(1, 1);
        checkbox_hoat_dong_bth.add(new Paragraph("□ Hoạt động bình thường").setFont(font));
        pcrt_01.addCell(checkbox_hoat_dong_bth);

        Cell checkbox_bat_thuong = new Cell(1, 2);
        checkbox_bat_thuong.add(new Paragraph("□ Bất thường").setFont(font));
        pcrt_01.addCell(checkbox_bat_thuong);

        Cell thong_tin_to_chuc = new Cell(1, 3);
        thong_tin_to_chuc.add(new Paragraph("2. Thông tin về tổ chức thực hiện giao dịch").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_to_chuc);

        Cell to_chuc = new Cell(1, 3);
        to_chuc.add(new Paragraph("2.1 Thông tin về tổ chức").setFont(font).setBold().setItalic());
        pcrt_01.addCell(to_chuc);

        Cell ten_to_chuc = new Cell(1, 3);
        ten_to_chuc.add(new Paragraph("a. Tên đầy đủ của tổ chức: " + bs_name).setFont(font));
        pcrt_01.addCell(ten_to_chuc);

        Cell ten_nuoc_ngoai = new Cell(1, 3);
        ten_nuoc_ngoai.add(new Paragraph("b. Tên tiếng nước ngoài (nếu có): " + bs_f_name).setFont(font));
        pcrt_01.addCell(ten_nuoc_ngoai);

        Cell ten_viet_tat = new Cell(1, 3);
        ten_viet_tat.add(new Paragraph("c. Tên viết tắt: " +  bs_s_name).setFont(font));
        pcrt_01.addCell(ten_viet_tat);

        Cell dia_chi2 = new Cell(1, 3);
        dia_chi2.add(new Paragraph("d. Địa chỉ: ").setFont(font));
        pcrt_01.addCell(dia_chi2);

        Cell district = new Cell(1, 1);
        Cell province = new Cell(1, 1);
        Cell country = new Cell(1, 1);
        district.add(new Paragraph("Quận/Huyện: " + bs_address_d).setFont(font));
        pcrt_01.addCell(district);
        province.add(new Paragraph("Tỉnh/Thành phố: " + bs_address_p).setFont(font));
        pcrt_01.addCell(province);
        country.add(new Paragraph("Quốc gia: " + bs_address_c).setFont(font));
        pcrt_01.addCell(country);

        Cell giay_phep_thanh_lap = new Cell(1, 1);
        giay_phep_thanh_lap.add(new Paragraph("e. Giấy phép thành lập số: " + bs_establishment_num).setFont(font));
        pcrt_01.addCell(giay_phep_thanh_lap);

        Cell ngay_cap_gptl = new Cell(1, 1);
        ngay_cap_gptl.add(new Paragraph("Ngày cấp: " + bs_establishment_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_gptl);

        Cell noi_cap_gptl = new Cell(1, 1);
        noi_cap_gptl.add(new Paragraph("Nơi cấp: " + bs_establishment_isp).setFont(font));
        pcrt_01.addCell(noi_cap_gptl);

        Cell dang_ky_kd = new Cell(1, 1);
        dang_ky_kd.add(new Paragraph("f. Đăng ký kinh doanh số: "+ bs_br_num).setFont(font));
        pcrt_01.addCell(dang_ky_kd);

        Cell ngay_cap_dkkd = new Cell(1, 1);
        ngay_cap_dkkd.add(new Paragraph("Ngày cấp: " + bs_br_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_dkkd);

        Cell noi_cap_dkkd = new Cell(1, 1);
        noi_cap_dkkd.add(new Paragraph("Nơi cấp: " + bs_br_isp).setFont(font));
        pcrt_01.addCell(noi_cap_dkkd);


        Cell ma_so_thue = new Cell(1, 3);
        ma_so_thue.add(new Paragraph("g. Mã số thuế: " + bs_tax).setFont(font));
        pcrt_01.addCell(ma_so_thue);

        Cell nganh_nghe_kd = new Cell(1, 3);
        nganh_nghe_kd.add(new Paragraph("h. Ngành nghề kinh doanh: " + bs_occupation).setFont(font));
        pcrt_01.addCell(nganh_nghe_kd);

        Cell dien_thoai3 = new Cell(1, 1);
        dien_thoai3.add(new Paragraph("i. Điện thoại: " + bs_phone).setFont(font));
        pcrt_01.addCell(dien_thoai3);

        Cell fax3 = new Cell(1, 2);
        fax3.add(new Paragraph("k. Fax: " + bs_fax).setFont(font));
        pcrt_01.addCell(fax3);

        Cell so_tai_khoan2 = new Cell(1, 3);
        so_tai_khoan2.add(new Paragraph("l. Số tài khoản:").setFont(font));
        pcrt_01.addCell(so_tai_khoan2);

        Cell loai_tai_khoan2 = new Cell(1, 3);
        loai_tai_khoan2.add(new Paragraph("m. Loại tài khoản:").setFont(font));
        pcrt_01.addCell(loai_tai_khoan2);

        Cell tinh_trang_tk = new Cell(1, 3);
        tinh_trang_tk.add(new Paragraph("o. Tình trạng tài khoản:").setFont(font));
        pcrt_01.addCell(tinh_trang_tk);

        Cell checkbox_hoat_dong_bth2 = new Cell(1, 1);
        checkbox_hoat_dong_bth2.add(new Paragraph("□ Hoạt động bình thường:").setFont(font));
        pcrt_01.addCell(checkbox_hoat_dong_bth2);

        Cell checkbox_bat_thuong2 = new Cell(1, 2);
        checkbox_bat_thuong2.add(new Paragraph("□ Bất thường (nêu rõ lí do):").setFont(font));
        pcrt_01.addCell(checkbox_bat_thuong2);


        Cell thong_tin_nguoi_dai_dien = new Cell(1, 3);
        thong_tin_nguoi_dai_dien.add(new Paragraph("2.2. Thông tin về người đại diện cho tổ chức:").setFont(font).setBold().setItalic());
        pcrt_01.addCell(thong_tin_nguoi_dai_dien);


        Cell ho_ten3 = new Cell(1, 3);
        ho_ten3.add(new Paragraph("a. Họ và tên: " + bs_customer_name).setFont(font));
        pcrt_01.addCell(ho_ten3);

        Cell dob = new Cell(1, 3);
        dob.add(new Paragraph("b. Ngày, tháng, năm sinh: " + bs_cus_dob).setFont(font));
        pcrt_01.addCell(dob);

        Cell nghe_nghiep2 = new Cell(1, 3);
        nghe_nghiep2.add(new Paragraph("c. Nghề nghiệp: " + bs_cus_occ).setFont(font));
        pcrt_01.addCell(nghe_nghiep2);

        Cell quoc_tich2 = new Cell(1, 3);
        quoc_tich2.add(new Paragraph("d. Quốc tịch: " + bs_cus_country).setFont(font));
        pcrt_01.addCell(quoc_tich2);

        Cell noi_dk_thuong_tru = new Cell(1, 3);
        noi_dk_thuong_tru.add(new Paragraph("e. Nơi đăng ký thường trú: " ).setFont(font));
        pcrt_01.addCell(noi_dk_thuong_tru);

        Cell district2 = new Cell(1, 1);
        Cell province2 = new Cell(1, 1);
        Cell country2 = new Cell(1, 1);
        district2.add(new Paragraph("Quận/Huyện: "+ bs_cus_p_add_d).setFont(font));
        pcrt_01.addCell(district2);
        province2.add(new Paragraph("Tỉnh/Thành phố: " + bs_cus_p_add_p).setFont(font));
        pcrt_01.addCell(province2);
        country2.add(new Paragraph("Quốc gia: " + bs_cus_p_add_c).setFont(font));
        pcrt_01.addCell(country2);

        Cell noi_o_hien_tai2 = new Cell(1, 3);
        noi_o_hien_tai2.add(new Paragraph("f. Nơi ở hiện tại:").setFont(font));
        pcrt_01.addCell(noi_o_hien_tai2);

        Cell district3 = new Cell(1, 1);
        Cell province3 = new Cell(1, 1);
        Cell country3 = new Cell(1, 1);
        district3.add(new Paragraph("Quận/Huyện: " + bs_cus_c_add_d).setFont(font));
        pcrt_01.addCell(district3);
        province3.add(new Paragraph("Tỉnh/Thành phố: "+ bs_cus_c_add_p).setFont(font));
        pcrt_01.addCell(province3);
        country3.add(new Paragraph("Quốc gia: " + bus_c_add_c).setFont(font));
        pcrt_01.addCell(country3);


        Cell so_cmt2 = new Cell(1, 1);
        so_cmt2.add(new Paragraph("g. Số CMT: " + bs_cus_ni_num).setFont(font));
        pcrt_01.addCell(so_cmt2);

        Cell ngay_cap_cmt2 = new Cell(1, 1);
        ngay_cap_cmt2.add(new Paragraph("Ngày cấp: " + bs_cus_ni_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_cmt2);

        Cell noi_cap_cmt2 = new Cell(1, 1);
        noi_cap_cmt2.add(new Paragraph("Nơi cấp: "+bs_cus_ni_isp).setFont(font));
        pcrt_01.addCell(noi_cap_cmt2);


        Cell ho_chieu2 = new Cell(1, 1);
        ho_chieu2.add(new Paragraph("h. Số hộ chiếu (còn hiệu lực): " + bs_cus_pp_num).setFont(font));
        pcrt_01.addCell(ho_chieu2);

        Cell ngay_cap_hc2 = new Cell(1, 1);
        ngay_cap_hc2.add(new Paragraph("Ngày cấp: " + bs_cus_pp_isd).setFont(font));
        pcrt_01.addCell(ngay_cap_hc2);

        Cell noi_cap_hc2 = new Cell(1, 1);
        noi_cap_hc2.add(new Paragraph("Nơi cấp: "+ bs_cus_pp_isp).setFont(font));
        pcrt_01.addCell(noi_cap_hc2);


        Cell dien_thoai_co_dinh3 = new Cell(1, 1);
        dien_thoai_co_dinh3.add(new Paragraph("i. Điện thoại cố định: " + bs_cus_office_phone).setFont(font));
        pcrt_01.addCell(dien_thoai_co_dinh3);

        Cell dien_thoai_di_dong4 = new Cell(1, 2);
        dien_thoai_di_dong4.add(new Paragraph("k. Điện thoại di động: " + bs_cus_mb_phone).setFont(font));
        pcrt_01.addCell(dien_thoai_di_dong4);


        Cell thong_tin_ve_giao_dich = new Cell(1, 3);
        thong_tin_ve_giao_dich.add(new Paragraph("3. Thông tin về giao dịch").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_ve_giao_dich);

        Cell thoi_gian_gd = new Cell(1, 3);
        thoi_gian_gd.add(new Paragraph("a. Thời gian tiến hành giao dịch: vào hồi ..., " + transaction_createdDate).setFont(font));
        pcrt_01.addCell(thoi_gian_gd);

        Cell so_tien = new Cell(1, 3);
        so_tien.add(new Paragraph("b. Số tiền giao dịch: ").setFont(font));
        pcrt_01.addCell(so_tien);

        Cell bang_so = new Cell(1, 1);
        bang_so.add(new Paragraph("Bằng số: "  + amount + "VND").setFont(font));
        pcrt_01.addCell(bang_so);

        Cell bang_chu = new Cell(1, 2);
        bang_chu.add(new Paragraph("Bằng chữ:").setFont(font));
        pcrt_01.addCell(bang_chu);

        Cell muc_dich = new Cell(1, 3);
        muc_dich.add(new Paragraph("c. Mục đích giao dịch").setFont(font));
        pcrt_01.addCell(muc_dich);

        Cell thong_tin_bo_sung = new Cell(1, 3);
        thong_tin_bo_sung.add(new Paragraph("4. Thông tin bổ sung").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_bo_sung);

        Cell phan_ba = new Cell(1, 1);
        phan_ba.add(new Paragraph("Phần III").setBold().setFont(font)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        pcrt_01.addCell(phan_ba);

        Cell tua_de_phan_ba = new Cell(1, 2);
        tua_de_phan_ba.add(new Paragraph("THÔNG TIN VỀ CÁ NHÂN, TỔ CHỨC CÓ LIÊN QUAN TỚI GIAO DỊCH ĐÁNG NGỜ").setFont(font).setBold());
        pcrt_01.addCell(tua_de_phan_ba);

        Cell ho_ten4 = new Cell(1, 3);
        ho_ten4.add(new Paragraph("a. Họ và tên:").setFont(font));
        pcrt_01.addCell(ho_ten4);

        Cell ngay_sinh2 = new Cell(1, 3);
        ngay_sinh2.add(new Paragraph("b. Ngày, tháng, năm sinh: ").setFont(font));
        pcrt_01.addCell(ngay_sinh2);

        Cell nghe_nghiep3 = new Cell(1, 3);
        nghe_nghiep3.add(new Paragraph("c. Nghề nghiệp:").setFont(font));
        pcrt_01.addCell(nghe_nghiep3);

        Cell quoc_tich3 = new Cell(1, 3);
        quoc_tich3.add(new Paragraph("d. Quốc tịch: ").setFont(font));
        pcrt_01.addCell(quoc_tich3);

        Cell noi_dk_thuong_tru2 = new Cell(1, 3);
        noi_dk_thuong_tru2.add(new Paragraph("e. Nơi đăng ký thường tru:").setFont(font));
        pcrt_01.addCell(noi_dk_thuong_tru2);

        Cell district4 = new Cell(1, 1);
        Cell province4 = new Cell(1, 1);
        Cell country4 = new Cell(1, 1);
        district4.add(new Paragraph("Quận/Huyện:").setFont(font));
        pcrt_01.addCell(district4);
        province4.add(new Paragraph("Tỉnh/Thành phố:").setFont(font));
        pcrt_01.addCell(province4);
        country4.add(new Paragraph("Quốc gia:").setFont(font));
        pcrt_01.addCell(country4);

        Cell noi_o_hien_tai3 = new Cell(1, 3);
        noi_o_hien_tai3.add(new Paragraph("f. Nơi ở hiện tại:").setFont(font));
        pcrt_01.addCell(noi_o_hien_tai3);

        Cell district5 = new Cell(1, 1);
        Cell province5 = new Cell(1, 1);
        Cell country5 = new Cell(1, 1);
        district5.add(new Paragraph("Quận/Huyện:").setFont(font));
        pcrt_01.addCell(district5);
        province5.add(new Paragraph("Tỉnh/Thành phố:").setFont(font));
        pcrt_01.addCell(province5);
        country5.add(new Paragraph("Quốc gia:").setFont(font));
        pcrt_01.addCell(country5);


        Cell so_cmt3 = new Cell(1, 1);
        so_cmt3.add(new Paragraph("g. Số CMT:").setFont(font));
        pcrt_01.addCell(so_cmt3);

        Cell ngay_cap_cmt3 = new Cell(1, 1);
        ngay_cap_cmt3.add(new Paragraph("Ngày cấp:").setFont(font));
        pcrt_01.addCell(ngay_cap_cmt3);

        Cell noi_cap_cmt3 = new Cell(1, 1);
        noi_cap_cmt3.add(new Paragraph("Nơi cấp: ").setFont(font));
        pcrt_01.addCell(noi_cap_cmt3);


        Cell ho_chieu3 = new Cell(1, 1);
        ho_chieu3.add(new Paragraph("h. Số hộ chiếu (còn hiệu lực):").setFont(font));
        pcrt_01.addCell(ho_chieu3);

        Cell ngay_cap_hc3 = new Cell(1, 1);
        ngay_cap_hc3.add(new Paragraph("Ngày cấp:").setFont(font));
        pcrt_01.addCell(ngay_cap_hc3);

        Cell noi_cap_hc3 = new Cell(1, 1);
        noi_cap_hc3.add(new Paragraph("Nơi cấp: ").setFont(font));
        pcrt_01.addCell(noi_cap_hc3);


        Cell dien_thoai_co_dinh4 = new Cell(1, 1);
        dien_thoai_co_dinh4.add(new Paragraph("i. Điện thoại cố định:").setFont(font));
        pcrt_01.addCell(dien_thoai_co_dinh4);

        Cell dien_thoai_di_dong5 = new Cell(1, 2);
        dien_thoai_di_dong5.add(new Paragraph("k. Điện thoại di động:").setFont(font));
        pcrt_01.addCell(dien_thoai_di_dong5);

        Cell so_tai_khoan3 = new Cell(1, 3);
        so_tai_khoan3.add(new Paragraph("l. Số tài khoản:").setFont(font));
        pcrt_01.addCell(so_tai_khoan3);

        Cell mo_tai_ngan_hang = new Cell(1, 3);
        mo_tai_ngan_hang.add(new Paragraph("m. Mở tại ngân hàng:").setFont(font));
        pcrt_01.addCell(mo_tai_ngan_hang);

        Cell dia_chi_ngan_hang = new Cell(1, 3);
        dia_chi_ngan_hang.add(new Paragraph("n. Địa chỉ ngân hàng:").setFont(font));
        pcrt_01.addCell(dia_chi_ngan_hang);

        Cell district6 = new Cell(1, 1);
        Cell province6 = new Cell(1, 1);
        Cell country6 = new Cell(1, 1);
        district6.add(new Paragraph("Quận/Huyện:").setFont(font));
        pcrt_01.addCell(district6);
        province6.add(new Paragraph("Tỉnh/Thành phố:").setFont(font));
        pcrt_01.addCell(province6);
        country6.add(new Paragraph("Quốc gia:").setFont(font));
        pcrt_01.addCell(country6);

        Cell thong_tin_to_chuc_lien_quan = new Cell(1, 3);
        thong_tin_to_chuc_lien_quan.add(new Paragraph("2. Thông tin về tổ chức có liên quan tới giao dịch").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_to_chuc_lien_quan);


        Cell ten_to_chuc2 = new Cell(1, 3);
        ten_to_chuc2.add(new Paragraph("a. Tên đầy đủ của tổ chức:").setFont(font));
        pcrt_01.addCell(ten_to_chuc2);

        Cell ten_nuoc_ngoai2 = new Cell(1, 3);
        ten_nuoc_ngoai2.add(new Paragraph("b. Tên tiếng nước ngoài (nếu có):").setFont(font));
        pcrt_01.addCell(ten_nuoc_ngoai2);

        Cell ten_viet_tat2 = new Cell(1, 3);
        ten_viet_tat2.add(new Paragraph("c. Tên viết tắt: ").setFont(font));
        pcrt_01.addCell(ten_viet_tat2);

        Cell dia_chi3 = new Cell(1, 3);
        dia_chi3.add(new Paragraph("d. Địa chỉ:").setFont(font));
        pcrt_01.addCell(dia_chi3);

        Cell district7 = new Cell(1, 1);
        Cell province7 = new Cell(1, 1);
        Cell country7 = new Cell(1, 1);
        district7.add(new Paragraph("Quận/Huyện:").setFont(font));
        pcrt_01.addCell(district7);
        province7.add(new Paragraph("Tỉnh/Thành phố:").setFont(font));
        pcrt_01.addCell(province7);
        country7.add(new Paragraph("Quốc gia:").setFont(font));
        pcrt_01.addCell(country7);

        Cell giay_phep_thanh_lap2 = new Cell(1, 1);
        giay_phep_thanh_lap2.add(new Paragraph("e. Giấy phép thành lập số:").setFont(font));
        pcrt_01.addCell(giay_phep_thanh_lap2);

        Cell ngay_cap_gptl2 = new Cell(1, 1);
        ngay_cap_gptl2.add(new Paragraph("Ngày cấp:").setFont(font));
        pcrt_01.addCell(ngay_cap_gptl2);

        Cell noi_cap_gptl2 = new Cell(1, 1);
        noi_cap_gptl2.add(new Paragraph("Nơi cấp:").setFont(font));
        pcrt_01.addCell(noi_cap_gptl2);

        Cell nganh_nghe = new Cell(1, 3);
        nganh_nghe.add(new Paragraph("f. Ngành nghề kinh doanh:").setFont(font));
        pcrt_01.addCell(nganh_nghe);

        Cell dang_ky_kd2 = new Cell(1, 1);
        dang_ky_kd2.add(new Paragraph("g. Đăng ký kinh doanh số:").setFont(font));
        pcrt_01.addCell(dang_ky_kd2);

        Cell ngay_cap_dkkd2 = new Cell(1, 1);
        ngay_cap_dkkd2.add(new Paragraph("Ngày cấp:").setFont(font));
        pcrt_01.addCell(ngay_cap_dkkd2);

        Cell noi_cap_dkkd2 = new Cell(1, 1);
        noi_cap_dkkd2.add(new Paragraph("Nơi cấp: ").setFont(font));
        pcrt_01.addCell(noi_cap_dkkd2);

        Cell dien_thoai_co_dinh5 = new Cell(1, 1);
        dien_thoai_co_dinh5.add(new Paragraph("h. Điện thoại cố định:").setFont(font));
        pcrt_01.addCell(dien_thoai_co_dinh5);

        Cell fax4 = new Cell(1, 2);
        fax4.add(new Paragraph("i. Fax:").setFont(font));
        pcrt_01.addCell(fax4);

        Cell so_tai_khoan4 = new Cell(1, 3);
        so_tai_khoan4.add(new Paragraph("k. Số tài khoản:").setFont(font));
        pcrt_01.addCell(so_tai_khoan4);

        Cell mo_tai_ngan_hang2 = new Cell(1, 3);
        mo_tai_ngan_hang2.add(new Paragraph("l. Mở tại ngân hàng:").setFont(font));
        pcrt_01.addCell(mo_tai_ngan_hang2);

        Cell dia_chi_nh = new Cell(1, 3);
        dia_chi_nh.add(new Paragraph("m. Địa chỉ ngân hàng:").setFont(font));
        pcrt_01.addCell(dia_chi_nh);

        Cell district8 = new Cell(1, 1);
        Cell province8 = new Cell(1, 1);
        Cell country8 = new Cell(1, 1);
        district8.add(new Paragraph("Quận/Huyện:").setFont(font));
        pcrt_01.addCell(district8);
        province8.add(new Paragraph("Tỉnh/Thành phố:").setFont(font));
        pcrt_01.addCell(province8);
        country8.add(new Paragraph("Quốc gia:").setFont(font));
        pcrt_01.addCell(country8);

        Cell thong_tin_bo_sung2 = new Cell(1, 3);
        thong_tin_bo_sung2.add(new Paragraph("3. Thông tin bổ sung:").setFont(font).setBold());
        pcrt_01.addCell(thong_tin_bo_sung2);

        Cell phan_bon = new Cell(1, 1);
        phan_bon.add(new Paragraph("PHẦN IV").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        pcrt_01.addCell(phan_bon);

        Cell tua_de_phan_bon = new Cell(1, 2);
        tua_de_phan_bon.add(new Paragraph("LÝ DO NGHI NGỜ GIAO DỊCH VÀ NHỮNG CÔNG VIỆC ĐÃ THỰC HIỆN").setFont(font).setBold());
        pcrt_01.addCell(tua_de_phan_bon);

        Cell mo_ta_giao_dich = new Cell(1, 3);
        mo_ta_giao_dich.add(new Paragraph("1. Mô tả giao dịch và lý do nghi ngờ:").setFont(font).setBold());
        pcrt_01.addCell(mo_ta_giao_dich);

        Cell nhung_cong_viec_da_xu_ly = new Cell(1, 3);
        nhung_cong_viec_da_xu_ly.add(new Paragraph("2. Những công việc đã xử lý liên quan đến giao dịch đáng ngờ:").setFont(font).setBold());
        pcrt_01.addCell(nhung_cong_viec_da_xu_ly);

        Cell phan_nam = new Cell(1, 1);
        phan_nam.add(new Paragraph("PHẦN V").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        pcrt_01.addCell(phan_nam);

        Cell tai_lieu_dinh_kem = new Cell(1, 2);
        tai_lieu_dinh_kem.add(new Paragraph("TÀI LIỆU ĐÍNH KÈM").setFont(font).setBold());
        pcrt_01.addCell(tai_lieu_dinh_kem);

        Cell tai_lieu_lien_quan = new Cell(1, 3);
        tai_lieu_lien_quan.add(new Paragraph("* Tài liệu, chứng từ có liên quan đến giao dịch đáng ngờ (bản sao điện chuyển tiền, giấy rút tiền, nộp tiền, phiếu chuyển khoản...)").setFont(font));
        pcrt_01.addCell(tai_lieu_lien_quan);

        Cell nguoi_lap_phieu = new Cell(1, 1);
        nguoi_lap_phieu.add(new Paragraph("NGƯỜI LẬP PHIẾU").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(nguoi_lap_phieu);

        Cell truong_phong = new Cell(1, 1);
        truong_phong.add(new Paragraph("TRƯỞNG PHÒNG\n(bộ phận)").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(truong_phong);

        Cell tong_giam_doc = new Cell(1, 1);
        tong_giam_doc.add(new Paragraph("TỔNG GIÁM ĐỐC\n(giám đốc)").setFont(font).setBold()).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(tong_giam_doc);


        Cell mo_ta1 = new Cell(1, 1);
        mo_ta1.add(new Paragraph("(Cán bộ lập báo cáo, kí và ghi rõ họ tên)").setFont(font)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(mo_ta1);

        Cell mo_ta2 = new Cell(1,1);
        mo_ta2.add(new Paragraph("(Là người chịu trách nhiệm về\n PCRT tại đơn vị, kí và ghi rõ họ tên)").setFont(font)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(mo_ta2);

        Cell mo_ta3 = new Cell(1,1);
        mo_ta3.add(new Paragraph("(Kí, ghi rõ họ tên, đóng dấu)").setFont(font)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);
        pcrt_01.addCell(mo_ta3);


        document.add(pcrt_01);
        document.close();


        return new File(outputPath);
    }

    public void uploadFile (Report report, File file) throws IOException {
        Map res = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "PCRT/PCRT-01"
        ));
        report.setFilePath(res.get("secure_url").toString());
        report.setStatus("reported");
        reportRepository.save(report);
        System.out.println("Lưu report thành công");
    }


}
