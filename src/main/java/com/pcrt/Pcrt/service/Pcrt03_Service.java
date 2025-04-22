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
import com.pcrt.Pcrt.entities.*;
import com.pcrt.Pcrt.repository.*;
import com.pcrt.Pcrt.repository.query.Pcrt03RepositoryQuery;
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
public class Pcrt03_Service {

    @Autowired
    private UserService userService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private Pcrt03Repository pcrt03Repository;
    @Autowired
    private Pcrt03RepositoryQuery pcrt03RepositoryCriteria;
    @Autowired
    private IdentifierRepository identifierRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerOfficeRepository customerOfficeRepository;
    @Autowired
    private Pcrt03DetailRepository pcrt03DetailRepository;





    public File createFile(PCRT_03_detail updateRequest, Customer customer) throws IOException {
        // Đường dẫn file PDF
        String outputPath = "form/pcrt-03/" + updateRequest.getPcrt03().getCustomer().getName() + "_" + customer.getNationalId() + ".pdf";

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
        Paragraph subtitle = new Paragraph("(Dành cho khách hàng cá nhân)")
                .setFont(font)
                .setItalic()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(subtitle);

        // Nội dung các trường thông tin
        String[] fields = {
                "1. Họ tên khách hàng: " + customer.getName(),
                "2. Số CMND/CCCD/Hộ chiếu: " + customer.getNationalId() + ".......Ngày cấp: " + updateRequest.getNationalIdentify().getIssuedDateTmp(),
                "3. Địa chỉ thường trú: " + updateRequest.getPermanentAddress().getDistrict() + ", " + updateRequest.getPermanentAddress().getProvince() + ", " + updateRequest.getPermanentAddress().getCountry(),
                "4. Địa chỉ hiện tại: " + updateRequest.getCurrentAddress().getDistrict() + ", " + updateRequest.getCurrentAddress().getProvince() + ", " + updateRequest.getCurrentAddress().getCountry(),
                "5. Mức thu nhập trung bình hàng tháng trong 06 tháng gần nhất trước thời điểm đánh giá:", ".........." + updateRequest.getAverageSalary() + " đồng.",
                "6. Thông tin cơ quan, tổ chức hoặc chủ cơ sở nơi làm việc hoặc nơi có thu nhập chính:",
                "    - Tên cơ quan: " + updateRequest.getOffice().getName(),
                "    - Địa chỉ cơ quan: " + updateRequest.getOffice().getAddress().getDistrict() + ", " + updateRequest.getOffice().getAddress().getProvince() + ", " + updateRequest.getOffice().getAddress().getCountry(),
                "    - Số điện thoại cơ quan: " + updateRequest.getOffice().getPhone(),
                "7. Thông tin liên quan đến nguồn tiền hoặc nguồn tài sản trong giao dịch của khách hàng:", updateRequest.getAdditionalInfo(),
                "................................................................................."
        };

        for (String field : fields) {
            document.add(new Paragraph(field).setFont(font).setFontSize(12));
        }

        // Cam kết của khách hàng
        Paragraph commit = new Paragraph("Tôi cam đoan và chịu hoàn toàn trách nhiệm về mọi thông tin đã cung cấp trên đây là đúng sự thật.")
                .setFont(font)
                .setFontSize(12);
        document.add(commit);


        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(50)); // Chiếm 50% chiều rộng trang
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT); // Đẩy về bên phải

        LocalDate localDateTime = LocalDate.now();
        Cell cell = new Cell()
                .add(new Paragraph("Ngày " + localDateTime.getDayOfMonth() + " tháng " + localDateTime.getMonth().getValue() + " năm " + localDateTime.getYear()).setFont(font))
                .add(new Paragraph("Khách hàng").setBold().setFont(font))
                .add(new Paragraph(updateRequest.getPcrt03().getCustomer().getName()).setFont(font))
                .setBorder(Border.NO_BORDER) // Ẩn đường viền bảng
                .setTextAlignment(TextAlignment.CENTER); // Căn giữa nội dung trong ô

        table.addCell(cell);
        document.add(table);

        document.close();
        System.out.println("PDF tạo thành công!");

        return new File(outputPath);
    }

    public PCRT_03 uploadCloud (File f, Customer customer) throws IOException {
        PCRT_03 pcrt03 = new PCRT_03(customer, "", userService.getCurrentUser(), LocalDateTime.now());
        if(f != null){
            Map res = cloudinary.uploader().upload(f, ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "PCRT/PCRT-03"
            ));
            pcrt03.setFilePath(res.get("secure_url").toString());
        }

        System.out.println("Tạo PCRT-03 thành công!");
        return pcrt03Repository.save(pcrt03);
    }

    // lấy danh sách pcrt-3 theo customer id
    public List<PCRT_03> getPCRT_03ListByCustomerId (int customerId){
        return pcrt03RepositoryCriteria.getListPCRT_03ByCustomerId(customerId);
    }

    public void deletePCRT_03ByCustomer (int customerId) throws IOException {
        List<PCRT_03> listPCRT_03 = getPCRT_03ListByCustomerId(customerId);
        for(PCRT_03 form: listPCRT_03){
            String publicId = form.getFilePath().replaceAll("^.*?/upload/v[0-9]+/", "")
                    .replaceAll("\\.[a-zA-Z0-9]+$", "");
            Map res = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            pcrt03Repository.delete(form);
        }
    }

    public void savePcrt03Detail (PCRT_03_detail detail, PCRT_03 pcrt03){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        //create nationalIdentify object
        Identifier nationalIdentify = detail.getNationalIdentify();
        nationalIdentify.setIssuedDate(LocalDate.parse(nationalIdentify.getIssuedDateTmp(), formatter));
        identifierRepository.save(nationalIdentify);

        //create passportIdentify object
        Identifier passportIdentify = detail.getPassportIdentify();
        passportIdentify.setIssuedDate(LocalDate.parse(passportIdentify.getIssuedDateTmp(), formatter));
        identifierRepository.save(passportIdentify);

        //create currentAddress object
        Address currentAddress = detail.getCurrentAddress();
        currentAddress.setType("current");
        addressRepository.save(currentAddress);

        //create permanentAddress obj
        Address permanentAddress = detail.getPermanentAddress();
        permanentAddress.setType("permanent");
        addressRepository.save(permanentAddress);


        //create officeAddress obj
        Address officeAddress = detail.getOffice().getAddress();
        officeAddress.setType("current");
        addressRepository.save(officeAddress);

        //create office obj
        CustomerOffice office = detail.getOffice();
        office.setAddress(officeAddress);
        customerOfficeRepository.save(office);

        //create pcrt03_detail obj
        PCRT_03_detail pcrt03Detail = new PCRT_03_detail();
        pcrt03Detail.setPcrt03(pcrt03);
        pcrt03Detail.setNationalIdentify(nationalIdentify);
        pcrt03Detail.setPassportIdentify(nationalIdentify);
        pcrt03Detail.setCurrentAddress(currentAddress);
        pcrt03Detail.setPermanentAddress(permanentAddress);
        pcrt03Detail.setAverageSalary(detail.getAverageSalary());
        pcrt03Detail.setOffice(office);
        pcrt03Detail.setAdditionalInfo(detail.getAdditionalInfo());
        pcrt03Detail.setPhoneNumber(detail.getPhoneNumber());
        pcrt03Detail.setOccupation(detail.getOccupation());
        pcrt03DetailRepository.save(pcrt03Detail);

        System.out.println("Lưu thông tin chi tiết PCRT-03 thành công");
    }

    public PCRT_03_detail getPCRT_03DetailByCustomerId (int customerId){


        return pcrt03RepositoryCriteria.getPCRT_03DetailByCustomerId(customerId);
    }
}
