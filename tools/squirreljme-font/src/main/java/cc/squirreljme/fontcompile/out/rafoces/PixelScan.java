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
import java.util.ArrayList;
import java.util.List;

/**
 * Scans the pixels within the glyph.
 *
 * @since 2024/05/27
 */
public final class PixelScan
{
	/** The bitmap to be parsed. */
	protected final GlyphBitmap bitmap;
	
	/** The shape identifiers. */
	private final byte[] _ident;
	
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
		
		// Initialize pixel identity
		byte[] ident = new byte[__bitmap.width * __bitmap.height];
		for (int i = 0, n = ident.length; i < n; i++)
			ident[i] = -1;
		this._ident = ident;
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
		byte[] ident = this._ident;
		int w = bitmap.width;
		int h = bitmap.height;
		
		// Find vector chains
		List<VectorChain> result = new ArrayList<>();
		for (int y = 0; y < h; y++)
			for (int x = 0, i = (y * h); x < w; x++, i++)
			{
				// Skip already processed pixels
				if (ident[i] >= 0)
					continue;
				
				// Only consider opaque pixels
				if (!bitmap.get(x, y))
					continue;
				
				throw Debugging.todo();
			}
		
		// Return all resultant chains
		return result.toArray(new VectorChain[result.size()]);
	}
}
