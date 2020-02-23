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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * This represents the map of a glyph.
 *
 * @since 2018/11/29
 */
public final class PCFGlyphMap
{
	/** The glyph data. */
	private byte[] _data;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the glyph map.
	 *
	 * @param __bm The bytes used to make up the map.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public PCFGlyphMap(List<Byte> __bm)
		throws NullPointerException
	{
		if (__bm == null)
			throw new NullPointerException("NARG");
		
		// Copy the data
		int n = __bm.size();
		byte[] data = new byte[n];
		for (int i = 0; i < n; i++)
			data[i] = __bm.get(i);
		
		this._data = data;
	}
	
	/**
	 * Initializes the glyph map.
	 *
	 * @param __bm The bytes used to make up the map.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public PCFGlyphMap(byte[] __bm)
		throws NullPointerException
	{
		if (__bm == null)
			throw new NullPointerException("NARG");
		
		this._data = __bm.clone();
	}
	
	/**
	 * Returns a copy of the glyph data.
	 *
	 * @return The glyph data.
	 * @since 2018/11/29
	 */
	public final byte[] data()
	{
		return this._data.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/29
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Setup buffer
			byte[] data = this._data;
			int n = data.length;
			StringBuilder sb = new StringBuilder(16 + (n * 9));
			
			sb.append("glyph[");
			sb.append(n);
			sb.append("]={");
			
			// Build glyph bytes
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append('|');
				for (int j = 7; j >= 0; j--)
					sb.append(((data[i] & (1 << j)) != 0 ? '#' : '.'));
			}
			
			sb.append("}");
			
			// Done
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

