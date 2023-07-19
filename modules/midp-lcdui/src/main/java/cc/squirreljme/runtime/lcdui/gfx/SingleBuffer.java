// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.util.Arrays;
import javax.microedition.lcdui.Graphics;

/**
 * Represents a single buffer for use with {@link DoubleBuffer}.
 *
 * @since 2022/02/25
 */
public final class SingleBuffer
{
	/** The color to fill with on resizes. */
	private final int fillColor;
	
	/** Available pixels. */
	private volatile int[] _pixels =
		new int[1];
	
	/** The width of the buffer. */
	private volatile int _width =
		1;
	
	/** The height of the buffer. */
	private volatile int _height =
		1;
	
	/**
	 * Initializes the single buffer.
	 * 
	 * @param __resizeFillColor The color to fill with when resizing.
	 * @since 2022/02/25
	 */
	public SingleBuffer(int __resizeFillColor)
	{
		this.fillColor = __resizeFillColor;
	}
	
	/**
	 * Clears the buffer to the fill color.
	 * 
	 * @since 2022/02/25
	 */
	public void clear()
	{
		Arrays.fill(this._pixels, this.fillColor);
	}
	
	/**
	 * Copies from the source buffer.
	 * 
	 * @param __source The source buffer.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	public void copyFrom(SingleBuffer __source)
		throws NullPointerException
	{
		if (__source == null)
			throw new NullPointerException("NARG");
			
		// Get destination parameters
		int[] destPixels = this._pixels;
		int destLimit = destPixels.length;
		
		// Get source parameters
		int[] srcPixels = __source._pixels;
		int srcWidth = __source._width;
		int srcHeight = __source._height;
		int srcArea = srcWidth * srcHeight;
		
		// If the source is larger, we need a new and bigger array but we
		// can just automatically copy that array over
		if (srcArea > destLimit)
			this._pixels = Arrays.copyOf(srcPixels, srcArea);
		
		// Only copy what is needed
		else
			System.arraycopy(srcPixels, 0,
				destPixels, 0, srcArea);
		
		// Copy the parameters over
		this._width = srcWidth;
		this._height = srcHeight;
	}
	
	/**
	 * Returns a graphics object for drawing into the off-screen buffer. If
	 * the screen size varies, then the contents of the graphics buffer is
	 * undefined.
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
		/* {@squirreljme.error EB31 Invalid buffer dimensions.} */
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("EB31");
		
		int[] pixels = this._pixels;
		
		// Do we need to resize the buffer?
		int currentLimit = pixels.length;
		int wantedArea = __width * __height;
		if (wantedArea > currentLimit)
		{
			this._pixels = (pixels = new int[wantedArea]);
			
			// Fill resized area accordingly
			Arrays.fill(pixels, this.fillColor);
		}
		
		// Set new parameters
		this._width = __width;
		this._height = __height;
		
		// Create graphics to wrap it, alpha is not used for buffers!
		return PencilGraphics.hardwareGraphics(UIPixelFormat.INT_RGB888,
			__width, __height,
			pixels, 0, null,
			0, 0, __width, __height);
	}
	
	/**
	 * Paints this buffer onto the target graphics instance.
	 * 
	 * @param __g The graphics to paint onto.
	 * @since 2022/02/25
	 */
	public void paint(Graphics __g)
	{
		// The fastest way to draw onto the screen is to do a direct draw
		// from the RGB pixel data
		int pw = this._width;
		__g.drawRGB(this._pixels, 0, pw, 0, 0,
			pw, this._height, false);
	}
}
