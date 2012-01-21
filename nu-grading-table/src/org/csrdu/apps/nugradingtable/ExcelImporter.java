package org.csrdu.apps.nugradingtable;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelImporter {

	private Vector<StudentResult> results;

	public ExcelImporter(Vector<StudentResult> results) {
		this.results = results;
	}

	public void importDataFromFile(File file) {
		// Vector data = new Vector();
		int snoCol = -1;
		int headerRow = -1;
		int beginStudentRow = -1;
		int endStudentRow = -1;
		try {
			Workbook workbook = null;
			try {
				workbook = Workbook.getWorkbook(file);
			} catch (IOException ex) {
				System.out.println("Error reading excel file.");
				ex.printStackTrace();
			}
			Sheet sheet = workbook.getSheet(0);

			// Find the column with SNO ... 
			// TODO: plus all required columns 
			System.out.println("Found total rows: " + sheet.getRows());

			for (int j = 0; j < sheet.getRows(); j++) {
				for (int i = 0; i < sheet.getColumns(); i++) {
					Cell cell = sheet.getCell(i, j);
					String cellContents = cell.getContents().trim();
					if ("S. No.".trim().equalsIgnoreCase(cellContents)) {
						snoCol = i;
						headerRow = j;
					}
				}
			}

			System.out.println("Found header row at: " + headerRow);

			boolean beenEmpty = true;
			// find row for first and last student.
			for (int i = headerRow + 1; i < sheet.getRows(); i++) {
				if (!sheet.getCell(snoCol, i).getContents().trim().equals("")) {
					// we have content and this HAS to be the first student
					if (beenEmpty) {
						beginStudentRow = i;
						beenEmpty = false; // no longer empty
					}
				}
				if (sheet.getCell(snoCol, i).getContents().trim().equals("")) {
					// we have not been empty and we just got empty. So, the row
					// before this one was the last student and we're done
					if (!beenEmpty) {
						endStudentRow = i - 1;
						break;
					}
				}
			}
			// if we still don't have endStudentRow, it's the last one.
			if (endStudentRow == -1)
				endStudentRow = sheet.getRows();

			System.out.println("Students rows from " + beginStudentRow + " to "
					+ endStudentRow);
			
			// now we loop from beginStudentRow to endStudentRow and populate the data! 
			// data.clear();
			// System.out.println("Num rows: " + sheet.getRows());
			// for (int j = 1; j < sheet.getRows(); j++) {
			// Vector d = new Vector();
			// for (int i = 0; i < sheet.getColumns(); i++) {
			//
			// Cell cell = sheet.getCell(i, j);
			// System.out.println("Col (" + j + "," + i + "):  "
			// + cell.getContents());
			// d.add(cell.getContents());
			//
			// }
			// d.add("\n");
			// data.add(d);
			// }
			// } catch (BiffException e) {
			// e.printStackTrace();
			// }
			// System.out.println("Done reading.");
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
}
