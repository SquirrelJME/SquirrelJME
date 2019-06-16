// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.lcdui.Font;

/**
 * This represents a SQF Font (SquirrelJME Font) which is a compacted and
 * simplified font representation that is made to be as simple as possible by
 * having the desired goals: small, low memory, easy to load.
 *
 * All fonts are ISO-8859-15, same sized cells for each characters although
 * their width can differ.
 *
 * The font is in the following format:
 *  - int8 pixelheight.
 *  - int8 ascent.
 *  - int8 descent.
 *  - int8 bytesperscan (The number of bytes per scanline).
 *  - int8[256] charwidths.
 *  - int8[256] isvalidchar.
 *  - uint[256 * bytesperscan * pixelheight] charbmp.
 *
 * @since 2018/11/27
 */
public final class SQFFont
{
	/** SQF Font data. */
	private static final Map<SQFFontSpecifier, SQFFont> _FONT_CACHE =
		new HashMap<>();
	
	/** The pixel height of the font. */
	public final byte pixelheight;
	
	/** The ascent of the font. */
	public final byte ascent;
	
	/** The maximum ascent of the font. */
	public final byte maxascent;
	
	/** The descent of the font. */
	public final byte descent;
	
	/** The bytes per scan. */
	public final byte bytesperscan;
	
	/** The bits per scan. */
	public final byte bitsperscan;
	
	/** The number of bytes that make up a character's bitmap. */
	public final int charbitmapsize;
	
	/**
	 * The leading of the font.
	 * It is unspecified what this value actually should be so for now it is
	 * just a constant.
	 */
	public final byte leading =
		2;
	
	/** Is this character valid? */
	private final boolean[] _isvalidchar;
	
	/** The character widths. */
	private final byte[] _charwidths;
	
	/** The character bitmap. */
	private final byte[] _charbmp;
	
	/**
	 * Initializes the SQF Font.
	 *
	 * @param __ph The pixel height.
	 * @param __a The ascent.
	 * @param __d The descent.
	 * @param __bps The bytes per scan.
	 * @param __cw Character widths.
	 * @param __ivc Is this a valid character?
	 * @param __bmp The bitmap data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	private SQFFont(byte __ph, byte __a, byte __d, byte __bps, byte[] __cw,
		boolean[] __ivc, byte[] __bmp)
		throws NullPointerException
	{
		if (__cw == null || __ivc == null || __bmp == null)
			throw new NullPointerException("NARG");
		
		this.pixelheight = __ph;
		this.ascent = __a;
		this.descent = __d;
		this.bytesperscan = __bps;
		this.bitsperscan = (byte)(__bps * 8);
		this._charwidths = __cw;
		this._isvalidchar = __ivc;
		this._charbmp = __bmp;
		this.charbitmapsize = __bps * __ph;
		
		// The maximum ascent is just the ascent since all SQFs have the
		// same properties
		this.maxascent = __a;
	}
	
	/**
	 * Returns the number of bytes each character requires to store its
	 * bitmap data.
	 *
	 * @return The character bitmap size.
	 * @since 2018/12/01
	 */
	public final int charBitmapSize()
	{
		return this.charbitmapsize;
	}
	
	/**
	 * Returns the width of the specified character.
	 *
	 * @param __c The character to get the width of.
	 * @return The width of the given character.
	 * @since 2018/11/30
	 */
	public final int charWidth(char __c)
	{
		byte[] charwidths = this._charwidths;
		if (__c > 256 || !this._isvalidchar[__c])
			return charwidths[0];
		return charwidths[__c];
	}
	
	/**
	 * Is the specified character valid?
	 *
	 * @param __c The character to check.
	 * @return If the character is valid.
	 * @since 2019/06/16
	 */
	public final boolean isValid(char __c)
	{
		if (__c > 256)
			return false;
		return this._isvalidchar[__c];
	}
	
	/**
	 * Loads the bitmap
	 *
	 * @param __c The character to get.
	 * @param __bmp The output bitmap.
	 * @return The bytes per scanline.
	 * @throws IndexOutOfBoundsException If the bitmap is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/01
	 */
	public final int loadCharBitmap(char __c, byte[] __bmp)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Illegal character? Use the replacement
		if (__c > 256 || !this._isvalidchar[__c])
			__c = 0;
		
		// Need to know this to copy each scanline of the font
		int charbitmapsize = this.charbitmapsize;
		
		// Copy bitmap data
		byte[] charbmp = this._charbmp;
		for (int o = 0, i = __c * charbitmapsize; o < charbitmapsize; o++, i++)
			__bmp[o] = charbmp[i];
		
		// Return the bytes per scan
		return this.bytesperscan;
	}
	
	/**
	 * Prints the glyph data to the given stream.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/01
	 */
	public final void printGlyphs(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		int pixelheight = this.pixelheight,
			bytesperscan = this.bytesperscan,
			bitsperscan = this.bitsperscan;
		
		// Setup string builders to handle each line
		int pipecount = 0;
		StringBuilder[] sb = new StringBuilder[pixelheight + 1];
		for (int i = 0; i < pixelheight + 1; i++)
			sb[i] = new StringBuilder();
		
		// Print each character
		byte[] charwidths = this._charwidths,
			charbmp = this._charbmp;
		boolean[] isvalidchar = this._isvalidchar;
		for (int ch = 0; ch < 256; ch++)
		{
			// Ignore unspecified characters
			if (!isvalidchar[ch])
				continue;
			
			int charwidth = (int)charwidths[ch],
				gapw = charwidth + 1;
			
			// Force font to single character
			if (charwidth < 1)
				charwidth = 1;
			
			// Put description line
			sb[0].append(String.format(
				"%-" + charwidth + "." + charwidth + "s",
				String.format("%02x,%d", ch, charwidth)));
			sb[0].append('|');
			
			// Draw each line
			for (int y = 0; y < pixelheight; y++)
			{
				int p = (ch * bytesperscan * pixelheight) +
					(y * bytesperscan);
				
				// Draw each column into the buffers
				for (int x = 0; x < charwidth; x++)
					sb[y + 1].append(
						(((charbmp[p + (x >>> 3)] & (1 << (x & 0x7))) != 0) ?
						'#' : '.'));
				
				// End in pipe
				sb[y + 1].append('|');
			}
			
			// Draw string builders and clear
			pipecount += (charwidth + 1);
			if (pipecount >= 70)
			{
				// Clear out
				for (int i = 0, n = sb.length; i < n; i++)
				{
					__ps.println(sb[i]);
					sb[i].setLength(0);
				}
				
				// Reset for next time
				pipecount = 0;
			}
		}
		
		// Clear out
		if (pipecount > 0)
		{
			for (int i = 0, n = sb.length; i < n; i++)
				__ps.println(sb[i]);
		}
	}
	
	/**
	 * Caches the SQF font.
	 *
	 * @param __n The name of the font.
	 * @param __pxs The pixel size of the font.
	 * @return The font.
	 * @throws IllegalArgumentException If the font does not exist or is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public static final SQFFont cacheFont(String __n, int __pxs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");	
		
		// Build specifier
		SQFFontSpecifier spec = new SQFFontSpecifier(__n, __pxs);
		
		Map<SQFFontSpecifier, SQFFont> cache = _FONT_CACHE;
		synchronized (cache)
		{
			// Already cached?
			SQFFont rv = cache.get(spec);
			if (rv != null)
				return rv;
			
			// Read the SQF Font
			try (InputStream in = SQFFont.class.getResourceAsStream(
				spec.toFileName()))
			{
				// {@squirreljme.error EB2l The font does not exist.
				// (The name; The pixel size)}
				if (in == null)
					throw new IllegalArgumentException(
						String.format("EB2l %s %d", __n, __pxs));
				
				// Read font data
				rv = SQFFont.read(in);
				
				// Cache the data
				cache.put(spec, rv);
			}
			
			// {@squirreljme.error EB2m Could not load the font data.
			// (The name; The pixel size)}
			catch (IOException e)
			{
				throw new IllegalArgumentException(
					String.format("EB2m %s %d", __n, __pxs), e);
			}
			
			// Use it!
			return rv;
		}
	}
	
	/**
	 * Caches the SQF font based on the passed font object.
	 *
	 * @param __f The font to cache.
	 * @return The SQF data for this font.
	 * @throws IllegalArgumentException If the font does not exist or is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public static final SQFFont cacheFont(Font __f)
		throws IllegalArgumentException, NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		return SQFFont.cacheFont(__f.getFontName(), __f.getPixelSize());
	}
	
	/**
	 * Reads and returns a SQF Font.
	 *
	 * @param __in The input stream data.
	 * @return The decoded SQF Font.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public static final SQFFont read(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Makes it easier to handle things
		DataInputStream dis = new DataInputStream(__in);
		
		// Read fields
		byte pixelheight = dis.readByte(),
			ascent = dis.readByte(),
			descent = dis.readByte(),
			bytesperscan = dis.readByte();
		
		// Read the widths
		byte[] charwidths = new byte[256];
		dis.readFully(charwidths);
		
		// Valid characters?
		boolean[] isvalidchar = new boolean[256];
		for (int i = 0; i < 256; i++)
			if (dis.readByte() != 0)
				isvalidchar[i] = true;
		
		// Read the bitmaps
		byte[] charbmp = new byte[256 * bytesperscan * pixelheight];
		dis.readFully(charbmp);
		
		// Build font
		return new SQFFont(pixelheight, ascent, descent, bytesperscan,
			charwidths, isvalidchar, charbmp);
	}
	
	/**
	 * This maps the given character to the SQF character map.
	 *
	 * Since SQFs are ISO-8859-15, they do not map exactly to ISO-8859-1
	 * which may be the default encoding.
	 *
	 * @param __c The character to map.
	 * @return The mapped character.
	 * @since 2019/04/29
	 */
	public static final char mapChar(char __c)
	{
		switch (__c)
		{
				// Convert
			case 0x20AC:	return (char)0x00A4;
			case 0x0160:	return (char)0x00A6;
			case 0x0161:	return (char)0x00A8;
			case 0x017D:	return (char)0x00B4;
			case 0x017E:	return (char)0x00B8;
			case 0x0152:	return (char)0x00BC;
			case 0x0153:	return (char)0x00BD;
			case 0x0178:	return (char)0x00BE;
			
				// Map similar characters to others
			case 0x00A4:	return (char)0x20AC;
			case 0x00B4:	return '\'';
			case 0x00A6:	return '|';
			case 0x00A8:	return '"';
				
				// Cannot be mapped
			case 0x00B8:
			case 0x00BC:
			case 0x00BD:
			case 0x00BE:	return (char)0xFFFD;
		}
		
		// Mapped the same
		return __c;
	}
}

