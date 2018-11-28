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
 *  - int8 descent.
 *  - int8 bytesperscan (The number of bytes per scanline).
 *  - int8[256] charwidths.
 *  - uint[256 * bytesperscan * pixelheight] charbmp.
 *
 * @since 2018/11/27
 */
public final class SQFFont
{
	/** The pixel height of the font. */
	public final byte pixelheight;
	
	/** The descent of the font. */
	public final byte descent;
	
	/** The bytes per scan. */
	public final byte bytesperscan;
	
	/** The character widths. */
	private final byte[] _charwidths;
	
	/** The character bitmap. */
	private final byte[] _charbmp;
	
	/**
	 * Initializes the SQF Font.
	 *
	 * @param __ph The pixel height.
	 * @param __d The descent.
	 * @param __bps The bytes per scan.
	 * @param __cw Character widths.
	 * @param __bmp The bitmap data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	private SQFFont(byte __ph, byte __d, byte __bps, byte[] __cw,
		byte[] __bmp)
		throws NullPointerException
	{
		if (__cw == null || __bmp == null)
			throw new NullPointerException("NARG");
		
		this.pixelheight = __ph;
		this.descent = __d;
		this.bytesperscan = __bps;
		this._charwidths = __cw;
		this._charbmp = __bmp;
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
			descent = dis.readByte(),
			bytesperscan = dis.readByte();
		
		// Read the widths
		byte[] charwidths = new byte[256];
		dis.readFully(charwidths);
		
		// Read the bitmaps
		byte[] charbmp = new byte[256 * bytesperscan * pixelheight];
		dis.readFully(charbmp);
		
		// Build font
		return new SQFFont(pixelheight, descent, bytesperscan,
			charwidths, charbmp);
	}
}

