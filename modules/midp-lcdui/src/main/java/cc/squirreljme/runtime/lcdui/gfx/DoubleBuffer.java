// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This class implements a double buffered image which may be used for
 * graphics operations.
 *
 * @since 2022/02/25
 */
public final class DoubleBuffer
{
	/** The proxy for the off-screen graphics. */
	private final ProxyGraphicsTarget _offScreenProxy =
		new ProxyGraphicsTarget(Image.createImage(1, 1)
			.getGraphics());
	
	/** The off-screen buffer. */
	private final SingleBuffer _offScreen;
	
	/** The on-screen buffer. */
	private final SingleBuffer _onScreen;
	
	/** The last used width. */
	private volatile int _lastWidth =
		-1;
	
	/** The last used height. */
	private volatile int _lastHeight =
		-1;
	
	/**
	 * Initializes the double buffer.
	 * 
	 * @param __resizeFillColor The color to set when resizing, this is to
	 * prevent skewed graphics from appearing.
	 * @since 2022/02/25
	 */
	public DoubleBuffer(int __resizeFillColor)
	{
		this._offScreen = new SingleBuffer(__resizeFillColor);
		this._onScreen = new SingleBuffer(__resizeFillColor);
	}
	
	/**
	 * Clears the off-screen buffer.
	 * 
	 * @since 2022/02/25
	 */
	public void clear()
	{
		this._offScreen.clear();
	}
	
	/**
	 * Flushes the off-screen buffer to be on-screen.
	 * 
	 * @since 2022/02/25
	 */
	public void flush()
	{
		this._onScreen.copyFrom(this._offScreen,
			0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * Flushes the off-screen buffer to be on-screen.
	 * 
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2024/08/04
	 */
	public void flush(int __x, int __y, int __w, int __h)
	{
		this._onScreen.copyFrom(this._offScreen, __x, __y, __w, __h);
	}
	
	/**
	 * Returns a graphics object for drawing into the off-screen buffer.
	 * 
	 * @param __width The buffer width.
	 * @param __height The buffer height.
	 * @return The graphics to draw onto the image.
	 * @throws IllegalArgumentException If the width and/or height are invalid.
	 * @since 2022/02/25
	 */
	public Graphics getGraphics(int __width, int __height)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB32 Invalid buffer dimensions.} */
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("EB32");
		
		// We use the proxy regardless
		ProxyGraphicsTarget proxy = this._offScreenProxy;
		ProxyGraphics rv = new ProxyGraphics(proxy, __width, __height);
		
		// If the surface area has not changed, then we can freely use the same
		// graphics object, this will help reduce load on double-buffered
		// operations
		int lastWidth = this._lastWidth;
		int lastHeight = this._lastHeight;
		if (__width == lastWidth && __height == lastHeight)
			return rv;
		
		// Otherwise, remember our new screen space and use the graphics it
		// produces 
		proxy.setGraphics(this._offScreen.getGraphics(__width, __height));
		this._lastWidth = __width;
		this._lastHeight = __height;
		
		return rv;
	}
	
	/**
	 * Returns the buffer height.
	 *
	 * @return The buffer height.
	 * @since 2024/08/04
	 */
	public int height()
	{
		return Math.max(0, this._lastHeight);
	}
	
	/**
	 * Paints the on-screen buffer onto the given graphics instance.
	 * 
	 * @param __g The graphics to paint onto.
	 * @since 2022/02/25
	 */
	public void paint(Graphics __g)
	{
		this._onScreen.paint(__g);
	}
	
	/**
	 * Returns the buffer width.
	 *
	 * @return The buffer width.
	 * @since 2024/08/04
	 */
	public int width()
	{
		return Math.max(0, this._lastWidth);
	}
}
