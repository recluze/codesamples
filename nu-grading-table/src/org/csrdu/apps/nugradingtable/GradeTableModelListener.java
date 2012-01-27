package org.csrdu.apps.nugradingtable;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class GradeTableModelListener implements TableModelListener {
	@SuppressWarnings("unused")
    private StudentClassResults results;
	@SuppressWarnings("unused")
    private JTable table;

	public GradeTableModelListener(StudentClassResults results, JTable table) {
		this.results = results;
		this.table = table;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int firstRow = e.getFirstRow();
		int lastRow = e.getLastRow();
		int mColIndex = e.getColumn();

		switch (e.getType()) {
		case TableModelEvent.INSERT:
			// The inserted rows are in the range [firstRow, lastRow]
			for (int r = firstRow; r <= lastRow; r++) {
				// Row r was inserted
			}
			break;
		case TableModelEvent.UPDATE:
			if (firstRow == TableModelEvent.HEADER_ROW) {
				if (mColIndex == TableModelEvent.ALL_COLUMNS) {
					// A column was added
				} else {
					// Column mColIndex in header changed
				}
			} else {
				// The rows in the range [firstRow, lastRow] changed
				for (int r = firstRow; r <= lastRow; r++) {
					// Row r was changed

					if (mColIndex == TableModelEvent.ALL_COLUMNS) {
						// All columns in the range of rows have changed
					} else {
						// Column mColIndex changed
					}
				}
			}
			break;
		case TableModelEvent.DELETE:
			// The rows in the range [firstRow, lastRow] changed
			for (int r = firstRow; r <= lastRow; r++) {
				// Row r was deleted
			}
			break;
		}
	}

}
