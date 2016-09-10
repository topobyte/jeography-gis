// Copyright 2016 Sebastian Kuerten
//
// This file is part of jeography.
//
// jeography is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// jeography is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with jeography. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.jeography.viewer.geometry.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;

import de.topobyte.jeography.viewer.geometry.manage.filetree.Entry;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;
import de.topobyte.swing.util.DocumentAdapter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class AddDirectoryDialog extends JDialog
{

	private static final long serialVersionUID = -3583598746828016900L;
	private GeometryManager geometryManager;
	private JTextField fieldParent;
	private JTextField fieldName;

	/**
	 * Create the dialog
	 * 
	 * @param geometryManager
	 *            the GeometryManager to manipulate
	 * @param frame
	 *            the parent frame.
	 * @param title
	 *            the title of the dialog.
	 * @param namespace
	 *            the namespace to display initially
	 */
	public AddDirectoryDialog(GeometryManager geometryManager, JFrame frame,
			String title, String namespace)
	{
		super(frame, title);

		this.geometryManager = geometryManager;

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		JPanel content = new JPanel(new GridBagLayout());
		content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		c.weightx = 1.0;
		c.weighty = 0.0;

		JLabel labelParent = new JLabel("parent:");
		JLabel labelName = new JLabel("name:");
		fieldParent = new JTextField(30);
		fieldName = new JTextField(30);
		JButton buttonParent = new JButton("pick");
		JButton buttonAccept = new JButton("Ok");
		JButton buttonCancel = new JButton("Cancel");
		c.insets = new Insets(2, 2, 2, 2);
		c.gridx = 0;
		c.gridy = 0;
		content.add(labelParent, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		content.add(fieldParent, c);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		content.add(labelName, c);
		c.gridx = 1;
		c.gridy = 1;
		content.add(fieldName, c);
		c.gridx = 2;
		c.gridy = 1;
		content.add(buttonParent, c);

		JPanel box = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel boxgrid = new JPanel(new GridLayout(1, 0, 5, 0));
		boxgrid.add(buttonAccept);
		boxgrid.add(buttonCancel);
		box.add(boxgrid, BorderLayout.EAST);
		content.add(box, c);

		JPanel overall = new JPanel(new GridBagLayout());
		setContentPane(overall);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(2, 2, 2, 2);
		overall.add(content, c);
		c.gridy = 1;
		c.insets = new Insets(4, 0, 4, 0);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		overall.add(separator, c);
		c.gridy = 2;
		c.weighty = 0.0;
		c.insets = new Insets(2, 2, 2, 2);
		overall.add(box, c);

		buttonAccept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				pushedOk();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				pushedCancel();
			}
		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e)
			{
				pushedCancel();
			}
		});

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		DocumentAdapter documentAdapter = new DocumentAdapter() {

			@Override
			public void update(DocumentEvent e)
			{
				checkValidityAndUpdateGui();
			}
		};

		KeyAdapter keyAdapter = new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					pushedOk();
				}
			}
		};

		fieldName.getDocument().addDocumentListener(documentAdapter);
		fieldParent.getDocument().addDocumentListener(documentAdapter);

		fieldName.addKeyListener(keyAdapter);
		fieldParent.addKeyListener(keyAdapter);

		fieldParent.setText(namespace);

		checkValidityAndUpdateGui();
	}

	private static final Color green = new Color(0xaa00ff00, true);
	private static final Color red = new Color(0xaaff0000, true);

	private boolean checkValidity()
	{
		return checkName() && checkParent();
	}

	void checkValidityAndUpdateGui()
	{
		fieldName.setBorder(getBorder(checkName()));
		fieldParent.setBorder(getBorder(checkParent()));
	}

	private boolean checkName()
	{
		System.out.println("check name");
		if (!checkParent()) {
			return false;
		}
		String name = fieldName.getText();
		String namespace = fieldParent.getText();

		GeometryTreeModel namespaceModel = geometryManager.getTree().getModel();
		Entry entry = namespaceModel.getByNamespace(namespace);
		if (!(entry instanceof Node)) {
			return false;
		}
		Node node = (Node) entry;

		boolean hasChildNode = node.hasChildNode(name);

		return !hasChildNode && name.length() > 0;
	}

	private boolean checkParent()
	{
		String namespace = fieldParent.getText();
		GeometryTreeModel namespaceModel = geometryManager.getTree().getModel();
		Entry entry = namespaceModel.getByNamespace(namespace);
		System.out.println(entry);
		boolean namespaceValid = entry != null;
		return namespaceValid;
	}

	private Border getBorder(boolean valid)
	{
		Border b1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border b2 = BorderFactory.createLineBorder(valid ? green : red);
		Border b3 = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		return BorderFactory.createCompoundBorder(b3,
				BorderFactory.createCompoundBorder(b2, b1));
	}

	/**
	 * Executed on cancel button press or window-closing event.
	 */
	protected void pushedCancel()
	{
		dispose();
	}

	/**
	 * Executed on Ok button press.
	 */
	protected void pushedOk()
	{
		if (!checkValidity()) {
			return;
		}
		String name = fieldName.getText();
		String namespace = fieldParent.getText();

		GeometryTreeModel namespaceModel = geometryManager.getTree().getModel();
		Entry entry = namespaceModel.getByNamespace(namespace);
		Node node = (Node) entry;

		geometryManager.getTree().getModel().addDirectory(node, name);

		dispose();
	}

}
