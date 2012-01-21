package org.csrdu.apps.nugradingtable;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.internal.ws.api.server.Container;

public class GradingTableGui extends JFrame {
	JTable gradeTable;

	public void addComponentsToPane() {
		JButton button;
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		button = new JButton("Button 1");
		c.weightx = 0.5;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(button, c);

		button = new JButton("Button 2");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		this.add(button, c);

		button = new JButton("Button 3");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		this.add(button, c);

		gradeTable = new JTable();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40; // make this component tall
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		gradeTable.setFillsViewportHeight(true);
		gradeTable.setAutoCreateRowSorter(true); // allow sorting
		JScrollPane scroller = new JScrollPane(gradeTable);
		this.add(scroller, c);

		button = new JButton("5");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		c.insets = new Insets(10, 0, 0, 0); // top padding
		c.gridx = 1; // aligned with button 2
		c.gridwidth = 2; // 2 columns wide
		c.gridy = 2; // third row
		this.add(button, c);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void populateGradeTable() {
		StudentClassResults classResults = new StudentClassResults();
		// set model for data retrieval 
		gradeTable.setModel(classResults.getRawData());
		// set change listener 
		gradeTable.getModel().addTableModelListener(
				new GradeTableModelListener(classResults, gradeTable));
	}

	public static void main(String args[]) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Don't care about this exception
		}
		GradingTableGui gtGui = new GradingTableGui();
		gtGui.addComponentsToPane();
		gtGui.setSize(800, 600);
		gtGui.populateGradeTable();
		gtGui.setVisible(true);
	}
}
