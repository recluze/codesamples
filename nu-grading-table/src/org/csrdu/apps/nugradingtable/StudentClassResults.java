package org.csrdu.apps.nugradingtable;

import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.io.File;

public class StudentClassResults {
	private Vector<StudentResult> results;
	private String resultsFilename;

	public StudentClassResults() {
		results = new Vector<StudentResult>();
		// StudentResult a = new StudentResult();
		// a.setsNo(1);
		// a.setStudentID("10-1005");
		// a.setShouldRound(true);
		// a.setProposedGrade("A");
		// a.setAddCurve(2);
		// a.setTotalMarks(87.01);
		// a.setStudentName("Blah");
		// results.add(a);
		//
		// a = new StudentResult();
		// a.setsNo(2);
		// a.setTotalMarks(60);
		// a.setMulCurve(1.51);
		// a.setProposedGrade("D");
		// a.setStudentID("11-1002");
		// a.setStudentName("Yada");
		// results.add(a);
		ExcelImporter eI = new ExcelImporter(results);
		eI.importDataFromFile(new File("res/CS598.xls"));
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
}

class GradeTableModel implements TableModel {
	private StudentClassResults classResults;

	final String[] columnNames = { "S.No.", "Student ID", "Student Name",
			"Total Marks", "Proposed Grade", "Curved Marks",
			"Calculated Grade", "Final Grade" };

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
		} else
			return res.getFinalGrade();
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
