// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.IOException;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * Represents a palette within a GIF.
 *
 * @since 2022/06/26
 */
final class __GIFPalette__
{
	/** The color palette. */
	private final int[] _colors;
	
	/**
	 * Initializes the GIF palette.
	 * 
	 * @param __colors The colors to initialize with.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/26
	 */
	private __GIFPalette__(int[] __colors)
		throws NullPointerException
	{
		if (__colors == null)
			throw new NullPointerException("NARG");
		
		this._colors = __colors;
	}
	
	/**
	 * Parses the global color table.
	 * 
	 * @param __in The stream to read from.
	 * @param __size
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	static __GIFPalette__ __parseGlobal(ExtendedDataInputStream __in,
		int __size)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read in every color
		int[] colors = new int[__size];
		for (int i = 0; i < __size; i++)
			colors[i] = (__in.readUnsignedByte() << 24) |
				(__in.readUnsignedByte() << 16) |
				__in.readUnsignedByte();
		
		return new __GIFPalette__(colors);
	}
}
