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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.pcftosqf.pcf.PCFFont;
import net.multiphasicapps.pcftosqf.pcf.PCFGlyphMap;
import net.multiphasicapps.pcftosqf.pcf.PCFMetric;

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
	
	/** The ascent of the font. */
	protected final int ascent;
	
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
		this.ascent = ascent;
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
			
			// The width of this font
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
		
		// Debug
		todo.DEBUG.note("Bitmap size: %d bytes", bitmap.length);
		
		// Used to only map valid characters
		boolean[] isvalidchar = new boolean[256];
		
		// Bits per scan
		int bitsperscan = bytesperscan * 8;
		
		// Draw glyphs
		Map<Integer, Integer> chartoglyph = this._chartoglyph;
		PCFFont pcf = this.pcf;
		for (int i = 0; i < 256; i++)
		{
			// Only draw known characters
			Integer uc = chartoglyph.get(i);
			if (uc == null)
				continue;
			
			// Only draw valid characters
			PCFGlyphMap gm = pcf.bitmap.glyphmaps.get(uc);
			if (gm == null)
				continue;
			
			// Is valid
			isvalidchar[i] = true;
			
			// Get the input glyph data to draw
			byte[] in = gm.data();
			
			// Just print for debugging
			/*for (byte x : in)
				todo.DEBUG.note("%8s", Integer.toString(x & 0xFF, 2));*/
			
			// Determine the output position and where it ends
			int outdx = i * bytesperscan * pixelheight,
				enddx = outdx + bytesperscan;
			
			// Metrics of the PCF are the following
			//  lb v   v rb
			//      ###  | ascent
			//     #   # |
			//     ##### |
			//     #   # |
			//     #   # |
			//        #  + descent
			//       #   |
			PCFMetric metrics = pcf.metrics.get(uc);
			
			// The X position starts at the left side bearing
			// The source X is zero
			int penxstart = metrics.leftsidebearing,
				srcxstart = 0;
			
			// The ending X position is just the right side bearing
			// The source ends at the difference between these
			int penxend = metrics.rightsidebearing,
				srcxend = penxend - penxstart;
			
			// The ascent is the height of the symbol character from the
			// baseline to the top (or top to baseline)
			// The baseline is the font's own ascent
			// The Y position is just the font's ascent minus our ascent
			int baseliney = pcf.accelerators.ascent,
				penystart = baseliney - metrics.charascent,
				srcystart = 0;
			
			// The end Y position is just the ascent and descent of the
			// character
			int srcyend = metrics.charascent + metrics.chardescent,
				penyend = penystart + srcyend;
			
			// The number of bits which make up a single scan of the glyph
			int scanbitlen = (srcxend + 7) & ~7;
			
			// Debug
			/*
			todo.DEBUG.note("src=(%d, %d)->(%d, %d) | pen=(%d, %d)->(%d, %d)",
				srcxstart, srcystart, srcxend, srcyend,
				penxstart, penystart, penxend, penyend);*/
			
			// Go through each pixel in the source glyph and just copy it to
			// the destination
			for (int peny = penystart, srcy = srcystart; peny < penyend;
				peny++, srcy++)
			{
				// Draw each column
				for (int penx = penxstart, srcx = srcxstart; penx < penxend;
					penx++, srcx++)
				{
					// If the pen is outside of the bounds, do not draw
					// anything because it would cause an overflow
					if (penx < 0 || penx >= bitsperscan ||
						peny < 0 || peny >= pixelheight)
						continue;
					
					// Determine the index offset into the input glyph
					// bitmap, additionally map the bit that is used as
					// well which is reversed because it goes from highest
					// to lowest
					int psp = (srcy * scanbitlen) + srcx,
						idx = psp >>> 3,
						sub = 7 - (psp & 7);
					
					/*todo.DEBUG.note("(%d, %d) -> [%d] << %d", srcx, srcy,
						idx, sub);*/
					
					// If the bit is not lit then no pixel is to be drawn
					if ((in[idx] & (1 << sub)) == 0)
						continue;
					
					// Draw into the destination bitmap
					bitmap[outdx + (bytesperscan * peny) + (penx >>> 3)] |=
						(byte)(1 << (penx & 7));
				}
			}
			
			/*
			// Determine the initial pen position. it is based on the bearing
			// and how far down the font is
			// The ascent of the font is length of the font from the baseline
			PCFMetric metrics = pcf.metrics.get(uc);
			int penxstart = metrics.leftsidebearing,
				penystart = (pixelheight - metrics.chardescent) -
					metrics.charascent,
				penxend = penxstart + (metrics.charwidth -
					(metrics.rightsidebearing + metrics.leftsidebearing)),
				penyend = penystart +
					(metrics.charascent + metrics.chardescent),
				py = penystart,
				viswidth = penxend - penxstart;
			
			// Bytes per viswidth
			int bpvw = (viswidth / 8) + 1;
			
			// penystart was (pixelheight - descent) - metrics.charascent
			
			// Draw each line
			for (; py < penyend; py++)
			{
				// Draw each column
				for (int px = penxstart; px < penxend; px++)
				{
					todo.DEBUG.note("Pen=(%d, %d)", px, py);
					
					// Pen outside bounds of destination character?
					if (px < 0 || px >= bitsperscan ||
						py < 0 || py >= pixelheight)
						continue;
					
					// Difference between the pen positions
					int pdiffx = (px - penxstart),
						pdiffy = (py - penystart);
					
					// Glyph pixels are padded to bytes
					int adx = pdiffy * bpvw;
					
					// Outside the array bounds
					if (adx < 0 || adx >= in.length)
						continue;
					
					// Figure out the position to check in the sub-pixel
					// Higher values are higher pixels
					int subpx = 7 - (pdiffx & 0x7);
					
					// If this not lit?
					if ((in[adx] & (1 << subpx)) == 0)
						continue;
					
					bitmap[outdx + (bytesperscan * py) + (px / 8)] |=
						(byte)(1 << (px % 8));
				}
			}
			*/
			/*
			// Draw all input lines
			int sxlimit = (metrics.charwidth + 7) & ~7;
			for (int idx = 0, inn = in.length; idx < inn; idx++, py++)
			{
				// Reset the X position to the starting left side
				px = penxstart;
				
				// Draw for the entire width of the glyph
				for (int sx = 0; sx < sxlimit; sx++, px++)
				{
					// The bitmap is encoded where the higher bits are the
					// left most pixels, so that needs to be swapped
					int subpx = 7 - (sx & 0x7);
					
					// No mark here?
					int uidx = idx + (sx >>> 3);
					if (uidx >= in.length || (in[uidx] & (1 << subpx)) == 0)
						continue;
					
					// Pen outside bounds?
					if (px < 0 || px >= bitsperscan ||
						py < 0 || py >= pixelheight)
						continue;
					
					bitmap[outdx + py + (px / 8)] |= (byte)(1 << (px % 8));
				}
			}*/
		}
		
		// Write font data
		__os.write(pixelheight);
		__os.write(this.ascent);
		__os.write(descent);
		__os.write(bytesperscan);
		__os.write(charwidths);
		
		for (boolean b : isvalidchar)
			__os.write((b ? 1 : 0));
		
		__os.write(bitmap);
		
		// Flush because this might be a buffered output
		__os.flush();
	}
}

