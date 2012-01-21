package org.csrdu.apps.nugradingtable;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class StudentClassResults {
    private Vector<StudentResult> results;
    private String resultsFilename;

    public StudentClassResults() {
        // initilalize
        results = new Vector<StudentResult>();

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

    public DefaultTableModel getRawData() {
        // TODO: generate results from data
        String[] columnNames = { "S.No.", "Student ID", "Student Name",
                "Total Marks", "Proposed Grade", "Curved Marks", "Grade" };
        Object[][] data = {
                { "blah", "Smith", "Snowboarding", new Integer(5),
                        new Boolean(false) },
                { "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
                { "Sue", "Black", "Knitting", new Integer(2),
                        new Boolean(false) },
                { "Jane", "White", "Speed reading", new Integer(20),
                        new Boolean(true) },
                { "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) } };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        return model;
    }
}
