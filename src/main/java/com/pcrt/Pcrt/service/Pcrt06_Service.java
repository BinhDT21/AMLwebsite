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
import com.pcrt.Pcrt.entities.PCRT_06;
import com.pcrt.Pcrt.entities.PCRT_06_item;
import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.repository.Pcrt06Repository;
import com.pcrt.Pcrt.repository.query.Pcrt06RepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class Pcrt06_Service {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private Pcrt06Repository pcrt06Repository;
    @Autowired
    private Pcrt06RepositoryQuery pcrt06RepositoryQuery;
    @Autowired
    private UserService userService;


    public Page<PCRT_06> getListPCRT_06 (Map<String, String> params){

        List<PCRT_06> pcrt06List = pcrt06RepositoryQuery.getListPCRT_06(params);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        long totalPages = pcrt06RepositoryQuery.countPCRT_06();

        return new PageImpl<>(pcrt06List, PageRequest.of(page, 10), totalPages);
    }

    public String uploadPCRT06(PCRT_06 pcrt_06) throws IOException {
        String outputPath = "form/pcrt-06/" + pcrt_06.getId() + ".pdf";

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
        Paragraph title = new Paragraph("Danh sách khách hàng, giao dịch có rủi ro cao cần thu thập bổ sung thông tin")
                .setFont(font)
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        //bang pcrt06
        Table bangPCRT06 = new Table(10);
        bangPCRT06.setWidth(UnitValue.createPercentValue(100));

        Cell cellSTT = new Cell(2, 1).add(new Paragraph("STT").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellNgayThucHienGiaoDich = new Cell(2, 1).add(new Paragraph("Ngày thực hiện giao dịch").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellTenKhachHang = new Cell(2, 1).add(new Paragraph("Tên khách hàng").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellDiaChi = new Cell(2, 1).add(new Paragraph("Địa chỉ").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellGiayToNhanDang = new Cell(1, 4).add(new Paragraph("Giấy tờ nhận dạng").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellTongSoTienGiaoDichTrongNgay = new Cell(2, 1).add(new Paragraph("Tổng số tiền giao dịch trong ngày").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        Cell cellTangSuat = new Cell(2, 1).add(new Paragraph("Tầng suất giao dịch trong ngày").setFont(font).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        Cell cellSoCMND = new Cell(1, 1).add(new Paragraph("Số CMND/CCCD").setFont(font).setTextAlignment(TextAlignment.CENTER));
        Cell cellSoHoChieu = new Cell(1, 1).add(new Paragraph("Số hộ chiếu").setFont(font).setTextAlignment(TextAlignment.CENTER));
        Cell cellSoDangKD = new Cell(1, 1).add(new Paragraph("Số đăng KD").setFont(font).setTextAlignment(TextAlignment.CENTER));
        Cell cellMaSoThue = new Cell(1, 1).add(new Paragraph("Mã số thuế").setFont(font).setTextAlignment(TextAlignment.CENTER));

        bangPCRT06.addCell(cellSTT);
        bangPCRT06.addCell(cellNgayThucHienGiaoDich);
        bangPCRT06.addCell(cellTenKhachHang);
        bangPCRT06.addCell(cellDiaChi);
        bangPCRT06.addCell(cellGiayToNhanDang);
        bangPCRT06.addCell(cellTongSoTienGiaoDichTrongNgay);
        bangPCRT06.addCell(cellTangSuat);

        bangPCRT06.addCell(cellSoCMND);
        bangPCRT06.addCell(cellSoHoChieu);
        bangPCRT06.addCell(cellSoDangKD);
        bangPCRT06.addCell(cellMaSoThue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //duyet qua pcrt06 list để tạo dòng
        for (int i = 0; i < pcrt_06.getItemList().size(); i++) {
            Cell cellId = new Cell().add(new Paragraph(String.valueOf(i + 1)).setFont(font));
            Cell cellTransactionDate = new Cell().add(new Paragraph(formatter.format(pcrt_06.getCreatedDate())).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellCustomerName = new Cell().add(new Paragraph(pcrt_06.getItemList().get(i).getCustomer().getName()).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellCustomerAddress = new Cell().add(new Paragraph(pcrt_06.getItemList().get(i).getCustomer().getAddress()).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellNationalId = new Cell().add(new Paragraph(pcrt_06.getItemList().get(i).getCustomer().getNationalId()).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellPassport = new Cell().add(new Paragraph(pcrt_06.getItemList().get(i).getCustomer().getPassport()).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellSDKD = new Cell();
            Cell cellVatCode = new Cell();
            Cell cellAmount = new Cell().add(new Paragraph(String.valueOf(pcrt_06.getItemList().get(i).getAmount())).setTextAlignment(TextAlignment.CENTER).setFont(font));
            Cell cellFrequency = new Cell().add(new Paragraph(String.valueOf(pcrt_06.getItemList().get(i).getFrequency())).setTextAlignment(TextAlignment.CENTER).setFont(font));

            bangPCRT06.addCell(cellId);
            bangPCRT06.addCell(cellTransactionDate);
            bangPCRT06.addCell(cellCustomerName);
            bangPCRT06.addCell(cellCustomerAddress);
            bangPCRT06.addCell(cellNationalId);
            bangPCRT06.addCell(cellPassport);
            bangPCRT06.addCell(cellSDKD);
            bangPCRT06.addCell(cellVatCode);
            bangPCRT06.addCell(cellAmount);
            bangPCRT06.addCell(cellFrequency);
        }
        //
        document.add(bangPCRT06);


        //
        Table bangChuKyCuaCEP = new Table(3);
        bangChuKyCuaCEP.setWidth(UnitValue.createPercentValue(100));


        Cell cellGDV = new Cell().add(new Paragraph("Giao dịch viên").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellKSV = new Cell().add(new Paragraph("Kiểm soát viên").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellAdmin = new Cell().add(new Paragraph("Tổng giám đốc/Giám đốc chi nhánh/\nTrưởng phòng giao dịch").setFont(font).setBold().setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);

        Cell cellGDVSignature = new Cell().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellKSVSignature = new Cell().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        Cell cellAdminSignature = new Cell().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);

        if(pcrt_06.getGdv() != null)
            cellGDVSignature.add(new Paragraph(pcrt_06.getGdv().getName()).setFont(font));
        if(pcrt_06.getKsv() != null)
            cellKSVSignature.add(new Paragraph(pcrt_06.getKsv().getName()).setFont(font));
        if(pcrt_06.getManager() != null)
            cellAdminSignature.add(new Paragraph(pcrt_06.getManager().getName()).setFont(font));

        bangChuKyCuaCEP.addCell(cellGDV);
        bangChuKyCuaCEP.addCell(cellKSV);
        bangChuKyCuaCEP.addCell(cellAdmin);

        bangChuKyCuaCEP.addCell(cellGDVSignature);
        bangChuKyCuaCEP.addCell(cellKSVSignature);
        bangChuKyCuaCEP.addCell(cellAdminSignature);

        document.add(bangChuKyCuaCEP);
        document.close();
        System.out.println("PDF tạo thành công!");


        // tạo file sau đó đưa lên cloud
        File f = new File(outputPath);

        Map res = cloudinary.uploader().upload(f, ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "PCRT/PCRT-06"
        ));

        return res.get("secure_url").toString();
    }

    public PCRT_06 getPcrt_06ById (int pcrt06Id){
        return pcrt06Repository.findById(pcrt06Id).orElseThrow(RuntimeException::new);
    }

    public void deleteFilePath (int pcrt06Id) throws IOException {
        PCRT_06 pcrt_06 = getPcrt_06ById(pcrt06Id);
        String publicId = pcrt_06.getFilePath().replaceAll("^.*?/upload/v[0-9]+/", "")
                .replaceAll("\\.[a-zA-Z0-9]+$", "");
        Map res = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        pcrt_06.setFilePath(null);
        pcrt06Repository.save(pcrt_06);
    }

    public void updatePCRT06 (int pcrt06Id) throws IOException {
        PCRT_06 pcrt_06 = getPcrt_06ById(pcrt06Id);
        deleteFilePath(pcrt06Id);

        User currentUser = userService.getCurrentUser();
        switch (currentUser.getRole()){
            case "GDV":
                pcrt_06.setGdv(currentUser);break;
            case "KSV":
                pcrt_06.setKsv(currentUser);break;
            case "AML_MANAGER":
                pcrt_06.setManager(currentUser);break;
        }

        String filePath = uploadPCRT06(pcrt_06);

        pcrt_06.setFilePath(filePath);
        pcrt06Repository.save(pcrt_06);
    }
}
