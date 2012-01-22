package org.csrdu.apps.nugradingtable;

import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.io.File;

public class StudentClassResults {
	private Vector<StudentResult> results;
	private String resultsFilename;
	private String courseCode = "";
	private String courseName = "";
	private double addCurve = 0.0;
	private double mulCurve = 1.0;
	private boolean isRound = false;

	public StudentClassResults() {
		results = new Vector<StudentResult>();
		// importFromExcel("res/CS598.xls");
	}

	/**
	 * @return the resultsFilename
	 */
	public String getResultsFilename() {
		return resultsFilename;
	}

	/**
	 * @param resultsFilename
	 *            the resultsFilename to set
	 */
	public void setResultsFilename(String resultsFilename) {
		this.resultsFilename = resultsFilename;
	}

	/** Read from file and save filename for future saving */
	public void readResultsFromFile(String filename) {
		this.resultsFilename = filename;
		// TODO: read XML from file and populate collection
	}

	/** Save modifications to file */
	public void saveFile() {
		// TODO: insert save-XML code
	}

	public TableModel getRawData() {
		// TODO: generate results from data
		return (TableModel) new GradeTableModel(this);
	}

	/**
	 * @return the results
	 */
	public Vector<StudentResult> getResults() {
		return results;
	}

	public void importFromExcel(String filename) {
		ExcelImporter eI = new ExcelImporter(results);
		eI.importDataFromFile(new File(filename));
	}

	public void setOptions(String courseCode, String courseName,
			double addCurve, double mulCurve, boolean round) {
		this.setCourseCode(courseCode);
		this.setCourseName(courseName);
		this.setAddCurve(addCurve);
		this.setMulCurve(mulCurve);
		this.setRound(round);
	}

	/**
	 * @return the addCurve
	 */
	public double getAddCurve() {
		return addCurve;
	}

	/**
	 * @param addCurve
	 *            the addCurve to set
	 */
	public void setAddCurve(double addCurve) {
		this.addCurve = addCurve;
		// populate for all student results
		for (StudentResult res : results)
			res.setAddCurve(addCurve);
	}

	/**
	 * @return the mulCurve
	 */
	public double getMulCurve() {
		return mulCurve;
	}

	/**
	 * @param mulCurve
	 *            the mulCurve to set
	 */
	public void setMulCurve(double mulCurve) {
		this.mulCurve = mulCurve;
		// populate for all student results
		for (StudentResult res : results)
			res.setMulCurve(mulCurve);
	}

	/**
	 * @return the isRound
	 */
	public boolean isRound() {
		return isRound;
	}

	/**
	 * @param isRound
	 *            the isRound to set
	 */
	public void setRound(boolean isRound) {
		this.isRound = isRound;
		// populate for all student results
		for (StudentResult res : results)
			res.setShouldRound(isRound);
	}

	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}

	/**
	 * @param courseCode
	 *            the courseCode to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void populateFinalGrades() {
		// copy all calculated results to final grades 
		for (StudentResult res : results){ 
			res.setFinalGrade(res.getCalculatedGrade());
		}
	}

	public int countGrade(String grade) {
		int gradeCount = 0; 
		for(StudentResult res : results){ 
			if(res.getFinalGrade().equals(grade))
				gradeCount++; 
		}
		return gradeCount;
	}
}

class GradeTableModel implements TableModel {
	private StudentClassResults classResults;

	final String[] columnNames = { "S.No.", "Student ID", "Student Name",
			"Total Marks", "Proposed Grade", "Curved Marks",
			"Calculated Grade", "Final Grade" , "Section"};

	public GradeTableModel(StudentClassResults studentClassResults) {
		this.classResults = studentClassResults;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return classResults.getResults().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		StudentResult res = this.classResults.getResults().get(row);
		if (col == 0) {
			return res.getsNo();
		} else if (col == 1) {
			return res.getStudentID();
		} else if (col == 2) {
			return res.getStudentName();
		} else if (col == 3) {
			return res.getTotalMarks();
		} else if (col == 4) {
			return res.getProposedGrade();
		} else if (col == 5) {
			return res.getCalculatedMarks();
		} else if (col == 6) {
			return res.getCalculatedGrade();
		} else if (col == 7) {
			return res.getFinalGrade();
		} else if (col == 8) {
			return res.getSection();
		} else 
			return "INVALID COL";
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		StudentResult res = this.classResults.getResults().get(row);
		if (col == 7) {
			res.setFinalGrade((String) value);
		}
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
