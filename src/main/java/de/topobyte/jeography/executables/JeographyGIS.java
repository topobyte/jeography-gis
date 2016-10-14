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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
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
import de.topobyte.adt.geo.Coordinate;
import de.topobyte.interactiveview.ZoomChangedListener;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.gis.GisActions;
import de.topobyte.jeography.viewer.Constants;
import de.topobyte.jeography.viewer.MouseMode;
import de.topobyte.jeography.viewer.bookmarks.Bookmarks;
import de.topobyte.jeography.viewer.config.ConfigReader;
import de.topobyte.jeography.viewer.config.Configuration;
import de.topobyte.jeography.viewer.config.ConfigurationHelper;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.core.Viewer;
import de.topobyte.jeography.viewer.geometry.OverlayDragGestureListener;
import de.topobyte.jeography.viewer.geometry.OverlayManager;
import de.topobyte.jeography.viewer.geometry.manage.EventJDialog;
import de.topobyte.jeography.viewer.geometry.manage.GeometryManager;
import de.topobyte.jeography.viewer.selection.pane.PolyPane;
import de.topobyte.jeography.viewer.selection.pane.RectPane;
import de.topobyte.jeography.viewer.selection.polygonal.PolySelectionAdapter;
import de.topobyte.jeography.viewer.selection.rectangular.IntSelection;
import de.topobyte.jeography.viewer.selection.rectangular.SelectionAdapter;
import de.topobyte.jeography.viewer.statusbar.StatusBarCallback;
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
				logger.warn(
						"no configuration file found, using default configuration");
			} catch (Exception e) {
				logger.warn("unable to read configuration at user home", e);
				logger.warn("using default configuration");
			}
		}

		int width = configuration.getWidth(),
				height = configuration.getHeight();
		Path pathDatabase = configuration.getPathDatabase();
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
			pathDatabase = Paths.get(line.getOptionValue("db"));
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
				logger.error("error while setting look and feel '" + lookAndFeel
						+ "': " + e.getClass().getSimpleName() + ", message: "
						+ e.getMessage());
			}
		}

		final JeographyGIS gis = new JeographyGIS(configFile, configuration,
				tilesIndex, pathDatabase, isOnline, showGrid, showTileNumbers,
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

	private String statusBarText = null;

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
		statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		statusBar.add(statusLabel, c);
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		statusBar.add(zoomControl, c);

		GisActions.setupActions(frame, this, toolbar, menuBar,
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
	 * @param pathDatabase
	 *            a path to a SQLite database for place lookup.
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
			int tilesIndex, Path pathDatabase, boolean isOnline,
			boolean showGrid, boolean showTileNumbers, boolean showCrosshair,
			boolean showOverlay)
	{
		this(configFile, configuration, tilesIndex, pathDatabase, isOnline,
				showGrid, showTileNumbers, showCrosshair, showOverlay,
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
	 * @param pathDatabase
	 *            a path to a SQLite database for place lookup.
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
			int tilesIndex, Path pathDatabase, boolean isOnline,
			boolean showGrid, boolean showTileNumbers, boolean showCrosshair,
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
		String text = null;
		if (coordinate == null) {
			text = String.format(
					"center: %f:%f mouse: n/a zoom: %d selection: %s",
					viewer.getMapWindow().getCenterLon(),
					viewer.getMapWindow().getCenterLat(), viewer.getZoomLevel(),
					s);
		} else {
			text = String.format(
					"center: %f:%f mouse: %f:%f zoom: %d selection: %s",
					viewer.getMapWindow().getCenterLon(),
					viewer.getMapWindow().getCenterLat(),
					coordinate.getLongitude(), coordinate.getLatitude(),
					viewer.getZoomLevel(), s);
		}
		statusBarText = text;
		statusLabel.setText(text);
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
		MapWindowPane mapWindowPane = new MapWindowPane(
				getViewer().getMapWindow());

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

		mapWindowPane.addStatusBarCallback(new StatusBarCallback() {

			@Override
			public void noInfoAvailable()
			{
				statusLabel.setText(statusBarText);
			}

			@Override
			public void infoAvailable(String info)
			{
				statusLabel.setText(info);
			}

		});
	}

	void setupBookmarksDialog(boolean show)
	{
		Bookmarks bookmarks = new Bookmarks(this);

		bookmarksDockable = new DefaultSingleCDockable("bookmarks", "Bookmarks",
				bookmarks);
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
		int status = JOptionPane.showConfirmDialog(frame, "Exit Jeography GIS?",
				"Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
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
