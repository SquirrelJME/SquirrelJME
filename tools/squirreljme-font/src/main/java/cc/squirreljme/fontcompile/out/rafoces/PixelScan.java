// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans the pixels within the glyph.
 *
 * @since 2024/05/27
 */
public final class PixelScan
{
	/** ID for pixels that are on the outer edge. */
	private static final short _EDGE_HOLE_ID =
		32767;
	
	/** The bitmap to be parsed. */
	protected final GlyphBitmap bitmap;
	
	/** The area of the bitmap. */
	protected final int area;
	
	/** Fill pixels. */
	private final short[] _fill;
	
	/** Hole pixels. */
	private final short[] _hole;
	
	/** Next hole ID. */
	private short _nextHole =
		PixelScan._EDGE_HOLE_ID - 1; 
	
	/** The next fill ID. */
	private short _nextFill =
		1;
	
	/**
	 * Scans the input bitmap image and processes the individual pixels to
	 * find starting points and otherwise.
	 *
	 * @param __bitmap The bitmap to scan.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public PixelScan(GlyphBitmap __bitmap)
		throws NullPointerException
	{
		if (__bitmap == null)
			throw new NullPointerException("NARG");
		
		this.bitmap = __bitmap;
		
		// Determine bitmap area
		int area = __bitmap.width * __bitmap.height;
		this.area = area;
		
		// Initialize pixel identity
		short[] fill = new short[area];
		short[] hole = new short[area];
		for (int i = 0, n = fill.length; i < n; i++)
		{
			fill[i] = -1;
			hole[i] = -1;
		}
		
		// Store
		this._fill = fill;
		this._hole = hole;
	}
	
	/**
	 * Calculates the vector chain for the image.
	 *
	 * @return Returns the vector chain.
	 * @since 2024/05/27
	 */
	public VectorChain[] calculate()
	{
		// Input and output processing
		GlyphBitmap bitmap = this.bitmap;
		short[] fill = this._fill;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// If the bitmap has no size, this is pointless then
		if (w == 0 || h == 0)
			return new VectorChain[0];
		
		// First calculate the adjacency group for the various pixels
		this.__calcAdjacent();
		
		// Calculate vector chains
		List<VectorChain> result = new ArrayList<>();
		if (false)
			throw Debugging.todo();
		
		// Return all resultant chains
		return result.toArray(new VectorChain[result.size()]);
	}
	
	/**
	 * Calculates adjacency and which pixels belong to which connection.
	 *
	 * @since 2024/05/29
	 */
	private void __calcAdjacent()
	{
		GlyphBitmap bitmap = this.bitmap;
		short[] fill = this._fill;
		short[] hole = this._hole;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Find pixel fills
		for (int y = 0; y <= h; y++)
			for (int x = 0; x <= w; x++)
			{
				// Only consider opaque pixels
				if (!bitmap.get(x, y))
				{
					// Determine which hole this is
					this.__calcSpread(hole, false, x, y);
					
					// Skip to next pixel
					continue;
				}
				
				// Calculate spread of fill
				this.__calcSpread(fill, true, x, y);
			}
		
		// Take fills over priority of holes
		for (int y = 0; y <= h; y++)
			for (int x = 0; x <= w; x++)
			{
				int f = this.__read(fill, x, y);
				int o = this.__read(hole, x, y);
				
				// Invalidate hole if there is also a fill
				if (f >= 0 && o >= 0)
					this.__write(hole, x, y, (short)-1);
			}
		
		// Debug
		PixelScan.__dump(System.err, fill, hole, w, h);
	}
	
	/**
	 * Calculates the spread this is in.
	 *
	 * @param __data The data to access.
	 * @param __fill Use fill counter?
	 * @param __x The X coordinate to check.
	 * @param __y The Y coordinate to check.
	 * @since 2024/05/29
	 */
	private void __calcSpread(short[] __data, boolean __fill, int __x, int __y)
	{
		GlyphBitmap bitmap = this.bitmap;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Since we scan from top to bottom, we only need the left and above
		// pixel to determine which fill/hole we are in
		short l = this.__read(__data, __x - 1, __y);
		short u = this.__read(__data, __x, __y - 1);
		
		// If the upper or left is on the edge of the bitmap, then it is
		// in the void area, thus those should get the same ID
		/*if (!__fill)
		{
			// We also need to consider pixels that are on an exact edge as
			// well
			if ((u < 0 && __y - 1 < 0) || (__y == h - 1))
			{
				u = PixelScan._EDGE_HOLE_ID;
				
				// Correct pixel
				this.__write(__data, __x - 1, __y, u);
			}
			
			// Same for left
			if ((l < 0 && __x - 1 < 0) || (__x == w - 1))
			{
				l = PixelScan._EDGE_HOLE_ID;
				
				// Correct pixel
				this.__write(__data, __x, __y - 1, l);
			}
		}*/
		
		// If both adjacent pixels are valid, we need to go back and remap
		// accordingly, note that we need to go through all pixels in the cases
		// of symbols like hash `#` or `h` where the left tip may have a
		// different ID
		if (u >= 0 && l >= 0)
		{
			for (int dy = h; dy >= 0; dy--)
				for (int dx = w; dx >= 0; dx--)
				{
					// Write, prioritizing upper because it is likely to
					// have a lower ID
					short was = this.__read(__data, dx, dy);
					if (was == l || was == u)
						this.__write(__data, dx, dy, u);
				}
		}
		
		// Determine the ID of the hole
		short id;
		if (u >= 0)
			id = u;
		else if (l >= 0)
			id = l;
		
		// No other hole is adjacent
		else
		{
			if (__fill)
				id = this._nextFill++;
			else
				id = this._nextHole--;
		}
		
		// Store the index
		this.__write(__data, __x, __y, id);
	}
	
	/**
	 * Reads the index value.
	 *
	 * @param __data The data to read from.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @return The value in the data buffer.
	 * @since 2024/05/29
	 */
	private short __read(short[] __data, int __x, int __y)
	{
		// Out of bounds?
		int w = this.bitmap.width;
		if (__x < 0 || __y < 0 || __x >= w)
			return -1;
		
		// Determine index used
		int dx = (__y * w) + __x;
		if (dx < 0 || dx >= __data.length)
			return -1;
		
		return __data[dx];
	}
	
	/**
	 * Writes the index value.
	 *
	 * @param __data The data to read from.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __v The value to write.
	 * @since 2024/05/29
	 */
	private void __write(short[] __data, int __x, int __y, short __v)
	{
		// Out of bounds?
		int w = this.bitmap.width;
		if (__x < 0 || __y < 0 || __x >= w)
			return;
		
		// Determine index used
		int dx = (__y * w) + __x;
		if (dx < 0 || dx >= __data.length)
			return;
		
		// Store data
		__data[dx] = __v;
	}
	
	/**
	 * Dumps the hole and fill information.
	 *
	 * @param __ps The stream to write to.
	 * @param __fill The fills.
	 * @param __hole The holes.
	 * @param __w The width of the bitmap.
	 * @param __h The height of the bitmap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/29
	 */
	private static void __dump(PrintStream __ps,
		short[] __fill, short[] __hole, int __w, int __h)
		throws NullPointerException
	{
		if (__ps == null || __fill == null || __hole == null)
			throw new NullPointerException("NARG");
		
		for (int y = 0, i = 0; y < __h; y++)
		{
			for (int x = 0; x < __w; x++, i++)
			{
				int fill = __fill[i];
				int hole = __hole[i];
				
				if (fill >= 0 && hole >= 0)
					__ps.print('?');
				else if (fill >= 0)
					__ps.print((char)('A' + (fill % 26)));
				else if (hole == PixelScan._EDGE_HOLE_ID)
					__ps.print('-');
				else
					__ps.print((char)('a' + ((-(hole - 32768)) % 26)));
			}
			
			// Go to next row
			__ps.println();
		}
		
		// Spacing
		__ps.println();
	}
}
