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
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topboyte.interactiveview.ZoomChangedListener;
import de.topobyte.adt.geo.BBox;
import de.topobyte.adt.geo.Coordinate;
import de.topobyte.awt.util.GraphicsUtil;
import de.topobyte.jeography.core.OverlayPoint;
import de.topobyte.jeography.core.Tile;
import de.topobyte.jeography.core.TileOnWindow;
import de.topobyte.jeography.core.TileUtil;
import de.topobyte.jeography.core.mapwindow.MapWindowChangeListener;
import de.topobyte.jeography.core.mapwindow.SteplessMapWindow;
import de.topobyte.jeography.core.mapwindow.SteppedMapWindow;
import de.topobyte.jeography.core.mapwindow.TileMapWindow;
import de.topobyte.jeography.tiles.LoadListener;
import de.topobyte.jeography.tiles.cache.MemoryCachePlus;
import de.topobyte.jeography.tiles.manager.ImageManager;
import de.topobyte.jeography.tiles.manager.ImageManagerHttpDisk;
import de.topobyte.jeography.tiles.manager.PriorityImageManager;
import de.topobyte.jeography.viewer.Constants;
import de.topobyte.jeography.viewer.config.TileConfig;
import de.topobyte.jeography.viewer.geometry.ImageManagerUpdateListener;
import de.topobyte.melon.casting.CastUtil;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SteplessViewer extends AbstractViewer implements
		ComponentListener, MouseMotionListener, MouseListener,
		MouseWheelListener, LoadListener<Tile, BufferedImage>,
		ImageManagerUpdateListener, MapWindowChangeListener,
		ZoomChangedListener
{

	private static final long serialVersionUID = -2141729332089589643L;

	final static Logger logger = LoggerFactory.getLogger(SteplessViewer.class);

	private SteplessMapWindow mapWindow;
	private TileMapWindow tileMapWindow;
	private double tileScale;

	protected ImageManager<Tile, BufferedImage> imageManagerBase;
	protected ImageManager<Tile, BufferedImage> imageManagerOverlay;
	protected PaintListener globalManager;

	private MemoryCachePlus<Tile, Image> scaleCacheBase;
	private MemoryCachePlus<Tile, Image> scaleCacheOverlay;

	private Set<OverlayPoint> points = null;

	public static enum TileDrawMode {
		TRANSFORM,
		SCALE_FAST,
		SCALE_SMOOTH
	}

	private TileDrawMode mode = TileDrawMode.SCALE_SMOOTH;

	/**
	 * default constructor
	 * 
	 * @param tileConfig
	 *            the configuration to use.
	 * @param tileConfigOverlay
	 *            the configuration for overlay to use.
	 */
	public SteplessViewer(TileConfig tileConfig, TileConfig tileConfigOverlay)
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
	public SteplessViewer(TileConfig tileConfig, TileConfig tileConfigOverlay,
			int zoom, double lon, double lat)
	{
		mapWindow = new SteplessMapWindow(1, 1, zoom, lon, lat);
		mapWindow.setMaxZoom(22);
		mapWindow.addChangeListener(this);
		mapWindow.addZoomListener(this);

		setupTileMapWindow();

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
	public SteplessMapWindow getMapWindow()
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
			((ImageManagerHttpDisk<Tile>) imageManagerBase)
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
			return ((ImageManagerHttpDisk<Tile>) imageManagerBase)
					.getNetworkState();
		}
		return false;
	}

	public void setMode(TileDrawMode mode)
	{
		this.mode = mode;
	}

	public TileDrawMode getMode()
	{
		return mode;
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;

		g.setColor(colorBackground);
		g.fillRect(0, 0, getWidth(), getHeight());

		int tw = tileMapWindow.getTileWidth();
		int th = tileMapWindow.getTileHeight();

		// draw base layer
		boolean isPriorityManager = imageManagerBase instanceof PriorityImageManager<?, ?, ?>;

		refreshCache(scaleCacheBase);
		refreshCache(scaleCacheOverlay);

		if (!isPriorityManager) {
			// with simple image manager

			// renew current tiles' cache status
			for (TileOnWindow tile : TileUtil.valid(tileMapWindow)) {
				imageManagerBase.willNeed(tile);
			}

			for (TileOnWindow tile : TileUtil.valid(tileMapWindow)) {
				BufferedImage image = imageManagerBase.get(tile);
				drawTile(g, tile, image, scaleCacheBase, tw, th);
			}
		} else {
			// with priority image manager
			PriorityImageManager<Tile, BufferedImage, Integer> pimb = CastUtil
					.cast(imageManagerBase);

			// first cancel pending jobs
			pimb.cancelJobs();

			// renew current tiles' cache status
			for (TileOnWindow tile : TileUtil.valid(tileMapWindow)) {
				pimb.willNeed(tile);
			}

			// request and draw
			for (TileOnWindow tile : TileUtil.valid(tileMapWindow)) {
				// calculate priority
				int priority = calculatePriority(tile);

				// request
				BufferedImage image = pimb.get(tile, priority);

				// draw
				drawTile(g, tile, image, scaleCacheBase, tw, th);
			}
		}

		// draw global stuff
		if (globalManager != null) {
			// globalManager.onPaint(mapWindow, g);
		}

		// draw overlay
		boolean overlay = drawOverlay && imageManagerOverlay != null;
		if (overlay) {
			// renew current tiles' cache status
			for (TileOnWindow tile : tileMapWindow) {
				imageManagerOverlay.willNeed(tile);
			}

			for (TileOnWindow tow : tileMapWindow) {
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
			for (TileOnWindow tow : tileMapWindow) {
				double mx = tow.getTx() * 256 * tileScale;
				double my = tow.getTy() * 256 * tileScale;
				double ddx = mapWindow.mercatorToX(mx);
				double ddy = mapWindow.mercatorToY(my);

				int dx = (int) Math.round(ddx);
				int dy = (int) Math.round(ddy);
				tow = new TileOnWindow(tow.getZoom(), tow.getTx(), tow.getTy(),
						dx, dy);
				g.drawRect(tow.dx, tow.dy, (int) Math.round(tw * tileScale),
						(int) Math.round(th * tileScale));
			}
		}
		if (drawTileNumbers) {
			GraphicsUtil.useAntialiasing(g, true);
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
			g.setFont(font);
			g.setColor(colorTilenumbers);
			for (TileOnWindow tow : tileMapWindow) {
				double mx = tow.getTx() * 256 * tileScale;
				double my = tow.getTy() * 256 * tileScale;
				double ddx = mapWindow.mercatorToX(mx);
				double ddy = mapWindow.mercatorToY(my);

				int dx = (int) Math.round(ddx);
				int dy = (int) Math.round(ddy);
				tow = new TileOnWindow(tow.getZoom(), tow.getTx(), tow.getTy(),
						dx, dy);

				String tilename = tow.getTx() + "," + tow.getTy();
				g.drawString(tilename, tow.dx + 10, tow.dy + 20);
			}
		}
		if (drawCrosshair) {
			GraphicsUtil.useAntialiasing(g, false);
			g.setStroke(new BasicStroke(1.0f));
			g.setColor(colorCrosshair);
			g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			int d = 20;
			g.drawArc(getWidth() / 2 - d / 2, getHeight() / 2 - d / 2, d, d, 0,
					90);
			g.drawArc(getWidth() / 2 - d / 2, getHeight() / 2 - d / 2, d, d,
					180, 90);
		}

		if (points != null) {
			for (OverlayPoint point : points) {
				double px = mapWindow.longitudeToX(point.getLongitude());
				double py = mapWindow.latitudeToY(point.getLatitude());

				int d = 20;
				g.setColor(new Color(255, 0, 0, 127));
				// g.fillArc(px - d / 2, py - d / 2, d, d, 0, 360);

				Rectangle2D rect = new Rectangle2D.Double(px - d / 2, py - d
						/ 2, d, d);
				g.fill(rect);
			}
		}
		triggerPaintListeners(g);
	}

	private void refreshCache(MemoryCachePlus<Tile, Image> cache)
	{
		if (cache == null) {
			return;
		}
		for (TileOnWindow tow : tileMapWindow) {
			cache.refresh(tow);
		}
	}

	private void drawTile(Graphics2D g, TileOnWindow tile, BufferedImage image,
			MemoryCachePlus<Tile, Image> scaleCache, int tw, int th)
	{
		if (image == null) {
			g.setColor(colorBackground);
			g.fillRect(tile.dx, tile.dy, tw, th);
			return;
		}

		if (mode == TileDrawMode.TRANSFORM) {
			double mx = tile.getTx() * tw * tileScale;
			double my = tile.getTy() * th * tileScale;
			double ddx = mapWindow.mercatorToX(mx);
			double ddy = mapWindow.mercatorToY(my);
			drawImage(g, image, ddx, ddy);
		} else {
			drawImage(g, image, tile, scaleCache);
		}
	}

	private void drawImage(Graphics2D g, BufferedImage image, double ddx,
			double ddy)
	{
		AffineTransform backup = g.getTransform();
		g.translate(ddx, ddy);
		g.scale(tileScale, tileScale);
		g.drawImage(image, 0, 0, null);
		g.setTransform(backup);
	}

	private void drawImage(Graphics g, BufferedImage image, TileOnWindow tow,
			MemoryCachePlus<Tile, Image> cache)
	{
		int tw = (int) Math.round(tileMapWindow.getTileWidth() * tileScale);
		int th = (int) Math.round(tileMapWindow.getTileHeight() * tileScale);

		int atw = mapWindow.getWorldScale();
		int ath = mapWindow.getWorldScale();

		double ddx = tow.getDX() * tileScale;
		double ddy = tow.getDY() * tileScale;

		int left = (int) Math.round(ddx);
		int top = (int) Math.round(ddy);
		int right = (int) Math.round(ddx + tileScale * atw);
		int bottom = (int) Math.round(ddy + tileScale * ath);

		if (image.getWidth() == tw && image.getHeight() == th) {
			g.drawImage(image, tow.dx, tow.dy, null);
		} else if (mode == TileDrawMode.SCALE_FAST) {
			g.drawImage(image, left, top, right - left, bottom - top, null);
		} else if (mode == TileDrawMode.SCALE_SMOOTH) {
			Image scaled = cache.get(tow);
			if (scaled == null) {
				scaled = image.getScaledInstance(tw, th,
						BufferedImage.SCALE_SMOOTH);
				cache.put(tow, scaled);
			}

			g.drawImage(scaled, left, top, right - left, bottom - top, null);
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
	public void componentHidden(ComponentEvent e)
	{
		// do nothing here
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		// do nothing here
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
	public void componentShown(ComponentEvent e)
	{
		// do nothing here
	}

	private Point pointPress;
	private boolean mousePressed = false;

	private double zoomStep = 0.05;

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.grabFocus();
		if (mouseActive) {
			if (e.getClickCount() == 2) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					zoomIn(e.getPoint(), zoomStep);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					zoomOut(e.getPoint(), zoomStep);
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
	public void mouseEntered(MouseEvent e)
	{
		// do nothing here
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// do nothing here
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) {
			// System.out.println("press");
			pointPress = e.getPoint();
			mousePressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) {
			// System.out.println("release");
			mousePressed = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (mouseActive && mousePressed) {
			Point currentPoint = e.getPoint();
			int dx = pointPress.x - currentPoint.x;
			int dy = pointPress.y - currentPoint.y;
			pointPress = currentPoint;
			// down right movement is negative for both
			// System.out.println(String.format("%d %d", dx, dy));
			mapWindow.move(dx, dy);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// do nothing here
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int scrollType = e.getScrollType();
		if (scrollType == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			int wheelRotation = e.getWheelRotation();
			// int unitsToScroll = e.getUnitsToScroll();
			if (wheelRotation < 0) {
				zoomIn(e.getPoint(), zoomStep);
			} else {
				zoomOut(e.getPoint(), zoomStep);
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

	private void zoomIn(Point point, double zoomStep)
	{
		switch (zoomMode) {
		default:
		case ZOOM_AT_CENTER:
			mapWindow.zoomIn(zoomStep);
			break;
		case ZOOM_AND_CENTER_POINT:
			mapWindow.zoomInToPosition(point.x, point.y, zoomStep);
			break;
		case ZOOM_AND_KEEP_POINT:
			zoomFixed(point.x, point.y, true, zoomStep);
			break;
		}
	}

	private void zoomOut(Point point, double zoomStep)
	{
		switch (zoomMode) {
		default:
		case ZOOM_AT_CENTER:
			mapWindow.zoomOut(zoomStep);
			break;
		case ZOOM_AND_CENTER_POINT:
			mapWindow.zoomOutToPosition(point.x, point.y, zoomStep);
			break;
		case ZOOM_AND_KEEP_POINT:
			zoomFixed(point.x, point.y, false, zoomStep);
			break;
		}
	}

	private void zoomFixed(int x, int y, boolean in, double zoomStep)
	{
		// (lon, lat) that we want to keep fixed at the screen point (x, y)
		double flon = mapWindow.getPositionLon(x);
		double flat = mapWindow.getPositionLat(y);

		if (in) {
			mapWindow.zoomIn(zoomStep);
		} else {
			mapWindow.zoomOut(zoomStep);
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
		scaleCacheBase = new MemoryCachePlus<>(tileMapWindow.minimumCacheSize());

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
					tileMapWindow.minimumCacheSize());

			triggerOverlayTileConfigListeners();
			dispatchRepaint();
		}
	}

	private void triggerPaintListeners(Graphics g)
	{
		synchronized (paintListeners) {
			for (PaintListener pl : paintListeners) {
				pl.onPaint(tileMapWindow, g);
			}
		}
	}

	/**
	 * Get the current zoom level.
	 * 
	 * @return the current zoom level.
	 */
	public double getZoomLevel()
	{
		return mapWindow.getZoom();
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
		setupTileMapWindow();

		ensureCacheSize();
	}

	@Override
	public void zoomChanged()
	{
		// TODO: only if the real zoom value changed
		if (scaleCacheBase != null) {
			scaleCacheBase.clear();
		}
		if (scaleCacheOverlay != null) {
			scaleCacheOverlay.clear();
		}

		ensureCacheSize();
	}

	private void ensureCacheSize()
	{
		int cacheSize = tileMapWindow.minimumCacheSize();
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

	private void setupTileMapWindow()
	{
		BBox bbox = mapWindow.getBoundingBox();
		double realZoom = mapWindow.getZoom();
		int tileZoom = (int) Math.round(realZoom);
		// int tileZoom = (int) realZoom;

		tileMapWindow = new SteppedMapWindow(bbox, tileZoom);

		tileScale = Math.pow(2, realZoom - tileZoom);

		System.out.println("real zoom: " + realZoom);
		System.out.println("tile zoom: " + tileZoom);
		System.out.println("scale factor: " + tileScale);
	}

	public void setTileSize(int tileSize)
	{
		boolean changed = mapWindow.setWorldScale(tileSize);
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
