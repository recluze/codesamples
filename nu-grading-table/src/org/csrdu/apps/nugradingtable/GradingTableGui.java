package org.csrdu.apps.nugradingtable;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class GradingTableGui extends JFrame {
	JTable gradeTable;
	JPanel optionsPane;
	private JLabel label;
	private JTextField txtCourseName;

	public void addComponentsToPane() {
		JButton button;
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		// start setting options Pane
		optionsPane = new JPanel();
		optionsPane.setSize(getWidth() / 2, 100);
		optionsPane.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();

		label = new JLabel("Course Code");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.gridy = 0;
		c2.weightx = 0.25;
		optionsPane.add(label, c2);

		txtCourseName = new JTextField("Course Name");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 1;
		c2.gridy = 0;
		c2.weightx = 0.75;
		optionsPane.add(txtCourseName, c2);

		label = new JLabel("Course Name");
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.gridy = 1;
		c2.weightx = 0.25;
		optionsPane.add(label, c2);

		// Continue with JTable
		c.weightx = 0.25;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(optionsPane, c);
		label = new JLabel("");
		c.weightx = 0.75;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		this.add(label, c);

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
