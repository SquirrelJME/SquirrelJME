// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.IOException;

/**
 * This contains the fixed huffman tree.
 *
 * @since 2016/03/11
 */
final class __InflateFixedHuffman__
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/03/11
	 */
	private __InflateFixedHuffman__()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Reads a fixed huffman code for use by the {@code TYPE_FIXED_HUFFMAN}
	 * state. This method does not traverse a huffman tree so to speak, but it
	 * instead uses many if statements. Initially every consideration was made
	 * but now instead it uses ranges once it keeps deep enough into the tree.
	 * This method is faster and provides a built-in huffman tree while not
	 * taking up too many bytes in the byte code.
	 *
	 * This is for the data decoder version.
	 *
	 * @param __in The circular boolean buffer to get bits from.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	static int read(InflateDataPipe __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The long if statement block
		if (__in.__zzRead())
			if (__in.__zzRead())
				if (__in.__zzRead())
					return 192 + __in.__zzReadInt(6, true);
				else
					if (__in.__zzRead())
						return 160 + __in.__zzReadInt(5, true);
					else
						if (__in.__zzRead())
							return 144 + __in.__zzReadInt(4, true);
						else
							return 280 + __in.__zzReadInt(3, true);
			else
				return 80 + __in.__zzReadInt(6, true);
		else
			if (__in.__zzRead())
				return 16 + __in.__zzReadInt(6, true);
			else
				if (__in.__zzRead())
					if (__in.__zzRead())
						return 0 + __in.__zzReadInt(4, true);
					else
						return 272 + __in.__zzReadInt(3, true);
				else
					return 256 + __in.__zzReadInt(4, true);
	}
}

