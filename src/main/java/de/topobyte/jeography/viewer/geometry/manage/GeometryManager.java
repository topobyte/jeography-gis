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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jeography.viewer.geometry.manage.filetree.Entry;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Leaf;
import de.topobyte.jeography.viewer.geometry.manage.filetree.Node;
import de.topobyte.swing.util.Components;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryManager extends JPanel
{

	private static final long serialVersionUID = 2605280490184092264L;

	final static Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	// TODO: connect to changes of file-tree-model. on update of namespace, find
	// connected rules 'rs'. for each 'r' in 'rs': trigger a callback:
	// 'geometries changed'

	/**
	 * Simple test to show this manager.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GeometryManager manager = new GeometryManager();
		frame.setContentPane(manager);

		frame.setSize(new Dimension(400, 600));
		frame.setVisible(true);

		// manager.displayAddRuleDialog();
		manager.showTab(Tab.RULES);
	}

	private GeometryRules rules = new GeometryRules(this);
	private GeometryStyles styles = new GeometryStyles();
	private GeometryTree tree = new GeometryTree(this);
	private GeometryFileCache cache = new GeometryFileCache();

	private JTabbedPane tabbed;

	/**
	 * Create the GeometryManager
	 */
	public GeometryManager()
	{
		tabbed = new JTabbedPane();

		tabbed.add("rules", rules);
		tabbed.add("styles", styles);
		tabbed.add("files", tree);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(tabbed, c);

	}

	void displayAddRuleDialog()
	{
		JFrame frame = Components.getContainingFrame(this);
		AddRuleDialog dialog = new AddRuleDialog(this, frame, "Add a new rule");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	void displayAddDirectoryDialog()
	{
		String namespace = "";
		TreeSelectionModel selectionModel = tree.getTree().getSelectionModel();
		int min = selectionModel.getMinSelectionRow();
		int max = selectionModel.getMaxSelectionRow();
		if (min == max && min != -1) {
			TreePath path = tree.getTree().getPathForRow(min);
			Object entry = path.getLastPathComponent();
			if (entry instanceof Node) {
				Node node = (Node) entry;
				namespace = node.getNamespace();
				System.out.println(namespace);
			}
		}
		JFrame frame = Components.getContainingFrame(this);
		AddDirectoryDialog dialog = new AddDirectoryDialog(this, frame,
				"Add a new directory", namespace);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	/**
	 * Show the denoted tab.
	 * 
	 * @param tab
	 *            the tab to show.
	 */
	public void showTab(Tab tab)
	{
		switch (tab) {
		case FILES:
			tabbed.setSelectedIndex(2);
			break;
		case RULES:
			tabbed.setSelectedIndex(0);
			break;
		case STYLES:
			tabbed.setSelectedIndex(1);
			break;
		}
	}

	/**
	 * @return the rules used in this manager.
	 */
	public GeometryRules getRules()
	{
		return rules;
	}

	/**
	 * @return the styles used in this manager.
	 */
	public GeometryStyles getStyles()
	{
		return styles;
	}

	/**
	 * @return the tree used in this manager.
	 */
	public GeometryTree getTree()
	{
		return tree;
	}

	/**
	 * @return the geometry cache used by this manager.
	 */
	public GeometryFileCache getGeometryCache()
	{
		return cache;
	}

	/**
	 * Get the StyledGeometry instance for the denoted rule.
	 * 
	 * @param rule
	 *            the rule to retrieve the StyledGeometry for.
	 * @return an instance of StyledGeometry.
	 */
	public StyledGeometry getStyledGeometry(GeometryRule rule)
	{
		GeometryStylesModel styleModel = styles.getModel();

		// final set of geometries to use
		List<GeometryContainer> geometries = new ArrayList<>();

		// style to use for the geometries
		GeometryStyle style = styleModel.getStyleByName(rule.getStyle());

		GeometryTreeModel treeModel = tree.getModel();
		logger.debug("looking for namespace: " + rule.getNamespace());
		Entry entry = treeModel.getByNamespace(rule.getNamespace());
		if (entry == null) {
			return null;
		}
		if (entry instanceof Leaf) {
			logger.debug("leaf: " + entry.toString());
			Leaf leaf = (Leaf) entry;
			File file = leaf.getFile();
			Set<GeometryContainer> tgs = cache.read(file);
			for (GeometryContainer tg : tgs) {
				geometries.add(tg);
			}
		} else {
			logger.debug("node: " + entry.toString());
			Node node = (Node) entry;
			for (Leaf leaf : node.getLeafs()) {
				File file = leaf.getFile();
				Set<GeometryContainer> gcs = cache.read(file);
				// TODO: apply tag filtering
				for (GeometryContainer gc : gcs) {
					geometries.add(gc);
				}
			}
		}

		logger.debug("Style: " + style.toString());
		logger.debug("number of geoetries: " + geometries.size());

		StyledGeometry styledGeometries = new StyledGeometry(geometries, style);
		return styledGeometries;
	}

	/**
	 * @return the collection of styled geometry sets.
	 */
	public Collection<StyledGeometry> getStyledGeometries()
	{
		List<StyledGeometry> styledGeomtrySets = new ArrayList<>();

		GeometryRulesModel ruleModel = rules.getModel();
		List<GeometryRule> ruleList = ruleModel.getRules();

		for (GeometryRule rule : ruleList) {
			StyledGeometry styledGeometry = getStyledGeometry(rule);
			if (styledGeometry == null) {
				continue;
			}
			styledGeomtrySets.add(styledGeometry);
		}

		return styledGeomtrySets;
	}

}
