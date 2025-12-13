package main.java.com.project.app.pdfwriter;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import main.java.com.project.app.dao.PdfSPDAO;
import main.java.com.project.app.model.PdfSPModel;
import main.java.com.project.app.model.PdfJobModel;
import main.java.com.project.app.model.PdfItemModel;

import java.io.File;
import java.util.List;

public class PdfExporter {

    public static void generatePdf(int spId) throws Exception {
        // ===== Ambil data dari DAO =====
        PdfSPDAO dao = new PdfSPDAO();
        PdfSPModel pdfSP = dao.getPdfSPById(spId);

        // Folder utama
        String folderAPath = System.getProperty("user.home") + "/Desktop/SuratPenawaran";
        File folderA = new File(folderAPath);
        if (!folderA.exists()) {
            folderA.mkdirs(); // bikin FolderA
        }

        // FolderB di dalam FolderA
        File folderB = new File(folderAPath + "/tmp");
        if (!folderB.exists()) {
            folderB.mkdirs();
        }

        // FolderC di dalam FolderA
        File folderC = new File(folderAPath + "/fixed");
        if (!folderC.exists()) {
            folderC.mkdirs();
        }

        String noSp = pdfSP.getNoSP();
        String templateHeader = "TemplateSP.pdf";
        String templateFooter = "FooterTemplateSP.pdf";
        String tmpHeaderFilled = System.getProperty("user.home") + "/Desktop/SuratPenawaran/tmp/tmp_header_filled.pdf";
        String tmpFooterFilled = System.getProperty("user.home") + "/Desktop/SuratPenawaran/tmp/tmp_footer_filled.pdf";
        String output = System.getProperty("user.home") + "/Desktop/SuratPenawaran/tmp/tmp_output_filled.pdf";

        // ===== 1. Header =====
        PdfDocument headerPdf = new PdfDocument(new PdfReader(templateHeader), new PdfWriter(tmpHeaderFilled));
        PdfAcroForm headerForm = PdfAcroForm.getAcroForm(headerPdf, true);

        if(headerForm.getField("tanggal") != null)
            headerForm.getField("tanggal").setValue(pdfSP.getTanggal().toString());
        if(headerForm.getField("no_sp") != null)
            headerForm.getField("no_sp").setValue(pdfSP.getNoSP());
        if(headerForm.getField("perihal") != null)
            headerForm.getField("perihal").setValue(pdfSP.getPerihal());
        if(headerForm.getField("nama_perusahaan") != null)
            headerForm.getField("nama_perusahaan").setValue(pdfSP.getNamaPerusahaan());
        if(headerForm.getField("alamat_perusahaan") != null)
            headerForm.getField("alamat_perusahaan").setValue(pdfSP.getAlamatPerusahaan());

        headerForm.flattenFields();
        headerPdf.close();

        // ===== 2. Footer =====
        PdfDocument footerPdf = new PdfDocument(new PdfReader(templateFooter), new PdfWriter(tmpFooterFilled));
        PdfAcroForm footerForm = PdfAcroForm.getAcroForm(footerPdf, true);

        double totalKeseluruhan = 0;
        for(PdfJobModel job : pdfSP.getJobs()){
            for(PdfItemModel item : job.getItems()){
                totalKeseluruhan += item.getQty() * item.getHarga();
            }
        }
        double ppn = totalKeseluruhan * 0.11;
        double grandTotal = totalKeseluruhan + ppn;

        if(footerForm.getField("total") != null)
            footerForm.getField("total").setValue("Rp " + String.format("%,.0f", totalKeseluruhan), true);
        if(footerForm.getField("ppn") != null)
            footerForm.getField("ppn").setValue("Rp " + String.format("%,.0f", ppn), true);
        if(footerForm.getField("grand_total") != null)
            footerForm.getField("grand_total").setValue("Rp " + String.format("%,.0f", grandTotal), true);

        footerForm.flattenFields();
        footerPdf.close();

        // ===== 3. Gabung header + jobs =====
        PdfDocument finalPdf = new PdfDocument(new PdfReader(tmpHeaderFilled), new PdfWriter(output));
        Document doc = new Document(finalPdf);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        doc.setFont(fontNormal).setFontSize(9);
        doc.setMargins(50,50,50,20);

        doc.add(new Paragraph(" ").setHeight(210).setMargin(0).setPadding(0));

        int jobCount = 1;
        for(PdfJobModel job : pdfSP.getJobs()){
            doc.add(new Paragraph(jobCount + ". " + job.getNamaPekerjaan())
                    .setFont(fontBold)
                    .setFontSize(10)
                    .setMarginBottom(5));

            Table table = new Table(UnitValue.createPercentArray(new float[]{4,1,2,2}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Komponen");
            table.addHeaderCell("Qty");
            table.addHeaderCell("Harga");
            table.addHeaderCell("Subtotal");

            for(PdfItemModel item : job.getItems()){
                double subtotal = item.getQty() * item.getHarga();
                table.addCell(item.getNamaItem());
                table.addCell(String.valueOf(item.getQty()));
                table.addCell("Rp " + String.format("%,.0f", item.getHarga()));
                table.addCell("Rp " + String.format("%,.0f", subtotal));
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            jobCount++;
        }

        doc.close();

        // ===== 4. Overlay footer di halaman terakhir =====
        PdfDocument finalPdf2 = new PdfDocument(new PdfReader(output),
                new PdfWriter(System.getProperty("user.home") + "/Desktop/SuratPenawaran/fixed/Surat_" + noSp +".pdf"));
        PdfDocument footerFilledPdf = new PdfDocument(new PdfReader(tmpFooterFilled));

        PdfPage lastPage = finalPdf2.getPage(finalPdf2.getNumberOfPages());
        PdfFormXObject footerXObject = footerFilledPdf.getFirstPage().copyAsFormXObject(finalPdf2);

        new com.itextpdf.layout.Canvas(new com.itextpdf.kernel.pdf.canvas.PdfCanvas(lastPage), lastPage.getPageSize())
                .add(new com.itextpdf.layout.element.Image(footerXObject)
                        .setFixedPosition(lastPage.getPageSize().getLeft(), lastPage.getPageSize().getBottom())
                        .scaleToFit(lastPage.getPageSize().getWidth(), footerXObject.getHeight())
                );

        footerFilledPdf.close();
        finalPdf2.close();

        System.out.println("✅ PDF final selesai: header + jobs + footer dari DB");
    }
}
