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

package de.topobyte.jeography.viewer.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.adt.geo.Coordinate;
import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.jeography.core.OverlayPoint;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.TileUtil;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.cache.MemoryCachePlus;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.manager.ImageManagerHttpDisk;
import de.topobyte.jeography.tiles.manager.PriorityImageManager;
import de.topobyte.jeography.tiles.manager.PriorityImageManagerHttpDisk;
import de.topobyte.jeography.viewer.Constants;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.geometry.ImageManagerUpdateListener;
import de.topobyte.melon.casting.CastUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class Viewer extends AbstractViewer implements
		LoadListener<Tile, BufferedImage>, ImageManagerUpdateListener,
		MapWindowChangeListener
{

	private static final long serialVersionUID = -2141729332089589643L;

	final static Logger logger = LoggerFactory.getLogger(Viewer.class);

	private TileMapWindow mapWindow;
	protected ImageManager<Tile, BufferedImage> imageManagerBase;
	protected ImageManager<Tile, BufferedImage> imageManagerOverlay;
	protected PaintListener globalManager;

	private MemoryCachePlus<Tile, Image> scaleCacheBase;
	private MemoryCachePlus<Tile, Image> scaleCacheOverlay;

	private Set<OverlayPoint> points = null;

	/**
	 * default constructor
	 * 
	 * @param tileConfig
	 *            the configuration to use.
	 * @param tileConfigOverlay
	 *            the configuration for overlay to use.
	 */
	public Viewer(TileConfig tileConfig, TileConfig tileConfigOverlay)
	{
		this(tileConfig, tileConfigOverlay, Constants.DEFAULT_ZOOM,
				Constants.DEFAULT_LON, Constants.DEFAULT_LAT);
	}

	/**
	 * constructor with startup position
	 * 
	 * @param tileConfig
	 *            the configuration to use.
	 * @param tileConfigOverlay
	 *            the configuration for overlay to use.
	 * @param zoom
	 *            startup zoom
	 * @param lon
	 *            startup longitude
	 * @param lat
	 *            startup latitude
	 */
	public Viewer(TileConfig tileConfig, TileConfig tileConfigOverlay,
			int zoom, double lon, double lat)
	{
		mapWindow = new SteppedMapWindow(1, 1, zoom, lon, lat);
		mapWindow.setMaxZoom(22);
		mapWindow.addChangeListener(this);

		setDoubleBuffered(true);
		setFocusable(true);

		setTileConfig(tileConfig);
		setOverlayTileConfig(tileConfigOverlay);

		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		Repainter repainter = new Repainter();
		Thread repainterThread = new Thread(repainter);
		repainterThread.start();
	}

	/**
	 * Get the underlying MapWindow.
	 * 
	 * @return the underlying MapWindow.
	 */
	@Override
	public TileMapWindow getMapWindow()
	{
		return mapWindow;
	}

	/**
	 * Set whether this Viewer shall load tiles from network.
	 * 
	 * @param state
	 *            whether to use network.
	 */
	public void setNetworkState(boolean state)
	{
		if (imageManagerBase instanceof ImageManagerHttpDisk<?>) {
			((ImageManagerHttpDisk<?>) imageManagerBase).setNetworkState(state);
		} else if (imageManagerBase instanceof PriorityImageManagerHttpDisk<?>) {
			((PriorityImageManagerHttpDisk<?>) imageManagerBase)
					.setNetworkState(state);
		}
	}

	/**
	 * Get whether this Viewer is using a network connection.
	 * 
	 * @return whether this Viewer is using a network connection.
	 */
	public boolean getNetworkState()
	{
		if (imageManagerBase instanceof ImageManagerHttpDisk<?>) {
			return ((ImageManagerHttpDisk<?>) imageManagerBase)
					.getNetworkState();
		} else if (imageManagerBase instanceof PriorityImageManagerHttpDisk<?>) {
			return ((PriorityImageManagerHttpDisk<?>) imageManagerBase)
					.getNetworkState();
		}
		return false;
	}

	private boolean scaleFast = false;

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;

		g.setColor(colorBackground);
		g.fillRect(0, 0, getWidth(), getHeight());

		int tw = mapWindow.getTileWidth();
		int th = mapWindow.getTileHeight();

		// draw base layer
		boolean isPriorityManager = imageManagerBase instanceof PriorityImageManager<?, ?, ?>;

		refreshCache(scaleCacheBase);
		refreshCache(scaleCacheOverlay);

		if (!isPriorityManager) {
			// with simple image manager

			// renew current tiles' cache status
			for (TileOnWindow tile : TileUtil.valid(mapWindow)) {
				imageManagerBase.willNeed(tile);
			}

			for (TileOnWindow tow : TileUtil.valid(mapWindow)) {
				BufferedImage image = imageManagerBase.get(tow);
				if (image != null) {
					drawImage(g, image, tow, scaleCacheBase);
				} else {
					g.setColor(colorBackground);
					g.fillRect(tow.dx, tow.dy, tw, th);
				}
			}
		} else {
			// with priority image manager
			PriorityImageManager<Tile, BufferedImage, Integer> pimb = CastUtil
					.cast(imageManagerBase);

			// first cancel pending jobs
			pimb.cancelJobs();

			// renew current tiles' cache status
			for (TileOnWindow tile : TileUtil.valid(mapWindow)) {
				pimb.willNeed(tile);
			}

			// request and draw
			for (TileOnWindow tile : TileUtil.valid(mapWindow)) {
				// calculate priority
				int priority = calculatePriority(tile);

				// request
				BufferedImage image = pimb.get(tile, priority);

				// draw
				if (image != null) {
					drawImage(g, image, tile, scaleCacheBase);
				}
			}
		}

		// draw global stuff
		if (globalManager != null) {
			globalManager.onPaint(mapWindow, g);
		}

		// draw overlay
		boolean overlay = drawOverlay && imageManagerOverlay != null;
		if (overlay) {
			// renew current tiles' cache status
			for (TileOnWindow tile : getMapWindow()) {
				imageManagerOverlay.willNeed(tile);
			}

			for (TileOnWindow tow : mapWindow) {
				BufferedImage image = overlay ? imageManagerOverlay.get(tow)
						: null;
				if (image != null) {
					drawImage(g, image, tow, scaleCacheOverlay);
				}
			}
		}

		// border, tile numbers, crosshair
		if (drawBorder) {
			GraphicsUtil.useAntialiasing(g, false);
			g.setStroke(new BasicStroke(1.0f));
			g.setColor(colorBorder);
			for (TileOnWindow tow : mapWindow) {
				g.drawRect(tow.dx, tow.dy, tw, th);
			}
		}
		if (drawTileNumbers) {
			GraphicsUtil.useAntialiasing(g, true);
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
			g.setFont(font);
			g.setColor(colorTilenumbers);
			for (TileOnWindow tow : mapWindow) {
				String tilename = tow.getTx() + "," + tow.getTy();
				g.drawString(tilename, tow.dx + 10, tow.dy + 20);
			}
		}
		if (drawCrosshair) {
			drawCrosshair(g);
		}

		if (points != null) {
			for (OverlayPoint point : points) {
				int px = (int) Math.round(mapWindow.longitudeToX(point
						.getLongitude()));
				int py = (int) Math.round(mapWindow.latitudeToY(point
						.getLatitude()));

				int d = 20;
				g.setColor(new Color(255, 0, 0, 127));
				// g.fillArc(px - d / 2, py - d / 2, d, d, 0, 360);
				g.fillRect(px - d / 2, py - d / 2, d, d);
			}
		}
		triggerPaintListeners(g);
	}

	private void refreshCache(MemoryCachePlus<Tile, Image> cache)
	{
		if (cache == null) {
			return;
		}
		for (TileOnWindow tow : mapWindow) {
			cache.refresh(tow);
		}
	}

	private void drawImage(Graphics g, BufferedImage image, TileOnWindow tow,
			MemoryCachePlus<Tile, Image> cache)
	{
		int tw = mapWindow.getTileWidth();
		int th = mapWindow.getTileHeight();

		if (image.getWidth() == tw && image.getHeight() == th) {
			g.drawImage(image, tow.dx, tow.dy, null);
		} else if (scaleFast) {
			g.drawImage(image, tow.dx, tow.dy, tw, th, null);
		} else {
			Image scaled = cache.get(tow);
			if (scaled == null) {
				scaled = image.getScaledInstance(tw, th,
						BufferedImage.SCALE_SMOOTH);
				cache.put(tow, scaled);
			}
			g.drawImage(scaled, tow.dx, tow.dy, null);
		}
	}

	private int calculatePriority(TileOnWindow tile)
	{
		int width = mapWindow.getWidth();
		int height = mapWindow.getHeight();
		int midX = width / 2;
		int midY = height / 2;
		int tX = tile.getDX() + Tile.SIZE / 2;
		int tY = tile.getDY() + Tile.SIZE / 2;
		int dX = tX - midX;
		int dY = tY - midY;
		int dist = dX * dX + dY * dY;
		return dist;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = getWidth();
		int height = getHeight();
		// System.out.println(String.format("resized to %d,%d", width, height));
		mapWindow.resize(width, height);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.grabFocus();
		if (mouseActive) {
			if (e.getClickCount() == 2) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					zoomIn(e.getPoint());
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					zoomOut(e.getPoint());
				}
				repaint();
			}
		}
		// TODO: the listeners only get triggered on click currently
		for (MouseListener listener : mouseListeners) {
			listener.mouseClicked(e);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int scrollType = e.getScrollType();
		if (scrollType == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			int wheelRotation = e.getWheelRotation();
			// int unitsToScroll = e.getUnitsToScroll();
			if (wheelRotation < 0) {
				zoomIn(e.getPoint());
			} else {
				zoomOut(e.getPoint());
			}
			repaint();
		}
	}

	@Override
	public void loadFailed(Tile thing)
	{
		// do nothing currently
	}

	@Override
	public void loaded(Tile thing, BufferedImage data)
	{
		// System.out.println("loaded: " + thing);
		dispatchRepaint();
	}

	private void zoomIn(Point point)
	{
		switch (zoomMode) {
		default:
		case ZOOM_AT_CENTER:
			mapWindow.zoomIn();
			break;
		case ZOOM_AND_CENTER_POINT:
			mapWindow.zoomInToPosition(point.x, point.y);
			break;
		case ZOOM_AND_KEEP_POINT:
			zoomFixed(point.x, point.y, true);
			break;
		}
	}

	private void zoomOut(Point point)
	{
		switch (zoomMode) {
		default:
		case ZOOM_AT_CENTER:
			mapWindow.zoomOut();
			break;
		case ZOOM_AND_CENTER_POINT:
			mapWindow.zoomOutToPosition(point.x, point.y);
			break;
		case ZOOM_AND_KEEP_POINT:
			zoomFixed(point.x, point.y, false);
			break;
		}
	}

	private void zoomFixed(int x, int y, boolean in)
	{
		// (lon, lat) that we want to keep fixed at the screen point (x, y)
		double flon = mapWindow.getPositionLon(x);
		double flat = mapWindow.getPositionLat(y);

		if (in) {
			mapWindow.zoomIn();
		} else {
			mapWindow.zoomOut();
		}

		// (x, y) of the (lon, lat) after applying the zoom change
		double fx = mapWindow.getX(flon);
		double fy = mapWindow.getY(flat);
		// shift the map to keep the (lon, lat) fixed
		mapWindow.move((int) Math.round(fx - x), (int) Math.round(fy - y));
	}

	/**
	 * Display the given points as an overlay.
	 * 
	 * @param points
	 *            a set of points to display.
	 */
	public void setOverlayPoints(Set<OverlayPoint> points)
	{
		this.points = points;
	}

	/**
	 * Set the viewport to display the overlay points...
	 */
	public void gotoOverlayPoints()
	{
		if (points == null || points.size() == 0)
			return;
		if (points.size() == 1) {
			logger.debug("hopping to point");
			OverlayPoint point = points.iterator().next();
			mapWindow.gotoLonLat(point.getLongitude(), point.getLatitude());
		} else {
			logger.debug("showing points");
			mapWindow.gotoPoints(points);
		}
	}

	/**
	 * Get the position of the mouse in geographical coordinate space. May
	 * return null, iff {@link #getMousePosition()} returns null.
	 * 
	 * @return the mouse's position.
	 */
	public Coordinate getMouseGeoPosition()
	{
		Point position = getMousePosition();
		if (position == null)
			return null;
		double lon = mapWindow.getPositionLon(position.x);
		double lat = mapWindow.getPositionLat(position.y);
		return new Coordinate(lon, lat);
	}

	/**
	 * Use the given configuration for tiles now.
	 * 
	 * @param tileConfig
	 *            the configuration to use
	 */
	public void setTileConfig(TileConfig tileConfig)
	{
		ImageManager<Tile, BufferedImage> imageManager = tileConfig
				.createImageManager();

		if (imageManager == null) {
			return;
		}

		this.tileConfig = tileConfig;
		imageManagerBase = imageManager;
		imageManagerBase.addLoadListener(this);
		scaleCacheBase = new MemoryCachePlus<>(mapWindow.minimumCacheSize());

		globalManager = tileConfig.createGlobalManager();

		triggerTileConfigListeners();
		dispatchRepaint();
	}

	/**
	 * @param config
	 *            the config to use.
	 */
	public void setOverlayTileConfig(TileConfig config)
	{
		this.overlayTileConfig = config;

		if (config != null) {
			ImageManager<Tile, BufferedImage> imageManager = config
					.createImageManager();

			if (imageManager == null) {
				return;
			}

			imageManagerOverlay = imageManager;

			// TODO: make dependable upon network-availability of overlay
			if (imageManagerOverlay instanceof ImageManagerHttpDisk<?>) {
				((ImageManagerHttpDisk<Tile>) imageManagerOverlay)
						.setNetworkState(true);
			}

			imageManagerOverlay.addLoadListener(this);
			scaleCacheOverlay = new MemoryCachePlus<>(
					mapWindow.minimumCacheSize());

			triggerOverlayTileConfigListeners();
			dispatchRepaint();
		}
	}

	private void triggerPaintListeners(Graphics g)
	{
		synchronized (paintListeners) {
			for (PaintListener pl : paintListeners) {
				pl.onPaint(mapWindow, g);
			}
		}
	}

	/**
	 * Get the current zoom level.
	 * 
	 * @return the current zoom level.
	 */
	public int getZoomLevel()
	{
		return mapWindow.getZoomLevel();
	}

	/**
	 * Get the minimum zoom available.
	 * 
	 * @return the minimum zoom.
	 */
	public int getMinZoomLevel()
	{
		// TODO: use TileResolutor here...
		return 1;
	}

	/**
	 * Get the maximum zoom available.
	 * 
	 * @return the maximum zoom.
	 */
	public int getMaxZoomLevel()
	{
		// TODO: use TileResolutor here...
		return 22;
	}

	@Override
	public void updated()
	{
		repaint();
	}

	/**
	 * @return the imageManager that is currently serving base images.
	 */
	public ImageManager<Tile, BufferedImage> getImageManagerBase()
	{
		return imageManagerBase;
	}

	/**
	 * @return the imageManager that is currently serving overlay images.
	 */
	public ImageManager<Tile, BufferedImage> getImageManagerOverlay()
	{
		return imageManagerOverlay;
	}

	@Override
	public void changed()
	{
		int cacheSize = mapWindow.minimumCacheSize();
		if (scaleCacheBase != null) {
			scaleCacheBase.setSize(cacheSize);
		}
		if (scaleCacheOverlay != null) {
			scaleCacheOverlay.setSize(cacheSize);
		}
		if (imageManagerBase != null) {
			imageManagerBase.setCacheHintMinimumSize(cacheSize);
		}
		if (imageManagerOverlay != null) {
			imageManagerOverlay.setCacheHintMinimumSize(cacheSize);
		}
	}

	public void setTileSize(int tileSize)
	{
		boolean changed = mapWindow.setTileSize(tileSize);
		if (changed) {
			if (scaleCacheBase != null) {
				scaleCacheBase.clear();
			}
			if (scaleCacheOverlay != null) {
				scaleCacheOverlay.clear();
			}
			tileSizeChanged(tileSize);
			repaint();
		}
	}

	protected void tileSizeChanged(int tileSize)
	{
		// do nothing
	}

}
