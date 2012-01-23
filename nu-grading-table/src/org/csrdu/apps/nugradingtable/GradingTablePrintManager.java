package org.csrdu.apps.nugradingtable;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.table.TableModel;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GradingTablePrintManager {

    public static void print(TableModel model, String courseCode,
            String courseName) {

        try {
            // intialize document
            Font titleFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
            Font boldFont = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
            Font normalFont = new Font(Font.TIMES_ROMAN, 12, Font.NORMAL);

            Document d = new Document(PageSize.A4);
            PdfWriter.getInstance(d, new FileOutputStream("sample.pdf"));
            d.open();

            // set the title
            Paragraph code = new Paragraph(courseCode + " " +  courseName, titleFont);
            //            Paragraph name = new Paragraph(courseName, titleFont);

            d.add(code);
            // d.add(name);

            // set table headers
            float[] columnWidths = { 5f, 10f, 25f, 10f };

            Phrase pSNo = new Phrase("S. No.", boldFont);
            Phrase pRollNo = new Phrase("Roll No.", boldFont);
            Phrase pSName = new Phrase("Student Name", boldFont);
            Phrase pSGrade = new Phrase("Grade", boldFont);

            PdfPTable t = new PdfPTable(columnWidths);
            t.setSpacingBefore(20f);
            PdfPCell cSNo = new PdfPCell(pSNo);
            cSNo.setPadding(3f);
            cSNo.setBorderWidthBottom(1f);
            t.addCell(cSNo);
            PdfPCell cRollNo = new PdfPCell(pRollNo);
            cRollNo.setPadding(3f);
            cRollNo.setBorderWidthBottom(1f);
            t.addCell(cRollNo);
            PdfPCell cSName = new PdfPCell(pSName);
            cSName.setPadding(3f);
            cSName.setBorderWidthBottom(1f);
            t.addCell(cSName);
            PdfPCell cSGrade = new PdfPCell(pSGrade);
            cSGrade.setPadding(3f);
            cSGrade.setBorderWidthBottom(1f);
            t.addCell(cSGrade);

            // set table data
            for (int i = 0; i < model.getRowCount(); i++) {
                pSNo = new Phrase(model.getValueAt(i, 0).toString(), normalFont);
                pRollNo = new Phrase(model.getValueAt(i, 1).toString(),
                        normalFont);
                pSName = new Phrase(model.getValueAt(i, 2).toString(),
                        normalFont);
                pSGrade = new Phrase(model.getValueAt(i, 7).toString(),
                        normalFont);

                cSNo = new PdfPCell(pSNo);
                cSNo.setPadding(3f);
                t.addCell(cSNo);
                cRollNo = new PdfPCell(pRollNo);
                cRollNo.setPadding(3f);
                t.addCell(cRollNo);
                cSName = new PdfPCell(pSName);
                cSName.setPadding(3f);
                t.addCell(cSName);
                cSGrade = new PdfPCell(pSGrade);
                cSGrade.setPadding(3f);
                t.addCell(cSGrade);
            }

            // add table and close document
            d.add(t);
            d.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // try to open it
        try {
            File myFile = new File("sample.pdf");
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            // no application registered for PDFs
        }

    }

}
