package com.pcrt.Pcrt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.pcrt.Pcrt.dto.response.CustomerCheckingResponse;
import com.pcrt.Pcrt.entities.Blacklist;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Kqks;
import com.pcrt.Pcrt.repository.KqksRepository;
import com.pcrt.Pcrt.repository.query.KqksRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class KqksService {

    @Autowired
    private UserService userService;
    @Autowired
    private KqksRepository kqksRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private KqksRepositoryQuery kqksRepositoryCriteria;

    public File createFile(CustomerCheckingResponse response, Customer customer) throws IOException {

        // Đường dẫn file PDF
        String outputPath = "form/kqks/" + customer.getName() + "_" + customer.getNationalId() + ".pdf";

        // Khởi tạo PdfWriter và PdfDocument
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Font Unicode (Arial)
        PdfFont font = PdfFontFactory.createFont("static\\font\\arial.ttf", "Identity-H");

        // tiêu đề tổ chức
        ImageData imageData = ImageDataFactory.create("https://res.cloudinary.com/dwdvnztnn/image/upload/v1742350980/PCRT/aml_form_header_uj0l0n.png");
        Image image = new Image(imageData);
        document.add(image);

        // ======= Thông tin khách hàng ======
        Paragraph customerTitle = new Paragraph("THÔNG TIN KHÁCH HÀNG")
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(customerTitle);

        // ====== Tạo bảng thông tin khách hàng
        float[] columnCustomerWidths = {100, 80, 100, 100, 100, 80, 80, 50}; // Kích thước cột
        Table tableCustomer = new Table(columnCustomerWidths);
        tableCustomer.setWidth(UnitValue.createPercentValue(100));

        String[] customerTableHeader = {"Họ tên", "Ngày sinh", "Địa chỉ", "Thành phố", "Quốc tịch", "CCCD/MST", "Hộ chiếu", "Loại KH"};
        for (String header : customerTableHeader) {
            tableCustomer.addHeaderCell(new Cell().add(new Paragraph(header).setFont(font).setFontSize(8).setBold()));
        }
        String[][] customerTableData = {
                {customer.getName(), customer.getDob(), customer.getAddress(), customer.getCity(), customer.getCountry(), customer.getNationalId(), customer.getPassport(), customer.getType()}
        };
        for (String[] row : customerTableData) {
            for (String cell : row) {
                tableCustomer.addCell(new Cell().add(new Paragraph(cell).setFont(font).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            }
        }
        document.add(tableCustomer);


        // ======= Tạo tiêu đề =======
        Paragraph title = new Paragraph(response.getResult())
                .setFont(font)
                .setFontSize(16)
                .setBold()
                .setFontColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        if(!response.getBlacklists().isEmpty()){
            // ======= Tạo bảng =======
            float[] columnWidths = {20, 100, 100, 80, 80, 80, 100, 120, 100, 120, 100, 100}; // Kích thước cột
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));
            // Danh sách tiêu đề cột
            String[] headers = {"id", "Họ tên", "CCCD/MST", "Ngày sinh", "Loại KH", "Quốc tịch",
                    "Sdt", "Email", "Địa chỉ", "Lý do", "Trạng thái", "Nguồn"};
            // Thêm tiêu đề bảng
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setFont(font).setFontSize(8).setBold()));
            }

            // Thêm dữ liệu vào bảng
            for (Blacklist bl : response.getBlacklists()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getId())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getName())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getNationalId())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getDob())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getType())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getCountry())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getPhoneNumber())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getEmail())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getAddress())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getReason())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getStatus())).setFont(font).setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(bl.getSource())).setFont(font).setFontSize(10)));
            }

            document.add(table);
        }

        // Đóng tài liệu
        document.close();
        System.out.println("PDF đã được tạo thành công: " + outputPath);


        return new File(outputPath);
    }

    public void uploadCloud (File f, Customer customer) throws IOException {
        Kqks kqks = new Kqks(customer, "", userService.getCurrentUser(), LocalDateTime.now());

        if(f != null){
            Map res = cloudinary.uploader().upload(f, ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "PCRT/KQKS"
            ));
            kqks.setFilePath(res.get("secure_url").toString());
        }
        kqksRepository.save(kqks);
        System.out.println("uploaded !!");
    }

    public List<Kqks> getListKqksByCustomer (int customerId){
        return kqksRepositoryCriteria.getListKqksByCustomer(customerId);
    }

    public void deleteKqksByCustomer (int customerId) throws IOException {
        List<Kqks> kqksList = this.getListKqksByCustomer(customerId);

        for(Kqks i : kqksList){
            String publicId = i.getFilePath().replaceAll("^.*?/upload/v[0-9]+/", "")
                    .replaceAll("\\.[a-zA-Z0-9]+$", "");
            Map res = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            kqksRepository.delete(i);
        }
    }

}
