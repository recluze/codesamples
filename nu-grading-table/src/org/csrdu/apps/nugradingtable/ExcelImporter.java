package org.csrdu.apps.nugradingtable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelImporter {

	private Vector<StudentResult> results;
    private StudentClassResults classResults;
    private String predictedCourseCode = "";
    private String predictedCourseName = "";
    private String predictedInstructor = "";
    private String predictedSemester = "";
    private String predictedBatch = "";
    private String predictedSection = "";
    

	public ExcelImporter(StudentClassResults studentClassResults) {
		this.results = studentClassResults.getResults();
        this.classResults = studentClassResults;
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
			
			// try to predict the course code, course name, batch and semester 
			predictCourseMetaInfo(sheet);

			// Find the column with SNO ...
			System.out.println("Found total rows: " + sheet.getRows());

			for (int j = 0; j < sheet.getRows(); j++) {
				for (int i = 0; i < sheet.getColumns(); i++) {
					Cell cell = sheet.getCell(i, j);
					String cellContents = cell.getContents().trim();
					// System.out.println(cellContents);
					if (cellContents.toLowerCase().trim().contains("roll")) {
						snoCol = i;
						headerRow = j;
					}
				}
			}
			if(headerRow == -1) {
				// System.out.println("Couldn't find header row.");
				JOptionPane.showMessageDialog(null, "Could not find header row.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return; 
			}
			System.out.println("Found header row at: " + headerRow);

			// go to left on header row and collect all headers...
			Map<String, Integer> headers = new HashMap<String, Integer>();
			Vector<String> headerNames = new Vector<String>();
			Vector<Integer> headerCols = new Vector<Integer>();
			{
				int i = 0;
				while (i < sheet.getColumns()
						//&& !sheet.getCell(i, headerRow).getContents().trim()
						//		.equals("")) {
				        ){
					headers.put(sheet.getCell(i, headerRow).getContents(),new Integer(i));
					headerNames.add(sheet.getCell(i, headerRow).getContents());
					i++;
				}
			}

			// create a dialog box and get the headers corresponding to required
			// fields
			showHeaderDialog(headers, headerNames, headerCols);

			// find row for first and last student.
			boolean beenEmpty = true;
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

			if(beginStudentRow == -1) {
				// System.out.println("Couldn't find header row.");
				JOptionPane.showMessageDialog(null, "Could not find any student record.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return; 
			}
			
			System.out.println("Students rows from " + beginStudentRow + " to "
					+ endStudentRow);

			// now we loop from beginStudentRow to endStudentRow and populate
			// the data based on headerCols[x]
			for (int row = beginStudentRow; row <= endStudentRow; row++){
				StudentResult res = new StudentResult(); 
				res.setsNo(new Integer(sheet.getCell(headerCols.get(0), row).getContents()));
				res.setStudentID(sheet.getCell(headerCols.get(1), row).getContents());
				res.setStudentName(sheet.getCell(headerCols.get(2), row).getContents());
				res.setTotalMarks(new Double(sheet.getCell(headerCols.get(3), row).getContents()));
				res.setProposedGrade(sheet.getCell(headerCols.get(4), row).getContents());
				res.setSection(predictedSection);
				results.add(res);
			}
			
			
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

    private void predictCourseMetaInfo(Sheet sheet) {
        // Let's go through first 15 rows trying to get the course meta info
        // sample output from RADIX: "CS303 Software Engineering CS08 (Fall 2011)"
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < 1; i++) {
                Cell cell = sheet.getCell(i, j);
                String cellContents = cell.getContents().trim();
                // System.out.println(cellContents);
                // let's try to match the RE
                Pattern pattern = Pattern.compile("([a-zA-Z]{2}[0-9]{3}) (.*) ([a-zA-Z]{2}[0-9]{2})([a-zA-Z]?) \\((.*)\\)(.*)");
                Matcher matcher = pattern.matcher(cellContents);

                System.out.println("trying to match: " + cellContents);
                boolean found=false; 
                if (matcher.find()) {
                    System.out.format("I found the text"
                            + " \"%s\" starting at "
                            + "index %d and ending at index %d.%n \n",
                            matcher.group(), matcher.start(), matcher.end());
                    predictedCourseCode = cellContents.substring(
                            matcher.start(1), matcher.end(1));
                    System.out.println("Course code: " + predictedCourseCode);

                    predictedCourseName = cellContents.substring(
                            matcher.start(2), matcher.end(2));
                    System.out.println("Course Name: " + predictedCourseName);
                    
                    predictedBatch = cellContents.substring(
                            matcher.start(3), matcher.end(3));
                    System.out.println("Batch: " + predictedBatch);
                    
                    predictedSection = cellContents.substring(
                            matcher.start(4), matcher.end(4));
                    System.out.println("Section: " + predictedSection);
                    
                    predictedSemester = cellContents.substring(
                            matcher.start(5), matcher.end(5));
                    System.out.println("Semester: " + predictedSemester);

                    
                    found = true;
                }
                if(found){
                    return; 
                }
            }
        }
    }

    /** Show a dialog to get the mappings 
	 * 
	 * @param headers The headers collected from the sheet 
	 * @param headerNames 
	 * @param headerCols The column number returned 
     * @throws InterruptedException 
	 * */ 
	private void showHeaderDialog(Map<String, Integer> headers, Vector<String> headerNames, Vector<Integer> headerCols) {
		// setting options pane ... 
		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();

		
		// add all components 
		// sno 
		JComboBox comboSno = new JComboBox();
		for (String header : headerNames)
			comboSno.addItem(header);

		JLabel label = new JLabel("S.No.");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = 0; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		c2.gridx = 1; c2.gridy = 0; c2.weightx = 0.25;
		optionsPane.add(comboSno, c2);
		
		// studentID 
		JComboBox comboSID = new JComboBox();
		for (String header : headerNames)
			comboSID.addItem(header);

		label = new JLabel("Student ID");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = 1; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		c2.gridx = 1; c2.gridy = 1; c2.weightx = 0.25;
		optionsPane.add(comboSID, c2);

		// student name 
		JComboBox comboSName = new JComboBox();
		for (String header : headerNames){
			comboSName.addItem(header);
		}
			

		label = new JLabel("Student Name");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = 2; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		c2.gridx = 1; c2.gridy = 2; c2.weightx = 0.25;
		optionsPane.add(comboSName, c2);

		// Total marks
		JComboBox comboTMarks = new JComboBox();
		for (String header : headerNames)
			comboTMarks.addItem(header);

		label = new JLabel("Total Marks");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = 3; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		c2.gridx = 1; c2.gridy = 3; c2.weightx = 0.25;
		optionsPane.add(comboTMarks, c2);
		
		
		// Proposed Grade
		JComboBox comboPropGrade = new JComboBox();
		for (String header : headerNames)
			comboPropGrade.addItem(header);

		label = new JLabel("Proposed Grade");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = 4; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		c2.gridx = 1; c2.gridy = 4; c2.weightx = 0.25;
		optionsPane.add(comboPropGrade, c2);
		
		
		// let's try to predict the header columns 
		for (int i = 0; i < headerNames.size(); i++){
			if(headerNames.get(i).trim().toLowerCase().equals("no."))
				comboSno.setSelectedIndex(i);
			else if(headerNames.get(i).toLowerCase().contains("roll"))
				comboSID.setSelectedIndex(i);
			else if(headerNames.get(i).toLowerCase().contains("name"))
				comboSName.setSelectedIndex(i);
			else if(headerNames.get(i).toLowerCase().contains("total"))
				comboTMarks.setSelectedIndex(i);
			else if(headerNames.get(i).toLowerCase().contains("grade"))
				comboPropGrade.setSelectedIndex(i);
		}

		int gridRow = 5;
		// section etc
		label = new JLabel("Course Code");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = gridRow; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		JTextField txtCourseCode = new JTextField();
		txtCourseCode.setText(predictedCourseCode);
		c2.gridx = 1; c2.gridy = gridRow++; c2.weightx = 0.25;
		optionsPane.add(txtCourseCode, c2);

		label = new JLabel("Course Name");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = gridRow; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		JTextField txtCourseName = new JTextField();
		txtCourseName.setText(predictedCourseName);
		c2.gridx = 1; c2.gridy = gridRow++; c2.weightx = 0.25;
		optionsPane.add(txtCourseName, c2);

		label = new JLabel("Semester");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = gridRow; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		JTextField txtSemester= new JTextField();
		txtSemester.setText(predictedSemester);
		c2.gridx = 1; c2.gridy = gridRow++; c2.weightx = 0.25;
		optionsPane.add(txtSemester, c2);

		label = new JLabel("Batch");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = gridRow; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		JTextField txtBatch = new JTextField();
		txtBatch.setText(predictedBatch);
		c2.gridx = 1; c2.gridy = gridRow++; c2.weightx = 0.25;
		optionsPane.add(txtBatch, c2);

		label = new JLabel("Section");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0; c2.gridy = gridRow; c2.weightx = 0.25;
		optionsPane.add(label, c2);
		JTextField txtSection = new JTextField();
		txtSection.setText(predictedSection);
		c2.gridx = 1; c2.gridy = gridRow++; c2.weightx = 0.25;
		optionsPane.add(txtSection, c2);
		
		// now show the dialog 
		JOptionPane.showMessageDialog(null, optionsPane,
				"Specify Column Mappings", JOptionPane.PLAIN_MESSAGE);
		// let's get the selected header columns
		headerCols.add(headers.get(comboSno.getSelectedItem()));
		headerCols.add(headers.get(comboSID.getSelectedItem()));
		headerCols.add(headers.get(comboSName.getSelectedItem()));
		headerCols.add(headers.get(comboTMarks.getSelectedItem()));
		headerCols.add(headers.get(comboPropGrade.getSelectedItem()));
		this.predictedCourseCode = txtCourseCode.getText();
		this.predictedCourseName = txtCourseName.getText();
		this.predictedSemester = txtSemester.getText();
		this.predictedBatch = txtBatch.getText();
		this.predictedSection = txtSection.getText();
		
        // let's populate the thingy
        classResults.setCourseName(predictedCourseName);
        classResults.setCourseCode(predictedCourseCode);
        classResults.setBatch(predictedBatch);
        classResults.setSemester(predictedSemester);
        // sections will be set for individual students 
	}
}
