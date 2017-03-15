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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;

import de.topobyte.jeography.viewer.action.SimpleAction;
import de.topobyte.jeography.viewer.geometry.list.index.IndexDialog;
import de.topobyte.jeography.viewer.geometry.manage.action.ReorderAction;
import de.topobyte.jsi.GenericRTree;
import de.topobyte.jsijts.JsiAndJts;
import de.topobyte.swing.util.ExtensionFileFilter;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryRules extends JPanel
{

	static final Logger logger = LoggerFactory.getLogger(GeometryRules.class);

	private static final long serialVersionUID = -4073767268996038129L;

	JList<GeometryRule> list;
	GeometryRulesModel model;
	GeometryRulesRenderer renderer;

	private JButton buttonSave;
	private JButton buttonOpen;
	private JButton buttonRefresh;
	private JButton buttonAdd;
	private JButton buttonRemove;
	private JButton buttonEdit;
	private JButton buttonMoveUp;
	private JButton buttonMoveDown;

	final GeometryManager geometryManager;

	/**
	 * Create a new GeometryStyles panel
	 * 
	 * @param geometryManager
	 *            the GeometryManager represented by this rule manager.
	 */
	public GeometryRules(GeometryManager geometryManager)
	{
		this.geometryManager = geometryManager;

		model = new GeometryRulesModel();

		// list and scroll pane
		renderer = new GeometryRulesRenderer();
		list = new JList<>(getModel());
		list.setCellRenderer(renderer);
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!(e.getButton() == MouseEvent.BUTTON3)) {
					return;
				}
				int[] selectedIndices = list.getSelectedIndices();
				HashSet<Integer> indices = new HashSet<>();
				for (int i : selectedIndices) {
					indices.add(i);
				}
				int index = list.locationToIndex(e.getPoint());
				if (!indices.contains(index)) {
					return;
				}
				RulePopup menu = new RulePopup(model.getRules().get(index));
				menu.show(list, e.getX(), e.getY());
				menu.setVisible(true);
			}
		});

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(list);

		// buttons
		Box box = new Box(BoxLayout.X_AXIS);

		buttonSave = new JButton(new SaveFileAction());
		buttonOpen = new JButton(new OpenFileAction());
		buttonRefresh = new JButton(new RefreshAction());
		buttonAdd = new JButton(new AddRuleAction());
		buttonRemove = new JButton(new RemoveRuleAction());
		buttonEdit = new JButton(new EditRuleAction());
		buttonMoveUp = new JButton(
				new GeometryRuleReorderAction(ReorderAction.UP));
		buttonMoveDown = new JButton(
				new GeometryRuleReorderAction(ReorderAction.DOWN));
		buttonSave.setText(null);
		buttonOpen.setText(null);
		buttonRefresh.setText(null);
		buttonAdd.setText(null);
		buttonRemove.setText(null);
		buttonEdit.setText(null);
		buttonMoveUp.setText(null);
		buttonMoveDown.setText(null);
		box.add(buttonSave);
		box.add(buttonOpen);
		box.add(buttonRefresh);
		box.add(buttonAdd);
		box.add(buttonRemove);
		box.add(buttonEdit);
		box.add(buttonMoveUp);
		box.add(buttonMoveDown);
		buttonSave.setMargin(new Insets(0, 0, 0, 0));
		buttonOpen.setMargin(new Insets(0, 0, 0, 0));
		buttonRefresh.setMargin(new Insets(0, 0, 0, 0));
		buttonAdd.setMargin(new Insets(0, 0, 0, 0));
		buttonRemove.setMargin(new Insets(0, 0, 0, 0));
		buttonEdit.setMargin(new Insets(0, 0, 0, 0));
		buttonMoveUp.setMargin(new Insets(0, 0, 0, 0));
		buttonMoveDown.setMargin(new Insets(0, 0, 0, 0));

		// overall layout

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridy = 0;
		add(box, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridy = 1;
		add(jsp, c);
	}

	/**
	 * @return the model in use
	 */
	public GeometryRulesModel getModel()
	{
		return model;
	}

	void dialogSave()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		ExtensionFileFilter filter = new ExtensionFileFilter("xml",
				"XML file for rules");
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);

		int response = chooser.showSaveDialog(null);
		boolean accept = response == JFileChooser.APPROVE_OPTION;

		if (accept) {
			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();
			logger.debug("file: " + path);

			File outfile = new File(path);
			if (outfile.exists()) {
				// TODO: check for existence, and ask user for replacement if
				// necessary
				logger.debug("not overwriting currently, please implement");
				return;
			}
			try {
				saveTo(path);
			} catch (Exception e) {
				logger.debug("error writing document: " + e.getMessage());
			}
		}
	}

	void dialogOpen()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		ExtensionFileFilter filter = new ExtensionFileFilter("xml",
				"XML file for rules");
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);

		int response = chooser.showOpenDialog(null);
		boolean accept = response == JFileChooser.APPROVE_OPTION;

		if (accept) {
			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();
			logger.debug("file: " + path);
			try {
				openFrom(path);
			} catch (Exception e) {
				logger.debug("error reading document: " + e.getMessage());
			}
		}
	}

	private void saveTo(String path) throws FileNotFoundException,
			TransformerConfigurationException, SAXException
	{
		logger.debug("writing to: " + path);

		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory
				.newInstance();
		TransformerHandler handler = factory.newTransformerHandler();
		Transformer serializer = handler.getTransformer();
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		StreamResult result = new StreamResult(new FileOutputStream(path));
		handler.setResult(result);

		// start document
		handler.startDocument();
		// serialize rules
		model.serialize(handler);
		// end document
		handler.endDocument();
	}

	private void openFrom(String path)
			throws ParserConfigurationException, SAXException, IOException
	{
		logger.debug("opening from: " + path);

		SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
		FileInputStream inputStream = new FileInputStream(path);

		List<GeometryRule> rules = GeometryRulesModel.deserialize(sax,
				inputStream);
		model.setRules(rules);
	}

	private class SaveFileAction extends SimpleAction
	{

		private static final long serialVersionUID = -1847746877034523351L;

		public SaveFileAction()
		{
			super("save", "save the rules file");
			this.setIconFromResource("res/images/document-save.png");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			dialogSave();
		}

	}

	private class OpenFileAction extends SimpleAction
	{

		private static final long serialVersionUID = 2220631405152029037L;

		public OpenFileAction()
		{
			super("open", "open a rules file");
			this.setIconFromResource("res/images/document-open.png");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			dialogOpen();
		}

	}

	private class RefreshAction extends SimpleAction
	{

		private static final long serialVersionUID = 2220631405152029037L;

		public RefreshAction()
		{
			super("refresh", "refresh the rules");
			this.setIconFromResource("res/images/view-refresh.png");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.refresh();
		}

	}

	private class AddRuleAction extends SimpleAction
	{

		private static final long serialVersionUID = 7360558652884511962L;

		public AddRuleAction()
		{
			super("add", "add an item");
			this.setIconFromResource("res/images/list-add.svg");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			geometryManager.displayAddRuleDialog();
		}

	}

	private class EditRuleAction extends SimpleAction
	{

		private static final long serialVersionUID = -2006912163061124708L;

		public EditRuleAction()
		{
			super("edit", "edit and item");
			this.setIconFromResource("res/images/stock_edit.png");
			list.getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e)
						{
							update();
						}
					});
			update();
		}

		void update()
		{
			ListSelectionModel selectionModel = list.getSelectionModel();
			int min = selectionModel.getMinSelectionIndex();
			int max = selectionModel.getMaxSelectionIndex();
			boolean active = (min == max && min >= 0);
			setEnabled(active);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
		}

	}

	private class RemoveRuleAction extends SimpleAction
	{

		private static final long serialVersionUID = 7360558652884511962L;

		public RemoveRuleAction()
		{
			super("remove", "remove an item");
			this.setIconFromResource("res/images/list-remove.svg");
			list.getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e)
						{
							update();
						}
					});
			update();
		}

		void update()
		{
			ListSelectionModel selectionModel = list.getSelectionModel();
			int min = selectionModel.getMinSelectionIndex();
			boolean active = (min >= 0);
			setEnabled(active);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] indices = list.getSelectedIndices();
			ArrayList<Integer> indexList = new ArrayList<>();
			for (int i : indices) {
				indexList.add(i);
			}
			Collections.reverse(indexList);
			model.removeByIndices(indexList);
		}

	}

	private class GeometryRuleReorderAction extends ReorderAction
	{

		private static final long serialVersionUID = -3836878319938334457L;

		public GeometryRuleReorderAction(int direction)
		{
			super(direction);
			list.getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e)
						{
							update();
						}
					});
			update();
		}

		void update()
		{
			ListSelectionModel selectionModel = list.getSelectionModel();
			int min = selectionModel.getMinSelectionIndex();
			int max = selectionModel.getMaxSelectionIndex();
			boolean single = (min == max && min >= 0);
			if (!single) {
				setEnabled(false);
				return;
			}
			if (direction == UP) {
				setEnabled(min != 0);
			} else {
				setEnabled(max != list.getModel().getSize() - 1);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			ListSelectionModel selectionModel = list.getSelectionModel();
			int min = selectionModel.getMinSelectionIndex();
			int max = selectionModel.getMaxSelectionIndex();
			if (min >= 0 && min == max) {
				if (direction == UP && min > 0) {
					model.swap(min, min - 1);
					list.setSelectedIndex(min - 1);
				} else if (direction == DOWN
						&& min < list.getModel().getSize() - 1) {
					model.swap(min, min + 1);
					list.setSelectedIndex(min + 1);
				}
			}
		}

	}

	private class RulePopup extends JPopupMenu
	{

		private static final long serialVersionUID = 2142319420500594482L;

		public RulePopup(final GeometryRule geometryRule)
		{
			JMenuItem itemEdit = add(new JMenuItem("edit"));
			JMenuItem itemRefresh = add(new JMenuItem("refresh"));
			JMenuItem itemRemove = add(new JMenuItem("remove"));
			JMenuItem itemShowHide = add(new JMenuItem("show / hide"));
			JMenuItem itemIndex = add(new JMenuItem("derive index"));

			// TODO: implement
			itemEdit.setEnabled(false);
			// TODO: implement
			itemShowHide.setEnabled(false);

			itemRefresh.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e)
				{
					model.refresh(geometryRule);
				}
			});

			itemRemove.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e)
				{
					model.removeRule(geometryRule);
				}
			});

			itemIndex.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e)
				{
					StyledGeometry sg = geometryManager
							.getStyledGeometry(geometryRule);
					Collection<GeometryContainer> gcs = sg.getGeometries();
					GenericRTree<Geometry> si = new GenericRTree<>();
					for (GeometryContainer gc : gcs) {
						Geometry g = gc.getGeometry().getGeometry();
						si.add(JsiAndJts.toRectangle(g), g);
					}
					IndexDialog indexDialog = new IndexDialog(si);
					indexDialog.showDialog(GeometryRules.this);
				}
			});
		}
	}

}

class GeometryRulesRenderer extends JPanel
		implements ListCellRenderer<GeometryRule>
{

	private static final long serialVersionUID = 811657575554871786L;

	private DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
	private JPanel panelBox = new JPanel();
	private JLabel labelName = new JLabel();
	private JLabel labelNamespace = new JLabel();
	private JLabel labelStyle = new JLabel();
	private JLabel labelTags = new JLabel();

	public GeometryRulesRenderer()
	{
		// setOpaque(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// c.fill = GridBagConstraints.VERTICAL;
		// c.weightx = 0.0;
		// c.gridx = 0;
		// c.gridheight = 4;
		// add(panelBox, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.weightx = 1.0;
		// c.gridx = 1;
		c.gridy = 0;
		add(labelName, c);
		c.gridy++;
		add(labelNamespace, c);
		c.gridy++;
		add(labelStyle, c);
		c.gridy++;
		add(labelTags, c);
		Border empty = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Border line = BorderFactory
				.createLineBorder(new Color(0x33000000, true), 2);
		Border b1 = BorderFactory.createCompoundBorder(empty, line);
		Border b2 = BorderFactory.createCompoundBorder(b1, empty);
		setBorder(b2);

		JCheckBox box = new JCheckBox();
		box.setOpaque(false);
		panelBox.add(box);
		panelBox.setOpaque(false);
		panelBox.setPreferredSize(new Dimension(20, -1));
		panelBox.setMaximumSize(new Dimension(20, 20));
		panelBox.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory
						.createMatteBorder(2, 2, 2, 2, new Color(0x000000))));
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends GeometryRule> list, GeometryRule value, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		Component c = dlcr.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);

		setForeground(c.getForeground());
		setBackground(c.getBackground());

		GeometryRule rule = value;
		// setText(rule.namespace + " <-> " + rule.style);
		labelName.setText(rule.name);
		labelNamespace.setText(rule.namespace);
		labelStyle.setText(rule.style);
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < rule.filters.size(); i++) {
			GeometryRuleTag t = rule.filters.get(i);
			strb.append(t.key + ":" + t.value);
			if (i < rule.filters.size() - 1) {
				strb.append(", ");
			}
		}
		labelTags.setText(strb.toString());

		return this;
	}

}
