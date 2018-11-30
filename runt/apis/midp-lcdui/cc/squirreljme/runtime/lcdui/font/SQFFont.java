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
import java.util.HashMap;
import java.util.Map;

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
	private static final Map<String, SQFFont> _FONT_CACHE =
		new HashMap<>();
	
	/** The pixel height of the font. */
	public final byte pixelheight;
	
	/** The ascent of the font. */
	public final byte ascent;
	
	/** The descent of the font. */
	public final byte descent;
	
	/** The bytes per scan. */
	public final byte bytesperscan;
	
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
		this._charwidths = __cw;
		this._isvalidchar = __ivc;
		this._charbmp = __bmp;
	}
	
	/**
	 * Caches the SQF font.
	 *
	 * @param __n The name of the font.
	 * @return The font.
	 * @throws IllegalArgumentException If the font does not exist or is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public static final SQFFont cacheFont(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");	
		
		Map<String, SQFFont> cache = _FONT_CACHE;
		synchronized (cache)
		{
			// Already cached?
			SQFFont rv = cache.get(__n);
			if (rv != null)
				return rv;
			
			// Read the SQF Font
			try (InputStream in = SQFFont.class.getResourceAsStream(__n))
			{
				// {@squirreljme.error EB2l The font does not exist.
				// (The name)}
				if (in == null)
					throw new IllegalArgumentException(
						String.format("EB2l %s", __n));
				
				// Read font data
				rv = SQFFont.read(in);
				
				// Cache the data
				cache.put(__n, rv);
			}
			
			// {@squirreljme.error EB2m Could not load the font data.
			// (The name)}
			catch (IOException e)
			{
				throw new IllegalArgumentException(
					String.format("EB2m %s", __n), e);
			}
			
			// Use it!
			return rv;
		}
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
}

