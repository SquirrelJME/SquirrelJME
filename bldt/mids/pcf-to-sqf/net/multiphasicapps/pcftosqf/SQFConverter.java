// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf;

import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.pcftosqf.pcf.PCFFont;

/**
 * Converts to SQF format from another font.
 *
 * @since 2018/11/27
 */
public class SQFConverter
{
	/** The input PCF font. */
	protected final PCFFont pcf;
	
	/** The pixel height of the font. */
	protected final int pixelheight;
	
	/** The descent of the font. */
	protected final int descent;
	
	/** Bytes per scan. */
	protected final int bytesperscan;
	
	/** The character widths. */
	private final byte[] _charwidths =
		new byte[256];
	
	/** Bitmap data. */
	private final byte[] _bitmap;
	
	/** Character to glyph index. */
	private final Map<Integer, Integer> _chartoglyph;
	
	/**
	 * Initializes the converter
	 *
	 * @param __pcf The PCF font to convert.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public SQFConverter(PCFFont __pcf)
		throws NullPointerException
	{
		if (__pcf == null)
			throw new NullPointerException("NARG");
		
		// Set pixels
		this.pcf = __pcf;
		
		// Extract properties
		int ascent = __pcf.accelerators.ascent,
			descent = __pcf.accelerators.descent,
			pixelheight = ascent + descent;
		this.descent = descent;
		this.pixelheight = pixelheight;
		
		// Debug
		todo.DEBUG.note("Asc + Des = PxH: %d + %d = %d",
			ascent, descent, pixelheight);
		
		// Map characters to glyph indexes, that way we can quickly find
		// them as such
		Map<Integer, Integer> chartoglyph = new HashMap<>();
		for (Map.Entry<Integer, String> e : __pcf.glyphnames.names.
			entrySet())
		{
			int ch = GlyphNames.toChar(e.getValue());
			if (ch >= 0)
				chartoglyph.put(ch, e.getKey());
		}
		
		// Store
		this._chartoglyph = chartoglyph;
		
		// Obtain all the widths
		int maxwidth = 0;
		byte[] charwidths = this._charwidths;
		for (int i = 0; i < 256; i++)
		{
			// No glyph here, ignore
			if (!chartoglyph.containsKey(i))
				continue;
			
			// Get the index
			int pcfdx = chartoglyph.get(i);
			
			// Extract the width
			int cw = __pcf.metrics.get(pcfdx).charwidth;
			charwidths[i] = (byte)cw;
			
			// Use greater width
			if (cw > maxwidth)
				maxwidth = cw;
		}
		
		// Bytes per scanline
		int bytesperscan = ((maxwidth - 1) / 8) + 1;
		this.bytesperscan = bytesperscan;
		
		// Setup bitmap
		this._bitmap = new byte[256 * bytesperscan * pixelheight];
	}
	
	/**
	 * Converts the font to SQF.
	 *
	 * @param __os The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public final void convertTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Get basic details
		int descent = this.descent,
			pixelheight = this.pixelheight,
			bytesperscan = this.bytesperscan;
		
		// Needed to build character data
		byte[] charwidths = this._charwidths;
		byte[] bitmap = this._bitmap;
		
		// Draw glyphs
		if (true)
			throw new todo.TODO();
		
		// Write font data
		__os.write(pixelheight);
		__os.write(descent);
		__os.write(bytesperscan);
		__os.write(charwidths);
		__os.write(bitmap);
	}
}

