package org.csrdu.apps.nugradingtable.tests;

import static org.junit.Assert.*;

import org.csrdu.apps.nugradingtable.StudentResult;
import org.junit.Test;

public class StudentResultTest {

    @Test
    public void testGetCalculatedGrade() {
        StudentResult res = new StudentResult();
        res.setAddCurve(1);
        res.setMulCurve(1.1);
        res.setShouldRound(true);
        res.setTotalMarks(70);

        assertEquals("B+", res.getCalculatedGrade());

        // TODO: Add more tests
    }

    @Test
    public void testGetCalculatedMarks() {
        StudentResult res = new StudentResult();
        res.setAddCurve(1);
        res.setMulCurve(1.1);
        res.setShouldRound(true);
        res.setTotalMarks(70);

        assertEquals(78, res.getCalculatedMarks(), 1);
        // TODO: Add more tests
    }

}
