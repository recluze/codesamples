package org.csrdu.apps.nugradingtable;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.table.TableModel;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GradingTablePrintManager {

	public static void print(TableModel model) {

		try {
			Document d = new Document(PageSize.A4);
			PdfWriter.getInstance(d, new FileOutputStream("sample.pdf"));
			d.open();
			Phrase p = new Phrase("Some sample Text");
			Phrase p2 = new Phrase("More of the text here with details... ");

			PdfPTable t = new PdfPTable(2);
			PdfPCell c1 = new PdfPCell(p);
			c1.setPadding(3f);
			t.addCell(c1);
			PdfPCell c2 = new PdfPCell(p2);
			c2.setPadding(3f);
			t.addCell(c2);

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
