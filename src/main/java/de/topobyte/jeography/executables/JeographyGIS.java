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

package de.topobyte.jeography.executables;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import de.topboyte.interactiveview.ZoomChangedListener;
import de.topobyte.adt.geo.Coordinate;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.viewer.Constants;
import de.topobyte.jeography.viewer.MouseMode;
import de.topobyte.jeography.viewer.action.AboutAction;
import de.topobyte.jeography.viewer.action.ConfigurationAction;
import de.topobyte.jeography.viewer.action.CrosshairAction;
import de.topobyte.jeography.viewer.action.DialogAction;
import de.topobyte.jeography.viewer.action.FullscreenAction;
import de.topobyte.jeography.viewer.action.GeometryIndexAction;
import de.topobyte.jeography.viewer.action.GeometryInfoAction;
import de.topobyte.jeography.viewer.action.GeometryListAction;
import de.topobyte.jeography.viewer.action.GeometryManagerAction;
import de.topobyte.jeography.viewer.action.GeometrySelectionAction;
import de.topobyte.jeography.viewer.action.GotoAction;
import de.topobyte.jeography.viewer.action.GridAction;
import de.topobyte.jeography.viewer.action.ManualAction;
import de.topobyte.jeography.viewer.action.MapWindowPanelAction;
import de.topobyte.jeography.viewer.action.MeasurePanelAction;
import de.topobyte.jeography.viewer.action.MouseModeAction;
import de.topobyte.jeography.viewer.action.NetworkStateAction;
import de.topobyte.jeography.viewer.action.OperationAction;
import de.topobyte.jeography.viewer.action.OverlayAction;
import de.topobyte.jeography.viewer.action.OverlayTileConfigAction;
import de.topobyte.jeography.viewer.action.PolygonalSelectionSnapAction;
import de.topobyte.jeography.viewer.action.QuitAction;
import de.topobyte.jeography.viewer.action.SelectionPolyPanelAction;
import de.topobyte.jeography.viewer.action.SelectionRectPanelAction;
import de.topobyte.jeography.viewer.action.SelectionSnapAction;
import de.topobyte.jeography.viewer.action.ShowStatusbarAction;
import de.topobyte.jeography.viewer.action.ShowToolbarAction;
import de.topobyte.jeography.viewer.action.TileConfigAction;
import de.topobyte.jeography.viewer.action.TileNumberAction;
import de.topobyte.jeography.viewer.action.ZoomAction;
import de.topobyte.jeography.viewer.bookmarks.Bookmarks;
import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.OverlayDragGestureListener;
import de.topobyte.jeography.viewer.geometry.OverlayManager;
import de.topobyte.jeography.viewer.geometry.list.operation.OperationList;
import de.topobyte.jeography.viewer.geometry.list.operation.Operations;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingBufferUnionList;
import de.topobyte.jeography.viewer.geometry.list.operation.ShowingTranslateList;
import de.topobyte.jeography.viewer.geometry.list.operation.transform.ShowingTransformList;
import de.topobyte.jeography.viewer.geometry.manage.EventJDialog;
import de.topobyte.jeography.viewer.geometry.manage.GeometryManager;
import de.topobyte.jeography.viewer.selection.pane.PolyPane;
import de.topobyte.jeography.viewer.selection.pane.RectPane;
import de.topobyte.jeography.viewer.selection.polygonal.PolySelectionAdapter;
import de.topobyte.jeography.viewer.selection.rectangular.IntSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.jeography.viewer.util.ActionUtil;
import de.topobyte.jeography.viewer.util.EmptyIcon;
import de.topobyte.jeography.viewer.windowpane.MapWindowPane;
import de.topobyte.jeography.viewer.zoom.ZoomControl;
import de.topobyte.swing.util.Components;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class JeographyGIS extends JPanel
{

	private static final long serialVersionUID = 8968318299129493776L;

	static final Logger logger = LoggerFactory.getLogger(JeographyGIS.class);

	private static final String HELP_MESSAGE = JeographyGIS.class
			.getSimpleName() + " [args]";

	public static void main(String[] args)
	{
		// @formatter:off
		Options options = new Options();
		OptionHelper.addL(options, "width", true, false, "pixels",
				"Window width");
		OptionHelper.addL(options, "height", true, false, "pixels",
				"Window height");
		OptionHelper.addL(options, "network", true, false, "yes/no",
				"start online / offline?");
		OptionHelper.addL(options, "grid", true, false, "yes/no", "show grid?");
		OptionHelper.addL(options, "tile-numbers", true, false, "yes/no",
				"show tile numbers?");
		OptionHelper.addL(options, "crosshair", true, false, "yes/no",
				"show crosshair?");
		OptionHelper.addL(options, "overlay", true, false, "yes/no",
				"show overlay?");
		OptionHelper.addL(options, "config", true, false, "file",
				"configuration to use");
		OptionHelper.addL(options, "lon", true, false, "double",
				"longitude to show");
		OptionHelper.addL(options, "lat", true, false, "double",
				"latitude to show");
		OptionHelper.addL(options, "zoom", true, false, "int",
				"zoom level to use");
		OptionHelper.addL(options, "db", true, false, "file",
				"file to use for database queries");
		OptionHelper.addL(options, "tiles", true, false, "index",
				"the ordinal index of the tile configuration to use");
		// @formatter:on

		CommandLineParser clp = new DefaultParser();

		CommandLine line = null;
		try {
			line = clp.parse(options, args);
		} catch (ParseException e) {
			System.err
					.println("Parsing command line failed: " + e.getMessage());
			new HelpFormatter().printHelp(HELP_MESSAGE, options);
			System.exit(1);
		}

		if (line == null)
			return;

		String configFile = null;
		Configuration configuration = Configuration
				.createDefaultConfiguration();

		if (line.hasOption("config")) {
			configFile = line.getOptionValue("config");
			try {
				configuration = ConfigReader.read(configFile);
			} catch (Exception e) {
				logger.warn(
						"unable to read configuration specified at command-line",
						e);
				logger.warn("using default configuration");
			}
		} else {
			configFile = ConfigurationHelper.getUserConfigurationFilePath();
			logger.debug("default user config file: " + configFile);
			try {
				configuration = ConfigReader.read(configFile);
			} catch (FileNotFoundException e) {
				logger.warn("no configuration file found, using default configuration");
			} catch (Exception e) {
				logger.warn("unable to read configuration at user home", e);
				logger.warn("using default configuration");
			}
		}

		int width = configuration.getWidth(), height = configuration
				.getHeight();
		String fileDb = configuration.getFileDb();
		boolean isOnline = configuration.isOnline();
		boolean showCrosshair = configuration.isShowCrosshair();
		boolean showGrid = configuration.isShowGrid();
		boolean showTileNumbers = configuration.isShowTileNumbers();
		boolean showOverlay = configuration.isShowOverlay();
		final boolean showGeometryManager = configuration
				.isShowGeometryManager();
		final boolean showSelectionRectDialog = configuration
				.isShowSelectionRectDialog();
		final boolean showSelectionPolyDialog = configuration
				.isShowSelectionPolyDialog();
		final boolean showMapWindowDialog = configuration
				.isShowMapWindowDialog();
		double lon = configuration.getLon();
		double lat = configuration.getLat();
		int zoom = configuration.getZoom();
		int tilesIndex = 0;

		if (line.hasOption("width")) {
			int w = Integer.valueOf(line.getOptionValue("width"));
			if (w != 0)
				width = w;
		}
		if (line.hasOption("height")) {
			int h = Integer.valueOf(line.getOptionValue("height"));
			if (h != 0)
				height = h;
		}
		if (line.hasOption("network")) {
			isOnline = line.getOptionValue("network").equals("yes");
		}
		if (line.hasOption("crosshair")) {
			showCrosshair = line.getOptionValue("crosshair").equals("yes");
		}
		if (line.hasOption("overlay")) {
			showOverlay = line.getOptionValue("overlay").equals("yes");
		}
		if (line.hasOption("grid")) {
			showGrid = line.getOptionValue("grid").equals("yes");
		}
		if (line.hasOption("tile_numbers")) {
			showTileNumbers = line.getOptionValue("tile_numbers").equals("yes");
		}
		if (line.hasOption("db")) {
			fileDb = line.getOptionValue("db");
		}

		if (line.hasOption("lon")) {
			String value = line.getOptionValue("lon");
			lon = Double.parseDouble(value);
		}
		if (line.hasOption("lat")) {
			String value = line.getOptionValue("lat");
			lat = Double.parseDouble(value);
		}
		if (line.hasOption("zoom")) {
			String value = line.getOptionValue("zoom");
			zoom = Integer.parseInt(value);
		}
		if (line.hasOption("tiles")) {
			String value = line.getOptionValue("tiles");
			tilesIndex = Integer.parseInt(value);
		}

		String lookAndFeel = configuration.getLookAndFeel();
		if (lookAndFeel != null) {
			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (Exception e) {
				logger.error("error while setting look and feel '"
						+ lookAndFeel + "': " + e.getClass().getSimpleName()
						+ ", message: " + e.getMessage());
			}
		}

		final JeographyGIS gis = new JeographyGIS(configFile, configuration,
				tilesIndex, fileDb, isOnline, showGrid, showTileNumbers,
				showCrosshair, showOverlay, zoom, lon, lat);

		final int fWidth = width;
		final int fHeight = height;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				gis.create(fWidth, fHeight, showGeometryManager,
						showSelectionRectDialog, showSelectionPolyDialog,
						showMapWindowDialog);
			}
		});
	}

	private String configFile;
	private Configuration configuration;

	private Viewer viewer;
	private JPanel statusBar;
	private JLabel statusLabel;
	private JToolBar toolbar;
	private ZoomControl zoomControl;

	private CControl control;
	private CGrid grid;

	private SelectionAdapter selectionAdapter;
	private PolySelectionAdapter polySelectionAdapter;
	// private GeometryAdapter geometryAdapter;

	// private ImageManagerGeometry geometryOverlay;
	// private GeometryList geometryList;
	private GeometryManager geometryManager;
	private EventJDialog geometryManagerDialog = null;
	private OverlayManager overlayManager;

	private DefaultSingleCDockable selectionRectDockable = null;
	private DefaultSingleCDockable selectionPolyDockable = null;

	private DefaultSingleCDockable mapWindowDockable = null;

	private DefaultSingleCDockable bookmarksDockable = null;

	private boolean showGeometryInfo = false;

	public void create(int width, int height, boolean showGeometryManager,
			boolean showSelectionRectDialog, boolean showSelectionPolyDialog,
			boolean showMapWindowDialog)
	{
		final JFrame frame = new JFrame("Jeography GIS");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// layout

		DefaultSingleCDockable dockableViewer = new DefaultSingleCDockable(
				"Map", "Map", viewer);
		dockableViewer.setExternalizable(false);
		dockableViewer.setMinimizable(false);

		control = new CControl(frame);

		setLayout(new BorderLayout());
		frame.setContentPane(this);

		add(toolbar, BorderLayout.NORTH);
		add(control.getContentArea(), BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		grid = new CGrid(control);
		grid.add(0, 0, 1, 10, dockableViewer);

		// layout done

		frame.setSize(new Dimension(width, height));
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e)
			{
				showReallyExitDialog(frame);
			}

		});

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		int mainFrameY = (int) frame.getLocation().getY();

		setupGeometryList();
		geometryManagerDialog.setLocation(width, mainFrameY);
		geometryManagerDialog.setVisible(showGeometryManager);

		setupSelectionRectDialog(showSelectionRectDialog);
		setupSelectionPolyDialog(showSelectionPolyDialog);
		setupMapWindowDialog(showMapWindowDialog);
		setupBookmarksDialog(true);

		control.getContentArea().deploy(grid);

		zoomControl = new ZoomControl();
		zoomControl.addZoomChangedListener(new ZoomChangedListener() {

			@Override
			public void zoomChanged()
			{
				double zoom = zoomControl.getValue();
				int tileSize = (int) Math.round(zoom * 256);
				viewer.setTileSize(tileSize);
			}
		});

		statusBar.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		statusBar.add(statusLabel, c);
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		statusBar.add(zoomControl, c);

		setupActions(frame, this, toolbar, menuBar,
				configuration.getTileConfigs(),
				configuration.getTileConfigsOverlay());
	}

	/**
	 * @return the viewer.
	 */
	public Viewer getViewer()
	{
		return viewer;
	}

	/**
	 * @return the toolbar of the main ui.
	 */
	public JToolBar getToolbar()
	{
		return toolbar;
	}

	/**
	 * Create a Jeography GIS instance.
	 * 
	 * @param configFile
	 *            the path to the configuration file in use.
	 * @param configuration
	 *            the configuration to use.
	 * @param fileDB
	 *            a String denoting a filename to a SQLite database for address
	 *            searching.
	 * @param isOnline
	 *            whether the applications shall use internet connection.
	 * @param showGrid
	 *            whether to show tile borders as grid.
	 * @param showTileNumbers
	 *            whether to show tile numbers.
	 * @param showCrosshair
	 *            whether to show a crosshair in the middle of the viewport.
	 * @param showOverlay
	 *            whether to show the overlay
	 */
	public JeographyGIS(String configFile, Configuration configuration,
			int tilesIndex, String fileDB, boolean isOnline, boolean showGrid,
			boolean showTileNumbers, boolean showCrosshair, boolean showOverlay)
	{
		this(configFile, configuration, tilesIndex, fileDB, isOnline, showGrid,
				showTileNumbers, showCrosshair, showOverlay,
				Constants.DEFAULT_ZOOM, Constants.DEFAULT_LON,
				Constants.DEFAULT_LAT);
	}

	/**
	 * Create a Jeography GIS instance.
	 * 
	 * @param configFile
	 *            the path to the configuration file in use.
	 * @param configuration
	 *            the configuration to use.
	 * @param fileDB
	 *            a String denoting a filename to a SQLite database for address
	 *            searching.
	 * @param isOnline
	 *            whether the applications shall use internet connection.
	 * @param showGrid
	 *            whether to show tile borders as grid.
	 * @param showTileNumbers
	 *            whether to show tile numbers.
	 * @param showCrosshair
	 *            whether to show a crosshair in the middle of the viewport.
	 * @param showOverlay
	 *            whether to show the overlay
	 * @param zoom
	 *            startup zoom
	 * @param lon
	 *            startup longitude
	 * @param lat
	 *            startup latitude
	 */
	public JeographyGIS(String configFile, Configuration configuration,
			int tilesIndex, String fileDB, boolean isOnline, boolean showGrid,
			boolean showTileNumbers, boolean showCrosshair,
			boolean showOverlay, int zoom, double lon, double lat)
	{
		this.configFile = configFile;
		this.configuration = configuration;

		// here's the GUI
		TileConfig configOverlay = null;
		if (configuration.getTileConfigsOverlay().size() > 0) {
			configOverlay = configuration.getTileConfigsOverlay().get(0);
		}
		viewer = new Viewer(configuration.getTileConfigs().get(tilesIndex),
				configOverlay, zoom, lon, lat);
		viewer.setNetworkState(isOnline);
		viewer.setDrawBorder(showGrid);
		viewer.setDrawTileNumbers(showTileNumbers);
		viewer.setDrawCrosshair(showCrosshair);
		viewer.setMouseActive(true);
		viewer.setDrawOverlay(showOverlay);

		OverlayDragGestureListener dragGestureListener = new OverlayDragGestureListener(
				this);
		viewer.setDragGestureListener(dragGestureListener);

		geometryManager = new GeometryManager();
		overlayManager = new OverlayManager(geometryManager, this);
		geometryManager.getRules().getModel().addChangeListener(overlayManager);

		selectionAdapter = new SelectionAdapter(viewer);
		selectionAdapter.setMouseActive(false);

		polySelectionAdapter = new PolySelectionAdapter(viewer);
		polySelectionAdapter.setMouseActive(false);

		statusBar = new JPanel();
		statusLabel = new JLabel();
		toolbar = new JToolBar();

		viewer.getMapWindow().addChangeListener(new MapWindowChangeListener() {

			@Override
			public void changed()
			{
				updateStatusBar();
			}
		});

		MouseAdapter mouseAdapter = new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e)
			{
				updateStatusBar();
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				updateStatusBar();
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				updateStatusBar();
			}
		};

		viewer.addMouseListener(mouseAdapter);
		viewer.addMouseMotionListener(mouseAdapter);
		viewer.addMouseWheelListener(mouseAdapter);

		toolbar.setFloatable(false);
	}

	/**
	 * @return the config file the configuration comes from or null
	 */
	public String getConfigFile()
	{
		return configFile;
	}

	/**
	 * @return the configuration of this instance.
	 */
	public Configuration getConfiguration()
	{
		return this.configuration;
	}

	private MouseMode mouseMode = MouseMode.NAVIGATE;

	/**
	 * Sets the mouse mode to the specified value.
	 * 
	 * @param mode
	 *            the mouse mode to use now.
	 */
	public void setMouseMode(MouseMode mode)
	{
		MouseMode oldMode = mouseMode;
		mouseMode = mode;

		if (mode != MouseMode.NAVIGATE) {
			viewer.setMouseActive(false);
		}
		if (mode != MouseMode.POLYSELECT) {
			polySelectionAdapter.setMouseActive(false);
		}
		if (mode != MouseMode.SELECT) {
			selectionAdapter.setMouseActive(false);
		}
		if (mode != MouseMode.DRAG) {
			viewer.setDragging(false);
		}
		switch (mode) {
		case NAVIGATE:
			viewer.setMouseActive(true);
			break;
		case SELECT:
			selectionAdapter.setMouseActive(true);
			break;
		case POLYSELECT:
			polySelectionAdapter.setMouseActive(true);
			break;
		case DRAG:
			viewer.setDragging(true);
		}
		firePropertyChange("mouseMode", oldMode, mode);
	}

	/**
	 * @return the current mouse mode.
	 */
	public MouseMode getMouseMode()
	{
		return mouseMode;
	}

	void updateStatusBar()
	{
		Coordinate coordinate = viewer.getMouseGeoPosition();
		IntSelection selection = selectionAdapter.getSelection();
		String s = selection == null ? "none" : selection.toString();
		if (coordinate == null) {
			statusLabel.setText(String.format(
					"center: %f:%f mouse: n/a zoom: %d selection: %s", viewer
							.getMapWindow().getCenterLon(), viewer
							.getMapWindow().getCenterLat(), viewer
							.getZoomLevel(), s));
		} else {
			statusLabel.setText(String.format(
					"center: %f:%f mouse: %f:%f zoom: %d selection: %s", viewer
							.getMapWindow().getCenterLon(), viewer
							.getMapWindow().getCenterLat(), coordinate
							.getLongitude(), coordinate.getLatitude(), viewer
							.getZoomLevel(), s));
		}
	}

	static void setupActions(JFrame frame, final JeographyGIS gis,
			JToolBar toolbar, JMenuBar menuBar, List<TileConfig> tileConfigs,
			List<TileConfig> overlayConfigs)
	{
		final Viewer viewer = gis.getViewer();

		MouseModeAction mma1 = new MouseModeAction(gis, MouseMode.NAVIGATE);
		MouseModeAction mma2 = new MouseModeAction(gis, MouseMode.SELECT);
		MouseModeAction mma3 = new MouseModeAction(gis, MouseMode.POLYSELECT);
		MouseModeAction mma4 = new MouseModeAction(gis, MouseMode.DRAG);
		List<MouseModeAction> actions = new ArrayList<>();
		actions.add(mma1);
		actions.add(mma2);
		actions.add(mma3);
		actions.add(mma4);
		SelectionSnapAction snap = new SelectionSnapAction(gis);
		PolygonalSelectionSnapAction snapPolygonal = new PolygonalSelectionSnapAction(
				gis);

		ConfigurationAction configure = new ConfigurationAction(gis);
		NetworkStateAction network = new NetworkStateAction(viewer);
		ShowStatusbarAction ssa = new ShowStatusbarAction(gis);
		ShowToolbarAction sta = new ShowToolbarAction(gis);
		GridAction grid = new GridAction(viewer);
		TileNumberAction tileNumbers = new TileNumberAction(viewer);
		CrosshairAction crosshair = new CrosshairAction(viewer);
		ZoomAction zoomIn = new ZoomAction(viewer, true);
		ZoomAction zoomOut = new ZoomAction(viewer, false);
		OverlayAction overlay = new OverlayAction(viewer);
		GeometryInfoAction geometryInfo = new GeometryInfoAction(gis);
		QuitAction quit = new QuitAction(gis, frame);
		GeometryManagerAction gma = new GeometryManagerAction(gis);
		SelectionRectPanelAction srpa = new SelectionRectPanelAction(gis);
		SelectionPolyPanelAction sppa = new SelectionPolyPanelAction(gis);
		MapWindowPanelAction mpa = new MapWindowPanelAction(gis);
		GeometryListAction gla = new GeometryListAction(viewer, viewer);
		GeometryIndexAction gia = new GeometryIndexAction(gis);
		GeometrySelectionAction gsa = new GeometrySelectionAction(gis);
		MeasurePanelAction mla = new MeasurePanelAction(gis);
		FullscreenAction fullscreen = new FullscreenAction(gis);

		ManualAction manual = new ManualAction(gis);
		AboutAction about = new AboutAction(gis);

		JToggleButton buttonMma1 = new JToggleButton(mma1);
		JToggleButton buttonMma2 = new JToggleButton(mma2);
		JToggleButton buttonMma3 = new JToggleButton(mma3);
		JToggleButton buttonMma4 = new JToggleButton(mma4);
		JToggleButton buttonGrid = new JToggleButton(grid);
		JToggleButton buttonTileNumbers = new JToggleButton(tileNumbers);
		JToggleButton buttonCrosshair = new JToggleButton(crosshair);
		JToggleButton buttonOverlay = new JToggleButton(overlay);
		JToggleButton buttonGeometryInfo = new JToggleButton(geometryInfo);
		JToggleButton buttonSnap = new JToggleButton(snap);
		JToggleButton buttonSnapPolygonal = new JToggleButton(snapPolygonal);
		JToggleButton buttonGM = new JToggleButton(gma);
		JToggleButton buttonSRP = new JToggleButton(srpa);
		JToggleButton buttonSPP = new JToggleButton(sppa);
		JToggleButton buttonMWP = new JToggleButton(mpa);
		buttonMma1.setText(null);
		buttonMma2.setText(null);
		buttonMma3.setText(null);
		buttonMma4.setText(null);
		buttonGrid.setText(null);
		buttonTileNumbers.setText(null);
		buttonCrosshair.setText(null);
		buttonOverlay.setText(null);
		buttonGeometryInfo.setText(null);
		buttonSnap.setText(null);
		buttonSnapPolygonal.setText(null);
		buttonGM.setText(null);
		buttonSRP.setText(null);
		buttonSPP.setText(null);
		buttonMWP.setText(null);

		toolbar.add(network);
		toolbar.addSeparator();
		toolbar.add(buttonMma1);
		toolbar.add(buttonMma2);
		toolbar.add(buttonMma3);
		toolbar.add(buttonMma4);
		toolbar.addSeparator();
		toolbar.add(buttonGrid);
		toolbar.add(buttonTileNumbers);
		toolbar.add(buttonCrosshair);
		toolbar.add(zoomIn);
		toolbar.add(zoomOut);
		toolbar.add(buttonOverlay);
		toolbar.add(buttonSnap);
		toolbar.add(buttonSnapPolygonal);
		toolbar.add(buttonGeometryInfo);
		toolbar.addSeparator();
		toolbar.add(buttonMWP);
		toolbar.add(buttonSRP);
		toolbar.add(buttonSPP);
		toolbar.add(buttonGM);

		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		menuFile.add(new JMenuItem(network));
		menuFile.add(new JMenuItem(configure));
		menuFile.add(new JMenuItem(quit));

		JMenu menuView = new JMenu("View");
		menuView.setMnemonic('V');
		menuBar.add(menuView);
		menuView.add(new JCheckBoxMenuItem(ssa));
		menuView.add(new JCheckBoxMenuItem(sta));
		menuView.add(new JCheckBoxMenuItem(grid));
		menuView.add(new JCheckBoxMenuItem(tileNumbers));
		menuView.add(new JCheckBoxMenuItem(crosshair));
		menuView.add(zoomIn);
		menuView.add(zoomOut);
		menuView.add(new JCheckBoxMenuItem(overlay));

		JMenu menuMap = new JMenu("Map");
		menuMap.setMnemonic('M');
		menuBar.add(menuMap);
		menuMap.add(new GotoAction(gis));

		JMenu menuTiles = new JMenu("Tiles");
		menuTiles.setMnemonic('T');
		menuBar.add(menuTiles);
		for (TileConfig config : tileConfigs) {
			menuTiles.add(new TileConfigAction(viewer, config));
		}

		JMenu menuOverlays = new JMenu("Overlays");
		menuOverlays.setMnemonic('O');
		menuBar.add(menuOverlays);
		for (TileConfig config : overlayConfigs) {
			menuOverlays.add(new OverlayTileConfigAction(viewer, config));
		}

		JMenu menuWindows = new JMenu("Windows");
		menuWindows.setMnemonic('W');
		menuBar.add(menuWindows);
		menuWindows.add(new JCheckBoxMenuItem(gma));
		menuWindows.add(new JCheckBoxMenuItem(srpa));
		menuWindows.add(new JCheckBoxMenuItem(mpa));

		menuWindows.add(new JMenuItem(gia));
		menuWindows.add(new JMenuItem(gsa));
		menuWindows.add(new JMenuItem(gla));
		menuWindows.add(new JMenuItem(mla));

		JMenu menuOperationsAdd = new JMenu("operations");
		menuOperationsAdd.setIcon(new EmptyIcon(24));
		menuWindows.add(menuOperationsAdd);

		menuOperationsAdd.add(new OperationAction(Operations.UNION, gis));
		menuOperationsAdd
				.add(new OperationAction(Operations.INTERSECTION, gis));
		menuOperationsAdd.add(new OperationAction(Operations.DIFFERENCE, gis));
		menuOperationsAdd.add(new OperationAction(Operations.COLLECTION, gis));
		menuOperationsAdd.add(new OperationAction(Operations.HULL, gis));

		menuOperationsAdd.add(new DialogAction(gis,
				"res/images/geometryOperation/union.png", "Union Buffered",
				"union geometries and create a buffer of the result") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingBufferUnionList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(gis,
				"res/images/geometryOperation/collection.png", "Translate",
				"collect and translate geometries") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingTranslateList(viewer);
			}
		});

		menuOperationsAdd.add(new DialogAction(gis,
				"res/images/geometryOperation/collection.png", "Transform",
				"collect and transform geometries") {

			private static final long serialVersionUID = 1L;

			@Override
			protected OperationList createDialog()
			{
				return new ShowingTransformList(viewer);
			}
		});

		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		menuBar.add(menuHelp);
		menuHelp.add(new JMenuItem(manual));
		menuHelp.add(new JMenuItem(about));

		menuBar.updateUI();

		/* key bindings */

		InputMap inputMap = gis.getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = gis.getActionMap();

		ActionUtil.setupMovementActions(viewer, inputMap, actionMap);
		ActionUtil.setupZoomActions(viewer, inputMap, actionMap);

		inputMap.put(KeyStroke.getKeyStroke('a'), "a");
		inputMap.put(KeyStroke.getKeyStroke('s'), "s");
		inputMap.put(KeyStroke.getKeyStroke('d'), "d");
		inputMap.put(KeyStroke.getKeyStroke('f'), "f");

		actionMap.put("a", mma1);
		actionMap.put("s", mma2);
		actionMap.put("d", mma3);
		actionMap.put("f", mma4);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "f5");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "f6");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "f7");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), "f8");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "f9");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "f10");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "f11");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "f12");

		actionMap.put("f5", grid);
		actionMap.put("f6", tileNumbers);
		actionMap.put("f7", crosshair);
		actionMap.put("f8", overlay);
		actionMap.put("f9", geometryInfo);
		actionMap.put("f11", fullscreen);

		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK),
				"ctrl f");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK),
				"ctrl g");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
				"ctrl s");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK),
				"ctrl p");

		actionMap.put("ctrl g", gma);
		actionMap.put("ctrl s", srpa);
		actionMap.put("ctrl p", mpa);

		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK),
				"ctrl f1");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.CTRL_MASK),
				"ctrl f2");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK),
				"ctrl f3");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK),
				"ctrl f4");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_MASK),
				"ctrl f5");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.CTRL_MASK),
				"ctrl f6");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F7, InputEvent.CTRL_MASK),
				"ctrl f7");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F8, InputEvent.CTRL_MASK),
				"ctrl f8");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK),
				"ctrl f9");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.CTRL_MASK),
				"ctrl f10");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_MASK),
				"ctrl f11");
		inputMap.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.CTRL_MASK),
				"ctrl f12");

		actionMap.put("ctrl f1", new OperationAction(Operations.UNION, gis));
		actionMap.put("ctrl f2", new OperationAction(Operations.INTERSECTION,
				gis));
		actionMap.put("ctrl f3",
				new OperationAction(Operations.DIFFERENCE, gis));
		actionMap.put("ctrl f5", gla);
	}

	/**
	 * @return the selection adapter in use.
	 */
	public SelectionAdapter getSelectionAdapter()
	{
		return selectionAdapter;
	}

	/**
	 * @return the polygonal selection adapter in use.
	 */
	public PolySelectionAdapter getPolygonalSelectionAdapter()
	{
		return polySelectionAdapter;
	}

	void setupGeometryList()
	{
		JFrame frame = Components.getContainingFrame(this);

		geometryManagerDialog = new EventJDialog(frame, "Geometry Manager");
		geometryManagerDialog.setContentPane(geometryManager);
		geometryManagerDialog.setSize(new Dimension(300, 500));
	}

	void setupSelectionRectDialog(boolean show)
	{
		RectPane pane = new RectPane(this, selectionAdapter);

		selectionRectDockable = new DefaultSingleCDockable("rect-selection",
				"Rect Selection", pane);
		selectionRectDockable.setExternalizable(true);
		selectionRectDockable.setMinimizable(true);

		grid.add(1, 0, 0.3, 1, selectionRectDockable);
		// control.getContentArea().deploy(grid);

		// control.addDockable(selectionRectDockable);
		selectionRectDockable.setVisible(show);
		selectionRectDockable.setMinimizable(false);
		selectionRectDockable.setMaximizable(false);
		selectionRectDockable.setCloseable(true);
	}

	void setupSelectionPolyDialog(boolean show)
	{
		PolyPane pane = new PolyPane(this, polySelectionAdapter);

		selectionPolyDockable = new DefaultSingleCDockable("poly-selection",
				"Poly Selection", pane);
		selectionPolyDockable.setExternalizable(true);
		selectionPolyDockable.setMinimizable(true);

		grid.add(1, 1, 0.3, 1, selectionPolyDockable);
		// control.getContentArea().deploy(grid);

		// control.addDockable(selectionPolyDockable);
		selectionPolyDockable.setVisible(show);
		selectionPolyDockable.setMinimizable(false);
		selectionPolyDockable.setMaximizable(false);
		selectionPolyDockable.setCloseable(true);
	}

	void setupMapWindowDialog(boolean show)
	{
		MapWindowPane mapWindowPane = new MapWindowPane(getViewer()
				.getMapWindow());

		mapWindowDockable = new DefaultSingleCDockable("map-window",
				"Map Window", mapWindowPane);
		mapWindowDockable.setExternalizable(true);
		mapWindowDockable.setMinimizable(true);

		grid.add(1, 2, 0.3, 1, mapWindowDockable);
		// control.getContentArea().deploy(grid);

		// control.addDockable(mapWindowDockable);
		mapWindowDockable.setVisible(show);
		mapWindowDockable.setMinimizable(false);
		mapWindowDockable.setMaximizable(false);
		mapWindowDockable.setCloseable(true);
	}

	void setupBookmarksDialog(boolean show)
	{
		Bookmarks bookmarks = new Bookmarks(this);

		bookmarksDockable = new DefaultSingleCDockable("bookmarks",
				"Bookmarks", bookmarks);
		bookmarksDockable.setExternalizable(true);
		bookmarksDockable.setMinimizable(true);

		grid.add(1, 3, 0.3, 1, bookmarksDockable);
		// control.getContentArea().deploy(grid);

		// control.addDockable(mapWindowDockable);
		bookmarksDockable.setVisible(show);
		bookmarksDockable.setMinimizable(false);
		bookmarksDockable.setMaximizable(false);
		bookmarksDockable.setCloseable(true);
	}

	/**
	 * @return the Geometry Manager dialog.
	 */
	public EventJDialog getGeometryManagerDialog()
	{
		return geometryManagerDialog;
	}

	/**
	 * @return the Rect Selection Panel dialog.
	 */
	public SingleCDockable getSelectionRectPanelDialog()
	{
		return selectionRectDockable;
	}

	/**
	 * @return the Poly Selection Panel dialog.
	 */
	public SingleCDockable getSelectionPolyPanelDialog()
	{
		return selectionPolyDockable;
	}

	/**
	 * @return the Selection Panel dialog.
	 */
	public SingleCDockable getMapWindowPanelDialog()
	{
		return mapWindowDockable;
	}

	/**
	 * @return whether a click on the viewport should check for intersected
	 *         geometries of the GeometryManager and report information about
	 *         those.
	 */
	public boolean isShowGeometryInfo()
	{
		return showGeometryInfo;
	}

	/**
	 * @param state
	 *            whether a click on the viewport should check for intersected
	 *            geometries of the GeometryManager and report information about
	 *            those.
	 */
	public void setShowGeometryInfo(boolean state)
	{
		showGeometryInfo = state;
	}

	/**
	 * @return the GeometryManager in use.
	 */
	public GeometryManager getGeometryManager()
	{
		return geometryManager;
	}

	/**
	 * @return the OverlayManager in use.
	 */
	public OverlayManager getOverlayManager()
	{
		return overlayManager;
	}

	/**
	 * Show a rally exit dialog for the application.
	 * 
	 * @param frame
	 *            the frame to center the dialog on
	 */
	public static void showReallyExitDialog(JFrame frame)
	{
		int status = JOptionPane.showConfirmDialog(frame,
				"Exit Jeography GIS?", "Confirm Exit",
				JOptionPane.OK_CANCEL_OPTION);
		if (status == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Set the configuration. This does not affect current settings.
	 * 
	 * @param configuration
	 *            the new configuration.
	 */
	public void setConfiguration(Configuration configuration)
	{
		this.configuration = configuration;
	}

	/**
	 * @return whether the status bar is visible
	 */
	public boolean isShowStatusBar()
	{
		return statusBar.isVisible();
	}

	/**
	 * @param visible
	 *            whether the status bar should be visible
	 */
	public void setShowStatusBar(boolean visible)
	{
		statusBar.setVisible(visible);
	}

	/**
	 * @return whether the toolbar is visible
	 */
	public boolean isShowToolBar()
	{
		return toolbar.isVisible();
	}

	/**
	 * @param visible
	 *            whether the toolbar should be visible
	 */
	public void setShowToolBar(boolean visible)
	{
		toolbar.setVisible(visible);
	}

}
