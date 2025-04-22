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
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.pcrt.Pcrt.dto.request.pcrt_04.BanDieuHanh;
import com.pcrt.Pcrt.dto.request.pcrt_04.BusinessCustomerUpdateRequest;
import com.pcrt.Pcrt.dto.request.pcrt_04.HoiDongQuanTri;
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.repository.AddressRepository;
import com.pcrt.Pcrt.repository.LicenseRepository;
import com.pcrt.Pcrt.repository.Pcrt04DetailRepository;
import com.pcrt.Pcrt.repository.Pcrt04Repository;
import com.pcrt.Pcrt.repository.query.Pcrt04RepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class Pcrt04_Service {

    @Autowired
    private UserService userService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private Pcrt04Repository pcrt04Repository;
    @Autowired
    private Pcrt04RepositoryQuery pcrt04RepositoryCriteria;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private Pcrt04DetailRepository pcrt04DetailRepository;

    public File createFile(BusinessCustomerUpdateRequest request, Customer customer, PCRT_04 pcrt04) throws IOException {
        String outputPath = "form/pcrt-04/" + customer.getName() + "_" + customer.getNationalId() + ".pdf";

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

        // Tiêu đề chính
        Paragraph title = new Paragraph("GIẤY BỔ SUNG THÔNG TIN KHÁCH HÀNG")
                .setFont(font)
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Phần dành cho khách hàng cá nhân
        Paragraph subtitle = new Paragraph("(Dành cho khách hàng tổ chức)")
                .setFont(font)
                .setItalic()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(subtitle);

        // Nội dung các trường thông tin
        String[] fields = {
                "1. Tên tổ chức: " + request.getPcrt04Detail().getBusinessName(),
                "Tên nước ngoài: " + request.getPcrt04Detail().getForeignName(),
                "Tên đơn vị chủ quản: " + request.getPcrt04Detail().getParentOrganizationName(),
                "Địa chỉ trụ sở giao dịch: " + request.getPcrt04Detail().getAddress().getDistrict() + ", " + request.getPcrt04Detail().getAddress().getProvince() + ", " + request.getPcrt04Detail().getAddress().getCountry(),
                "2. Ngành, nghề sản xuất kinh doanh, dịch vụ tạo doanh thu chính: " + request.getPcrt04Detail().getBusinessOccupation(),
                "3. Tổng doanh thu trong hai năm gần nhất trước thời điểm đánh giá: " + request.getTong_doanh_thu() + "VND",
                "   Trong đó: ",
                "    - Doanh thu năm " + request.getNam_thu_nhat() + ": " + request.getDoanh_thu_nam_nhat() + "VND",
                "    - Doanh thu năm " + request.getNam_thu_hai() + ": " + request.getDoanh_thu_nam_hai() + "VND",
                "4. Thông tin liên quan đến nguồn tiền hoặc nguồn tài sản trong giao dịch của khách hàng:",
                request.getThong_tin_lien_quan_nguon_tien(),
                "    ...............................................................................................",
                "    ...............................................................................................",
                "5. Danh sách thành viên Hội đồng quản trị hoặc Hội đồng thành viên, Ban điều hành, Người đại diện pháp luật, Kế toán trưởng hoặc tương đương ",
                "5.1 Hội đồng quản trị hoặc Hội dồng thành viên"
        };
        for (String field : fields) {
            document.add(new Paragraph(field).setFont(font).setFontSize(12));
        }

        Table bangHoiDongQuanTri = new Table(3);
        bangHoiDongQuanTri.setWidth(UnitValue.createPercentValue(100));

        Cell cellName = new Cell().add(new Paragraph("Họ tên").setTextAlignment(TextAlignment.CENTER).setFont(font));
        Cell cellRole = new Cell().add(new Paragraph("Chức vụ").setTextAlignment(TextAlignment.CENTER).setFont(font));
        Cell cellAddress = new Cell().add(new Paragraph("Địa chỉ thường trú").setTextAlignment(TextAlignment.CENTER).setFont(font));

        bangHoiDongQuanTri.addCell(cellName);
        bangHoiDongQuanTri.addCell(cellRole);
        bangHoiDongQuanTri.addCell(cellAddress);

        if (request.getDanh_sach_hdqt() != null && !request.getDanh_sach_hdqt().isEmpty()) {
            for (HoiDongQuanTri i : request.getDanh_sach_hdqt()) {
                Cell nameCell = new Cell().add(new Paragraph(i.getTen()).setTextAlignment(TextAlignment.CENTER).setFont(font));
                Cell roleCell = new Cell().add(new Paragraph(i.getChucVu()).setTextAlignment(TextAlignment.CENTER).setFont(font));
                Cell addressCell = new Cell().add(new Paragraph(i.getDiaChi()).setTextAlignment(TextAlignment.CENTER).setFont(font));

                bangHoiDongQuanTri.addCell(nameCell);
                bangHoiDongQuanTri.addCell(roleCell);
                bangHoiDongQuanTri.addCell(addressCell);
            }
        }


        document.add(bangHoiDongQuanTri);

        document.add(new Paragraph("5.2 Ban điều hành").setFont(font).setFontSize(12));
        Table bangBanDieuHanh = new Table(3);
        bangBanDieuHanh.setWidth(UnitValue.createPercentValue(100));

        Cell cellName2 = new Cell().add(new Paragraph("Họ tên").setTextAlignment(TextAlignment.CENTER).setFont(font));
        Cell cellRole2 = new Cell().add(new Paragraph("Chức vụ").setTextAlignment(TextAlignment.CENTER).setFont(font));
        Cell cellAddress2 = new Cell().add(new Paragraph("Địa chỉ thường trú").setTextAlignment(TextAlignment.CENTER).setFont(font));
        bangBanDieuHanh.addCell(cellName2);
        bangBanDieuHanh.addCell(cellRole2);
        bangBanDieuHanh.addCell(cellAddress2);

        if (request.getDanh_sach_bdh() != null && !request.getDanh_sach_bdh().isEmpty()) {
            for (BanDieuHanh i : request.getDanh_sach_bdh()) {
                Cell nameCell = new Cell().add(new Paragraph(i.getTen()).setTextAlignment(TextAlignment.CENTER).setFont(font));
                Cell roleCell = new Cell().add(new Paragraph(i.getChucVu()).setTextAlignment(TextAlignment.CENTER).setFont(font));
                Cell addressCell = new Cell().add(new Paragraph(i.getDiaChi()).setTextAlignment(TextAlignment.CENTER).setFont(font));

                bangBanDieuHanh.addCell(nameCell);
                bangBanDieuHanh.addCell(roleCell);
                bangBanDieuHanh.addCell(addressCell);
            }
        }


        document.add(bangBanDieuHanh);


        document.add(new Paragraph("5.3 Kế toán trưởng").setFont(font).setFontSize(12));
        document.add(new Paragraph("Họ tên: " + request.getKe_toan_truong().getTen())
                .setFont(font).setFontSize(12));
        document.add(new Paragraph("Địa chỉ thường trú:" + request.getKe_toan_truong().getDiaChi())
                .setFont(font).setFontSize(12));

        // Cam kết của khách hàng
        Paragraph commit = new Paragraph("Tôi cam đoan và chịu hoàn toàn trách nhiệm về mọi thông tin đã cung cấp trên đây là đúng sự thật.")
                .setFont(font)
                .setFontSize(12);
        document.add(commit);

        //
        Table bangChuKyKhachHang = new Table(1);
        bangChuKyKhachHang.setWidth(UnitValue.createPercentValue(50));
        bangChuKyKhachHang.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        Cell cellChuKyKhachHang = new Cell()
                .add(new Paragraph("Ngày " + LocalDateTime.now().getDayOfMonth() + " tháng " + LocalDateTime.now()
                        .getMonth().getValue() + " năm " + LocalDateTime.now().getYear()).setItalic().setFont(font).setFontSize(12))
                .add(new Paragraph("NGƯỜI ĐẠI DIỆN HỢP PHÁP").setBold().setFont(font).setFontSize(12))
                .add(new Paragraph(customer.getName()).setItalic().setFont(font).setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER);

        bangChuKyKhachHang.addCell(cellChuKyKhachHang);

        document.add(bangChuKyKhachHang);

        //
        Table bangChuKyCuaCEP = new Table(3);
        bangChuKyCuaCEP.setWidth(UnitValue.createPercentValue(100));

        Cell cellTitle = new Cell(1, 3)
                .add(new Paragraph("PHẦN DÀNH CHO TỔ CHỨC TÀI CHÍNH VI MÔ CEP").setFontSize(12).setFont(font))
                .setTextAlignment(TextAlignment.CENTER).setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);

        bangChuKyCuaCEP.addCell(cellTitle);

        Cell cellGDV = new Cell().add(new Paragraph("Giao dịch viên").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellKSV = new Cell().add(new Paragraph("Kiểm soát viên").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellAdmin = new Cell().add(new Paragraph("Tổng giám đốc/Giám đốc chi nhánh/\nTrưởng phòng giao dịch").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);

        //Cần tạo thêm 3 Cell chứa chữ kí của GDV, KSV, employee_sa
        Cell cellGDVSig = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        Cell cellKSVSig = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        Cell cellEmployeeSaSig = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);

        if (pcrt04.getGdv()!=null)cellGDVSig.add(new Paragraph(pcrt04.getGdv().getName())).setFont(font).setFontSize(12).setItalic();
        if(pcrt04.getKsv()!=null)cellKSVSig.add(new Paragraph(pcrt04.getKsv().getName())).setFont(font).setFontSize(12).setItalic();
        if(pcrt04.getManager()!=null)cellEmployeeSaSig.add(new Paragraph(pcrt04.getManager().getName())).setFont(font).setFontSize(12).setItalic();


        bangChuKyCuaCEP.addCell(cellGDV);
        bangChuKyCuaCEP.addCell(cellKSV);
        bangChuKyCuaCEP.addCell(cellAdmin);

        bangChuKyCuaCEP.addCell(cellGDVSig);
        bangChuKyCuaCEP.addCell(cellKSVSig);
        bangChuKyCuaCEP.addCell(cellEmployeeSaSig);

        document.add(bangChuKyCuaCEP);
        document.close();
        System.out.println("PDF tạo thành công!");

        return new File(outputPath);
    }

    public PCRT_04 uploadCloud(File f, Customer customer, PCRT_04 pcrt04) throws IOException {
        pcrt04.setGdv(userService.getCurrentUser());
        pcrt04.setCustomer(customer);
        if (f != null) {
            Map res = cloudinary.uploader().upload(f, ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "PCRT/PCRT-04"
            ));
            pcrt04.setFilePath(res.get("secure_url").toString());
        }
        pcrt04Repository.save(pcrt04);

        System.out.println("uploaded !!");

        return pcrt04;
    }

    public void savePcrt04Detail(BusinessCustomerUpdateRequest request, PCRT_04 pcrt04) {
        PCRT_04_detail detail = new PCRT_04_detail();
        detail.setBusinessName(request.getPcrt04Detail().getBusinessName());
        detail.setForeignName(request.getPcrt04Detail().getForeignName());
        detail.setShortName(request.getPcrt04Detail().getShortName());
        detail.setParentOrganizationName(request.getPcrt04Detail().getParentOrganizationName());
        detail.setTaxCode(request.getPcrt04Detail().getTaxCode());
        detail.setBusinessOccupation(request.getPcrt04Detail().getBusinessOccupation());
        detail.setPhoneNumber(request.getPcrt04Detail().getPhoneNumber());
        detail.setFaxNumber(request.getPcrt04Detail().getFaxNumber());
        detail.setCustomerOccupation(request.getPcrt04Detail().getCustomerOccupation());
        detail.setCustomerOfficePhone(request.getPcrt04Detail().getCustomerOfficePhone());
        detail.setCustomerMobilePhone(request.getPcrt04Detail().getCustomerMobilePhone());
        detail.setPcrt04(pcrt04);

        //address
        Address address = new Address(
                request.getPcrt04Detail().getAddress().getDistrict(),
                request.getPcrt04Detail().getAddress().getProvince(),
                request.getPcrt04Detail().getAddress().getCountry(),
                "current"
        );
        detail.setAddress(addressRepository.save(address));

        //formatter for license issued date save
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        //License establishmentLicense
        License establishmentLicense = new License(
                request.getPcrt04Detail().getEstablishmentLicense().getNumber(),
                LocalDate.parse(request.getPcrt04Detail().getEstablishmentLicense().getIssuedDateTmp(), formatter),
                request.getPcrt04Detail().getEstablishmentLicense().getIssuedPlace(),
                "establishment"
        );

        //đặt issuedDateTmp và đối tượng establishmentLicense để Pass Validation
        establishmentLicense.setIssuedDateTmp(request.getPcrt04Detail().getEstablishmentLicense().getIssuedDateTmp());

        detail.setEstablishmentLicense(licenseRepository.save(establishmentLicense));


        //License businessRegistrationLicense
        License businessRegistrationLicense = new License(
                request.getPcrt04Detail().getBusinessRegistrationLicense().getNumber(),
                LocalDate.parse(request.getPcrt04Detail().getBusinessRegistrationLicense().getIssuedDateTmp(), formatter),
                request.getPcrt04Detail().getBusinessRegistrationLicense().getIssuedPlace(),
                "business_registration"
        );
        //đặt issuedDateTmp và đối tượng businessRegistrationLicense để Pass Validation
        businessRegistrationLicense.setIssuedDateTmp(request.getPcrt04Detail().getBusinessRegistrationLicense().getIssuedDateTmp());
        detail.setBusinessRegistrationLicense(licenseRepository.save(businessRegistrationLicense));


        //address customerPermanentAddress
        Address customerPermanentAddress = new Address(
                request.getPcrt04Detail().getCustomerPermanentAddress().getDistrict(),
                request.getPcrt04Detail().getCustomerPermanentAddress().getProvince(),
                request.getPcrt04Detail().getCustomerPermanentAddress().getCountry(),
                "permanent"
        );
        detail.setCustomerPermanentAddress(addressRepository.save(customerPermanentAddress));
        //address customerCurrentAddress
        Address customerCurrentAddress = new Address(
                request.getPcrt04Detail().getCustomerCurrentAddress().getDistrict(),
                request.getPcrt04Detail().getCustomerCurrentAddress().getProvince(),
                request.getPcrt04Detail().getCustomerCurrentAddress().getCountry(),
                "current"
        );
        detail.setCustomerCurrentAddress(addressRepository.save(customerCurrentAddress));

        pcrt04DetailRepository.save(detail);
        System.out.println("Lưu pcrt04 detail thành công !");
    }

    public PCRT_04_detail getPCRT_04DetailByCustomerId (int customerId){
        PCRT_04_detail detail = pcrt04RepositoryCriteria.getPCRT_04DetailByCustomerId(customerId);
        return detail;
    }

    public List<PCRT_04> getPCRT_04ListByCustomerId(int customerId) {
        return pcrt04RepositoryCriteria.getListPCRT_04ByCustomerId(customerId);
    }

    public void deletePCRT_04ByCustomer(int customerId) throws IOException {
        List<PCRT_04> listPCRT_04 = getPCRT_04ListByCustomerId(customerId);
        for (PCRT_04 form : listPCRT_04) {
            String publicId = form.getFilePath().replaceAll("^.*?/upload/v[0-9]+/", "")
                    .replaceAll("\\.[a-zA-Z0-9]+$", "");
            Map res = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            pcrt04Repository.delete(form);
        }


    }
}
