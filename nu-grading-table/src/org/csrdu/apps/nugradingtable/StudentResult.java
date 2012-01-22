package org.csrdu.apps.nugradingtable;

public class StudentResult {
    /** Data submitted by the instructor */
    private String studentName = "";
    private int sNo = 0;
    private String studentID = "";
    private double totalMarks = 0;
    private String proposedGrade = "";
    private String finalGrade = "B";
    private String section = "A"; 

    /** Calculation data */
    private double addCurve = 0.0;
    private double mulCurve = 1;
    private boolean shouldRound = false;

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

    public void setAddCurve(double addCurve) {
        this.addCurve = addCurve;
    }

    public double getMulCurve() {
        return mulCurve;
    }

    public void setMulCurve(double d) {
        this.mulCurve = d;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double d) {
        this.totalMarks = d;
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

	/**
	 * @return the finalGrade
	 */
	public String getFinalGrade() {
		return finalGrade;
	}

	/**
	 * @param finalGrade the finalGrade to set
	 */
	public void setFinalGrade(String finalGrade) {
		this.finalGrade = finalGrade;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}
}
