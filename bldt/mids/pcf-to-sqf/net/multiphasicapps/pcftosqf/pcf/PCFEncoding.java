// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf.pcf;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This contains the PCF encoding data.
 *
 * @since 2018/11/28
 */
public final class PCFEncoding
{
	/** The format. */
	public final int format;
	
	/** The minimum char or byte 2? */
	public final int mincharorbyte2;
	
	/** The maximum char or byte 2? */
	public final int maxcharorbyte2;
	
	/** Min byte1? */
	public final int minbyte1;
	
	/** Max byte1? */
	public final int maxbyte1;
	
	/** Default character. */
	public final int defaultchar;
	
	/** Glyph indexes. */
	private final int[] _glyphindexes;
	
	/**
	 * Initializes the encoding.
	 *
	 * @param __format Format.
	 * @param __mincharorbyte2 Minimum char or byte2?
	 * @param __maxcharorbyte2 Maximum char or byte2?
	 * @param __minbyte1 Min byte1?
	 * @param __maxbyte1 Max byte1?
	 * @param __defaultchar Default character.
	 * @param __glyphindexes Glyph indexes.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public PCFEncoding(int __format, int __mincharorbyte2,
		int __maxcharorbyte2, int __minbyte1, int __maxbyte1,
		int __defaultchar, int[] __glyphindexes)
		throws NullPointerException
	{
		if (__glyphindexes == null)
			throw new NullPointerException("NARG");
		
		this.format = __format;
		this.mincharorbyte2 = __mincharorbyte2;
		this.maxcharorbyte2 = __maxcharorbyte2;
		this.minbyte1 = __minbyte1;
		this.maxbyte1 = __maxbyte1;
		this.defaultchar = __defaultchar;
		this._glyphindexes = __glyphindexes.clone();
	}
	
	/**
	 * Reads the encoding data.
	 *
	 * @param __dis The input stream.
	 * @return The resulting encoding data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public static final PCFEncoding read(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Format
		int format = Integer.reverseBytes(__dis.readInt());
		
		// Read properties
		int mincharorbyte2 = __dis.readShort(),
			maxcharorbyte2 = __dis.readShort(),
			minbyte1 = __dis.readShort(),
			maxbyte1 = __dis.readShort(),
			defaultchar = __dis.readShort();
		
		// Read index table
		int numglyphindexes = (maxcharorbyte2 - mincharorbyte2 + 1) *
			(maxbyte1 - minbyte1 + 1);
		int[] glyphindexes = new int[numglyphindexes];
		for (int i = 0; i < numglyphindexes; i++)
			glyphindexes[i] = __dis.readShort();
		
		// Build
		return new PCFEncoding(format, mincharorbyte2, maxcharorbyte2,
			minbyte1, maxbyte1, defaultchar, glyphindexes);
	}
}

