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
import java.util.Arrays;
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
		Short.MAX_VALUE;
	
	/** The bitmap to be parsed. */
	protected final GlyphBitmap bitmap;
	
	/** The area of the bitmap. */
	protected final int area;
	
	/** Fill pixels. */
	private final short[] _fill;
	
	/** Hole pixels. */
	private final short[] _hole;
	
	/** Next counts for fill. */
	private final short[] _fillNextTo;
	
	/** Next counts for holes. */
	private final short[] _holeNextTo;
	
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
		
		// The next to count already starts at zero
		this._fillNextTo = new short[area];
		this._holeNextTo = new short[area];
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
		int w = bitmap.width;
		int h = bitmap.height;
		
		// If the bitmap has no size, this is pointless then
		if (w == 0 || h == 0)
			return new VectorChain[0];
		
		// First calculate the adjacency group for the various pixels
		this.__calcAdjacent();
		
		// Calculate vector points
		VectorPoint[] points = this.__calcPoints();
		
		// Debug
		Debugging.debugNote("Points: %s",
			Arrays.asList(points));
		
		// Calculate the number of adjacent pixels on the same fill
		this.__calcNextTo(this._fill, this._fillNextTo);
		this.__calcNextTo(this._hole, this._holeNextTo);
		
		// Calculate vector chains
		return this.__calcVector(points);
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
		
		// Find pixel fills/holes
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
		
		// Calculate holes that are next to the edges of the bitmap, this
		// area becomes the void space
		this.__calcAdjacentEdge();
		
		// Debug
		PixelScan.__dump(System.err, fill, hole, w, h);
	}
	
	/**
	 * Calculates holes which are adjacent to bitmap edges, which become voids.
	 *
	 * @since 2024/05/29
	 */
	private void __calcAdjacentEdge()
	{
		GlyphBitmap bitmap = this.bitmap;
		short[] fill = this._fill;
		short[] hole = this._hole;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Find all holes that are next to an edge
		boolean[] edge = new boolean[Short.MAX_VALUE];
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				// Not on edge
				if (!(x == 0 || x == (w - 1) ||
					y == 0 || y == (h - 1)))
					continue;
				
				// Mark ID as being on the edge
				int id = this.__read(hole, x, y);
				if (id >= 0)
					edge[id] = true;
			}
		
		// Go through again and turn all edge adjacent IDs to edges
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				int id = this.__read(hole, x, y);
				if (id >= 0 && edge[id])
					this.__write(hole, x, y, PixelScan._EDGE_HOLE_ID);
			}
	}
	
	/**
	 * Calculates the number of pixels adjacent to the same shape.
	 *
	 * @param __data The data to use.
	 * @param __count The output counts;
	 * @since 2024/06/02
	 */
	private void __calcNextTo(short[] __data, short[] __count)
	{
		GlyphBitmap bitmap = this.bitmap;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Go through each pixel
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				// Not filled or is the edge fill?
				int id = this.__read(__data, x, y);
				if (id < 0 || id == PixelScan._EDGE_HOLE_ID)
					continue;
				
				// Get direct neighbor IDs
				int u = this.__read(__data, x, y - 1);
				int d = this.__read(__data, x, y + 1);
				int l = this.__read(__data, x - 1, y);
				int r = this.__read(__data, x + 1, y);
				
				// Determine total adjacency
				short total = (short)((u == id ? 1 : 0) +
					(d == id ? 1 : 0) +
					(l == id ? 1 : 0) +
					(r == id ? 1 : 0));
				this.__write(__count, x, y, total);
			}
	}
	
	/**
	 * Calculates vector points.
	 *
	 * @since 2024/05/29
	 */
	private VectorPoint[] __calcPoints()
	{
		GlyphBitmap bitmap = this.bitmap;
		short[] fill = this._fill;
		short[] hole = this._hole;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Go through bitmap and find starting points for all pixels and
		// holes, since we process from top to bottom, all holes should
		// follow fills since we ignore edge holes
		List<VectorPoint> result = new ArrayList<>();
		boolean[] hit = new boolean[Short.MAX_VALUE];
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				// Read details
				short f = this.__read(fill, x, y);
				short o = this.__read(hole, x, y);
				
				// Ignore edges
				if (o == PixelScan._EDGE_HOLE_ID)
					continue;
				
				// Already processed?
				short id = (f >= 0 ? f : o);
				if (hit[id])
					continue;
				
				// Do not process this ID again
				hit[id] = true;
				
				// Add in fill or short
				result.add(new VectorPoint(x, y, o >= 0));
			}
		
		// Return all resultant chains
		return result.toArray(new VectorPoint[result.size()]);
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
		
		// Determine the ID of the fill/hole
		short id;
		if (u >= 0)
			id = u;
		else if (l >= 0)
			id = l;
		
		// No other fill/hole is adjacent
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
	 * Calculates vector chains.
	 *
	 * @param __points The points which make up a vector.
	 * @return The resultant vector chains.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/29
	 */
	private VectorChain[] __calcVector(VectorPoint[] __points)
		throws NullPointerException
	{
		if (__points == null)
			throw new NullPointerException("NARG");
		
		// Used to quickly determine the shape edge
		short[] fill = this._fill;
		short[] fillNextTo = this._fillNextTo;
		short[] hole = this._hole;
		short[] holeNextTo = this._holeNextTo;
		
		// There is a one to one mapping between chains and points
		int n = __points.length;
		VectorChain[] result = new VectorChain[n];
		for (int i = 0; i < n; i++)
		{
			// Get point we are starting at
			VectorPoint point = __points[i];
			
			// Run sub-calculation using the specific fill or hole next to
			// counts
			if (point.hole)
				result[i] = this.__calcVector(point, hole, holeNextTo);
			else
				result[i] = this.__calcVector(point, fill, fillNextTo);
		}
		
		// Return all resultant chains
		return result;
	}
	
	/**
	 * Calculates a specific vector point.
	 *
	 * @param __point The point to base from.
	 * @param __data The data for the shape ID.
	 * @param __count The next to counts for the shape.
	 * @return The resultant parsed vector chain.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/02
	 */
	private VectorChain __calcVector(VectorPoint __point,
		short[] __data, short[] __count)
		throws NullPointerException
	{
		if (__point == null || __data == null || __count == null)
			throw new NullPointerException("NARG");
		
		// Determine width and height
		GlyphBitmap bitmap = this.bitmap;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Base start and end points
		int x = __point.x;
		int y = __point.y;
		int ex = __point.x;
		int ey = __point.y;
		
		// Our own shape ID
		int id = this.__read(__data, x, y);
		
		// Debug
		Debugging.debugNote("Start %s.", __point);
		
		// Resultant chain
		List<ChainCode> chain = new ArrayList<>();
		
		// Run around until back at the start
		VectorAngle angle = VectorAngle.LEFT;
		do
		{
			// Debug
			Debugging.debugNote("At (%d, %d) facing %s.",
				x, y, angle);
			
			// Fell out of bounds?
			if (x < 0 || y < 0 || x >= w || y >= h)
				throw new IllegalStateException(String.format(
					"Fell outside bounds at (%d, %d), " +
					"limit ([0, %d], [0, %d]).",
					x, y, w, h));
			
			// Get number of pixels here
			short count = this.__read(__count, x, y);
			if (count <= 0 || count >= 4)
				throw new IllegalStateException(String.format(
					"Wrong point (%d, %d) on path, count is %d?",
					x, y, count));
			
			// Determine chain code
			ChainCode code;
			if (count == 1)
				code = ChainCode.LEFT;
			else if (count == 2)
				code = ChainCode.STRAIGHT;
			else
				code = ChainCode.RIGHT;
			
			// Adjust angle accordingly
			VectorAngle newAngle = angle.moveAngle(code);
			
			// Get id of where we will be moving
			int checkX = angle.moveX(x);
			int checkY = angle.moveY(y);
			
			// If the ID matches, thus it is valid, we can move there
			int checkId = this.__read(__data, checkX, checkY);
			if (checkId == id)
			{
				x = checkX;
				y = checkY;
				
				// Point is valid, so we use the chain code
				chain.add(code);
			}
			
			// Otherwise, since we cannot move there set the new angle 
			else
			{
				angle = newAngle;
				
				// We changed angle, so we use the chain code here
				chain.add(code);
			}
		} while (!(x == ex && y == ey && angle == VectorAngle.LEFT));
		
		// Debug
		Debugging.debugNote("End %s: %s", __point, chain);
		
		return new VectorChain(__point, new ChainList(chain));
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
