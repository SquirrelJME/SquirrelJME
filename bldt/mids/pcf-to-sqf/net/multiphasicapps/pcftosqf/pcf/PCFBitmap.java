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
 * This represents the bitmap from within a PCF file.
 *
 * @since 2018/11/28
 */
public final class PCFBitmap
{
	/** The format. */
	public final int format;
	
	/** The number of glyphs. */
	public final int numglyphs;
	
	/** The offsets. */
	private final int[] _offsets;
	
	/** The bitmap data. */
	private final byte[] _data;
	
	/**
	 * Initializes the bitmap data.
	 *
	 * @param __format The format used.
	 * @param __numglyphs The glyphs used.
	 * @param __offsets The offsets.
	 * @param __data The data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public PCFBitmap(int __format, int __numglyphs, int[] __offsets,
		byte[] __data)
		throws NullPointerException
	{
		if (__offsets == null || __data == null)
			throw new NullPointerException("NARG");
		
		this.format = __format;
		this.numglyphs = __numglyphs;
		this._offsets = __offsets.clone();
		this._data = __data.clone();
		
		// Debug
		StringBuilder sb = new StringBuilder();
		for (int j = 0, n = __data.length; j < n; j++)
		{
			sb.setLength(0);
			
			byte b = __data[j];
			for (int i = 7; i >= 0; i--)
				sb.append(((b & (1 << i)) != 0 ? '#' : '.'));
			
			todo.DEBUG.note("%04d: %s", j, sb);
		}
	}
	
	/**
	 * Reads the bitmap data.
	 *
	 * @param __dis The input stream.
	 * @return The resulting bitmap.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public static final PCFBitmap read(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Format
		int format = Integer.reverseBytes(__dis.readInt());
		
		// Read the glyph offsets
		int numglyphs = __dis.readInt();
		int[] offsets = new int[numglyphs];
		for (int i = 0; i < numglyphs; i++)
			offsets[i] = __dis.readInt();
		
		// Read all four bitmap sizes
		int[] bitmapsizes = new int[4];
		for (int i = 0; i < 4; i++)
			bitmapsizes[i] = __dis.readInt();
		
		// The bytes depends on the format
		byte[] data = new byte[bitmapsizes[format & 3]];
		__dis.readFully(data);
		
		// Build bitmap
		return new PCFBitmap(format, numglyphs, offsets, data);
	}
}

