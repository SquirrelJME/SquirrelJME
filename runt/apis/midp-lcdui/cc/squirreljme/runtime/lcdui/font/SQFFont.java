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
 *  - uint8 pixelheight.
 *  - uint8 descent.
 *  - uint8 bytesperscan (The number of bytes per scanline).
 *  - uint8[256] charwidths.
 *  - uint[256 * bytesperscan * pixelheight] charbmp.
 *
 * @since 2018/11/27
 */
public final class SQFFont
{
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
		
		throw new todo.TODO();
	}
}

