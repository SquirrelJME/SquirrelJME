// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.rts.map.Chunk;
import cc.squirreljme.rts.map.WorldMap;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;

/**
 * This manages a viewport, or a player's view into a specific game.
 *
 * @since 2026/06/11
 */
public class Viewport
{
	/** The local view ID. */
	protected final int localId;
	
	/** The local cursor X position. */
	protected volatile int localCursorX;
	
	/** The local cursor Y position. */
	protected volatile int localCursorY;
	
	/** The screen X coordinate of this viewport. */ 
	protected volatile int screenX;
	
	/** The screen Y coordinate of this viewport. */
	protected volatile int screenY;
	
	/** The screen width of this viewport. */
	protected volatile int screenW;
	
	/** The screen height of this viewport. */
	protected volatile int screenH;
	
	/** The map X coordinate being viewed. */
	protected volatile int mapX;
	
	/** The map Y coordinate being viewed. */
	protected volatile int mapY;
	
	/** The map width. */
	protected volatile int mapW;
	
	/** The map height. */
	protected volatile int mapH;
	
	/** The map end X coordinate. */
	protected volatile int mapEX;
	
	/** The map end Y coordinate. */
	protected volatile int mapEY;
	
	/** Current text to be drawn. */
	private final TextDrawer _debugText =
		new TextDrawer();
	
	/** Is this viewport dirty? */
	private volatile boolean _isDirty;
	
	/**
	 * Initializes the viewport.
	 *
	 * @param __localId The local view ID.
	 * @since 2026/06/12
	 */
	public Viewport(int __localId)
	{
		this.localId = __localId;
		
		// Viewports are always dirty by default, so they are force updated
		this._isDirty = true;
	}
	
	/**
	 * Center the map on the given coordinates.
	 *
	 * @param __mapX The X coordinate.
	 * @param __mapY The Y coordinate.
	 * @since 2026/06/12
	 */
	public void center(int __mapX, int __mapY)
	{
		synchronized (this)
		{
			// Set coordinates directly
			this.mapX = __mapX;
			this.mapY = __mapY;
			
			// Map width/height depends on the zoom level
			this.mapW = this.screenW;
			this.mapH = this.screenH;
			
			// Calculate the proper end coordinate of the view
			this.mapEX = this.mapX + this.mapW;
			this.mapEY = this.mapY + this.mapH;
		}
	}
	
	/**
	 * Is this view active?
	 *
	 * @return If this view is active.
	 * @since 2026/07/05
	 */
	public boolean isActive()
	{
		return true;
	}
	
	/**
	 * Updates the local cursor.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @return If an update occurred.
	 * @since 2026/07/05
	 */
	public boolean localCursor(int __x, int __y)
	{
		// Ignore cursor position if off-screen
		if (__x < 0 || __y < 0 ||
			__x >= this.screenW || __y >= this.screenH)
			return false;
		
		this.localCursorX = __x;
		this.localCursorY = __y;
		return true;
	}
	
	/**
	 * Paints the viewport.
	 *
	 * @param __g The graphics to paint into.
	 * @param __sw The actual screen width.
	 * @param __sh The actual screen height.
	 * @param __map The map to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/12
	 */
	public void paint(PencilGraphics __g, int __sw, int __sh, WorldMap __map)
		throws NullPointerException
	{
		if (__g == null || __map == null)
			throw new NullPointerException("NARG");
		
		int screenX = this.screenX;
		int screenY = this.screenY;
		
		// Force the clip in the specific screen area for this view so it does
		// not bleed anywhere else!
		__g.setClip(screenX, screenY, this.screenW, this.screenH);
		
		// Now draw!
		try
		{
			// Set map relative coordinate
			__g.translate((-this.mapX) + screenX,
				(-this.mapY) + screenY);
			
			// Draw everything that is map relative!
			try
			{
				// Draw the ground surface
				this.paintSurface(__g, __map);
				
				// Paint debugging stuff?
				this.paintDebug(__g, __map);
			}
			
			// Revert map translation
			finally
			{
				__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
			}
			
			// Draw the UI overlay after everything
			this.paintOverlay(__g);
		}
		finally
		{
			// Reset the clip
			__g.setClip(0, 0, __sw, __sh);
		}
	}
	
	/**
	 * Prints debugging information about the map.
	 *
	 * @param __g The graphics to draw onto.
	 * @param __map The map being drawn.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/12
	 */
	public void paintDebug(PencilGraphics __g, WorldMap __map)
		throws NullPointerException
	{
		if (__g == null || __map == null)
			throw new NullPointerException("NARG");
		
		// Set color for drawing the tile dot grid
		__g.setColor(0xFFFFFF);
		
		// Map view bounds
		int mapX = this.mapX;
		int mapY = this.mapY;
		int mapEX = this.mapEX;
		int mapEY = this.mapEY;
		
		// Draw the tile dot grid
		for (int y = mapY; y < mapEY; y += Chunk.TILE_TO_PX)
			for (int x = mapX; x < mapEX; x += Chunk.TILE_TO_PX)
				__g.drawLine(x, y, x + 1, y);
		
		// Draw the local cursor position
		this._debugText.draw(__g, 4, 4,
			"LC (%d, %d)", this.localCursorX, this.localCursorY);
	}
	
	/**
	 * Paints the overlay which contains all the game information.
	 *
	 * @param __g The graphics to paint into.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/12
	 */
	public void paintOverlay(PencilGraphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Get screen parameters
		int screenX = this.screenX;
		int screenY = this.screenY;
		int screenW = this.screenW;
		int screenH = this.screenH;
		
		// Draw player ID color
		__g.setColor(DrawStyle.localViewColor(this.localId));
		__g.drawRect(screenX, screenY,
			screenW - 2, screenH - 2);
		__g.drawRect(screenX + 1, screenY + 1,
			screenW - 3, screenH - 3);
	}
	
	/**
	 * Paints the surface of the map.
	 *
	 * @param __g The graphics to draw onto.
	 * @param __map The map to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/13
	 */
	public void paintSurface(PencilGraphics __g, WorldMap __map)
		throws NullPointerException
	{
		if (__g == null || __map == null)
			throw new NullPointerException("NARG");
		
		// Map view bounds
		int mapX = this.mapX;
		int mapY = this.mapY;
		int mapEX = this.mapEX;
		int mapEY = this.mapEY;
		
		// Draw the tile dot grid
		for (int y = mapY; y < mapEY; y += Chunk.TILE_TO_PX)
			for (int x = mapX; x < mapEX; x += Chunk.TILE_TO_PX)
				__g.drawLine(x, y, x + 1, y);
	}
	
	/**
	 * Splits this view.
	 *
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @param __numViewers The number of active viewers.
	 * @throws IllegalArgumentException If the width and/or height are
	 * negative.
	 * @since 2026/06/12
	 */
	public void split(int __sw, int __sh, int __numViewers)
		throws IllegalArgumentException
	{
		if (__sw <= 0 || __sh <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Determine the actual screen dimension
		int dimW = __sw;
		int dimH = __sh;
		
		// Split horizontally first
		if (__numViewers >= 2)
			dimW /= 2;
		
		// Then vertically
		if (__numViewers >= 3)
			dimH /= 2;
		
		// Use calculated values
		synchronized (this)
		{
			// Horizontal split?
			if (__numViewers >= 2)
				this.screenX = dimW * (this.localId % 2);
			else
				this.screenX = 0;
			
			// Vertical split?
			if (__numViewers >= 3)
				this.screenY = dimH * (this.localId / 2);
			else
				this.screenY = 0;
			
			// Set exact screen dimension
			this.screenW = dimW;
			this.screenH = dimH;
			
			// Correct the map view coordinates
			this.center(this.mapX, this.mapY);
	
			// No longer considered dirty
			this._isDirty = false;
		}
	}
	
	/**
	 * Splits this view if it is dirty.
	 *
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @param __numViewers The number of active viewers.
	 * @throws IllegalArgumentException If the width and/or height are
	 * zero or negative.
	 * @since 2026/06/12
	 */
	public void splitIfDirty(int __sw, int __sh, int __numViewers)
		throws IllegalArgumentException
	{
		if (__sw <= 0 || __sh <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Only split if dirty
		if (this._isDirty)
			this.split(__sw, __sh, __numViewers);
	}
}
