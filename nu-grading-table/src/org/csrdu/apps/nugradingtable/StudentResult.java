package org.csrdu.apps.nugradingtable;

public class StudentResult {
    /** Data submitted by the instructor */
    private String studentName;
    private int sNo;
    private String studentID;
    private int totalMarks;
    private String proposedGrade;

    /** Calculation data */
    private double addCurve;
    private double mulCurve;
    private boolean shouldRound;

    public String getCalculatedGrade() {
        double calcMarks = getCalculatedMarks();
        if (calcMarks >= 90)
            return "A+";
        if (calcMarks >= 86)
            return "A";
        if (calcMarks >= 82)
            return "A-";
        if (calcMarks >= 78)
            return "B+";
        if (calcMarks >= 74)
            return "B";
        if (calcMarks >= 70)
            return "B-";
        if (calcMarks >= 66)
            return "C+";
        if (calcMarks >= 62)
            return "C";
        if (calcMarks >= 58)
            return "C-";
        if (calcMarks >= 54)
            return "D+";
        if (calcMarks >= 50)
            return "D";
        else
            return "F";
    }

    public double getCalculatedMarks() {
        if (shouldRound)
            return Math.round((totalMarks * mulCurve) + addCurve);
        else
            return (totalMarks * mulCurve) + addCurve;
    }

    public double getAddCurve() {
        return addCurve;
    }

    public void setAddCurve(float addCurve) {
        this.addCurve = addCurve;
    }

    public double getMulCurve() {
        return mulCurve;
    }

    public void setMulCurve(double d) {
        this.mulCurve = d;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getProposedGrade() {
        return proposedGrade;
    }

    public void setProposedGrade(String proposedGrade) {
        this.proposedGrade = proposedGrade;
    }

    public boolean isShouldRound() {
        return shouldRound;
    }

    public void setShouldRound(boolean shouldRound) {
        this.shouldRound = shouldRound;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @param studentName
     *            the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return the sNo
     */
    public int getsNo() {
        return sNo;
    }

    /**
     * @param sNo
     *            the sNo to set
     */
    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    /**
     * @return the studentID
     */
    public String getStudentID() {
        return studentID;
    }

    /**
     * @param studentID
     *            the studentID to set
     */
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
