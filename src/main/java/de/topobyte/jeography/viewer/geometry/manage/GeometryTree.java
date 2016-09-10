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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
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

import de.topobyte.jeography.geometry.GeoObject;
import de.topobyte.jeography.viewer.action.SimpleAction;
import de.topobyte.jeography.viewer.geometry.list.dnd.FileTransferHandler;
import de.topobyte.jeography.viewer.geometry.list.dnd.GeometrySourceTransferHandler;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Entry;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Leaf;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;
import de.topobyte.jeography.viewer.tools.preview.GeometryPreview;
import de.topobyte.swing.util.ExtensionFileFilter;
import de.topobyte.swing.util.dnd.DestinationSourceTransferHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryTree extends JPanel
{

	final static Logger logger = LoggerFactory.getLogger(GeometryTree.class);

	private static final long serialVersionUID = -3418352956823109052L;

	private JTree tree;
	// private JXTreeTable tree;
	private GeometryTreeModel treeModel;

	final GeometryManager geometryManager;

	private JButton buttonSave;
	private JButton buttonOpen;
	private JButton buttonAdd;
	private JButton buttonRemove;
	private JButton buttonEdit;

	/**
	 * Public constructor.
	 * 
	 * @param geometryManager
	 *            the geometry manager this tree is attached to.
	 */
	public GeometryTree(GeometryManager geometryManager)
	{
		this.geometryManager = geometryManager;

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		treeModel = new GeometryTreeModel();
		// tree = new JXTreeTable(treeModel;
		tree = new JTree(treeModel);

		Box box = new Box(BoxLayout.X_AXIS);

		buttonSave = new JButton(new SaveFileAction());
		buttonOpen = new JButton(new OpenFileAction());
		buttonAdd = new JButton(new AddFileAction());
		buttonRemove = new JButton(new RemoveFileAction());
		buttonEdit = new JButton(new EditFileAction());
		box.add(buttonSave);
		box.add(buttonOpen);
		box.add(buttonAdd);
		box.add(buttonRemove);
		box.add(buttonEdit);
		buttonSave.setMargin(new Insets(0, 0, 0, 0));
		buttonOpen.setMargin(new Insets(0, 0, 0, 0));
		buttonAdd.setMargin(new Insets(0, 0, 0, 0));
		buttonRemove.setMargin(new Insets(0, 0, 0, 0));
		buttonEdit.setMargin(new Insets(0, 0, 0, 0));
		buttonSave.setText(null);
		buttonOpen.setText(null);
		buttonAdd.setText(null);
		buttonRemove.setText(null);
		buttonEdit.setText(null);

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(tree);
		// list.setFont(new Font("Arial", 0, 20));
		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.setDropMode(DropMode.ON);
		// tree.setDropMode(DropMode.ON_OR_INSERT_ROWS);
		tree.setRootVisible(false);

		TheTransferHandler myTransferHandler = new TheTransferHandler();
		tree.setTransferHandler(myTransferHandler);

		tree.setDragEnabled(true);

		GeometryCellRenderer renderer = new GeometryCellRenderer();
		// DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		tree.setCellRenderer(renderer);

		// tree.setCellEditor(new GeometryCellEditor());
		// tree.setEditable(true);

		// TreeUI ui = new GeometryTreeUI();
		// tree.setUI(ui);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridy = 0;
		add(box, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridy = 1;
		add(jsp, c);

		// new DropTarget(list, this);
		// new FileDrop(System.out, list, this);

		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					TreePath path = getTree().getPathForLocation(e.getX(),
							e.getY());
					if (path == null) {
						return;
					}
					Entry entry = (Entry) path.getLastPathComponent();
					if (entry instanceof Leaf) {
						Leaf leaf = (Leaf) entry;
						File file = leaf.getFile();
						logger.debug(file.toString());
						showViewerWithFile(file);
					}
				}
			}
		});
	}

	void showViewerWithFile(File file)
	{
		Set<GeometryContainer> tgs = geometryManager.getGeometryCache().read(
				file);

		GeometryPreview preview = new GeometryPreview();
		preview.showViewerWithFile(this, tgs, file.getName());
	}

	/**
	 * @return the tree
	 */
	public JTree getTree()
	{
		return tree;
	}

	/**
	 * @return the list model in use.
	 */
	public GeometryTreeModel getModel()
	{
		return treeModel;
	}

	private class TheTransferHandler extends DestinationSourceTransferHandler
	{

		private static final long serialVersionUID = -1833778393371284859L;

		public TheTransferHandler()
		{
			TheGeometrySourceHandler source = new TheGeometrySourceHandler();
			setSourceHandler(source);
			TheFileDestinationHandler destination = new TheFileDestinationHandler();
			setDestinationHandler(destination);
		}

		class TheFileDestinationHandler extends FileTransferHandler
		{

			private static final long serialVersionUID = -963849709255476348L;

			@Override
			public void handleFiles(List<File> handleFiles, TransferSupport ts)
			{
				JTree.DropLocation dropLocation = (JTree.DropLocation) ts
						.getDropLocation();
				TreePath path = dropLocation.getPath();
				if (path != null) {
					logger.debug(dropLocation.getPath().toString());
					for (int i = 0; i < path.getPathCount(); i++) {
						Object component = path.getPathComponent(i);
						logger.debug(component.getClass() + ": "
								+ component.toString());
					}
				}
				getModel().addFiles(handleFiles, path);
			}

		}

		class TheGeometrySourceHandler extends GeometrySourceTransferHandler
		{

			private static final long serialVersionUID = 7103315511699333737L;

			@Override
			public int getSourceActions(JComponent c)
			{
				return COPY;
			}

			@Override
			public Collection<Geometry> getGeometries()
			{
				List<Geometry> geometries = new ArrayList<>();
				TreePath[] paths = getTree().getSelectionPaths();
				for (TreePath path : paths) {
					Entry last = (Entry) path.getLastPathComponent();
					if (last instanceof Leaf) {
						Leaf leaf = (Leaf) path.getLastPathComponent();
						File file = leaf.getFile();
						Set<GeometryContainer> tgs = geometryManager
								.getGeometryCache().read(file);
						for (GeometryContainer container : tgs) {
							GeoObject tg = container.getGeometry();
							geometries.add(tg.getGeometry());
						}
					} else if (last instanceof Node) {
						// TODO: take all children...
					}
				}
				return geometries;
			}
		}

	}

	private class SaveFileAction extends SimpleAction
	{

		private static final long serialVersionUID = -1847746877034523351L;

		public SaveFileAction()
		{
			super("save", "save the file tree");
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
			super("open", "open a file tree");
			this.setIconFromResource("res/images/document-open.png");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			dialogOpen();
		}

	}

	private class AddFileAction extends SimpleAction
	{

		private static final long serialVersionUID = 7360558652884511962L;

		public AddFileAction()
		{
			super("add", "add an item");
			this.setIconFromResource("res/images/list-add.svg");
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			geometryManager.displayAddDirectoryDialog();
		}

	}

	private class EditFileAction extends SimpleAction
	{

		private static final long serialVersionUID = -2006912163061124708L;

		public EditFileAction()
		{
			super("edit", "edit and item");
			this.setIconFromResource("res/images/stock_edit.png");

			getTree().getSelectionModel().addTreeSelectionListener(
					new TreeSelectionListener() {

						@Override
						public void valueChanged(TreeSelectionEvent e)
						{
							update();
						}
					});
			update();
		}

		void update()
		{
			TreeSelectionModel selectionModel = getTree().getSelectionModel();
			int min = selectionModel.getMinSelectionRow();
			int max = selectionModel.getMaxSelectionRow();
			boolean valid = min != -1 && min == max;
			setEnabled(valid);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO implement action
		}

	}

	private class RemoveFileAction extends SimpleAction
	{

		private static final long serialVersionUID = 7340500539867513961L;

		public RemoveFileAction()
		{
			super("remove", "remove an item");
			this.setIconFromResource("res/images/list-remove.svg");

			getTree().getSelectionModel().addTreeSelectionListener(
					new TreeSelectionListener() {

						@Override
						public void valueChanged(TreeSelectionEvent e)
						{
							update();
						}
					});
			update();
		}

		void update()
		{
			TreeSelectionModel selectionModel = getTree().getSelectionModel();
			int min = selectionModel.getMinSelectionRow();
			boolean valid = min != -1;
			setEnabled(valid);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			TreeSelectionModel selectionModel = getTree().getSelectionModel();
			TreePath[] selectionPaths = selectionModel.getSelectionPaths();
			for (TreePath path : selectionPaths) {
				getModel().removePath(path);
			}
		}

	}

	void dialogSave()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		ExtensionFileFilter filter = new ExtensionFileFilter("xml",
				"XML file for file trees");
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
				"XML file for file trees");
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
		// serialize tree model
		treeModel.serialize(handler);
		// end document
		handler.endDocument();
	}

	private void openFrom(String path) throws ParserConfigurationException,
			SAXException, IOException
	{
		logger.debug("opening from: " + path);

		SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
		FileInputStream inputStream = new FileInputStream(path);

		GeometryTreeModel model = GeometryTreeModel.deserialize(sax,
				inputStream);
		this.treeModel = model;
		tree.setModel(model);
	}

}
