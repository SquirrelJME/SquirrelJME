// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This contains the table used for calculation of the CRC.
 *
 * @since 2016/07/16
 */
final class __CRC32Table__
{
	/** Static table reference. */
	private static final Map<Integer, Reference<__CRC32Table__>> _TABLES =
		new HashMap<>();
	
	/** CRC table data. */
	final int[] _table;
	
	/**
	 * Initializes the table data.
	 *
	 * @param __poly The polynomial.
	 * @since 2016/07/16
	 */
	private __CRC32Table__(int __poly)
	{
		// Setup table;
		int[] table = new int[256];
		for (int i = 0; i < 256; i++)
		{
			int remainder = i << 24;
			
			// Divide all bits possibly
			for (int b = 0; b < 8; b++)
			{
				int oldrem = remainder;
				remainder <<= 1;
				
				// XOR in polynomial
				if (0 != (oldrem & 0x8000_0000))
					remainder ^= __poly;
			}
			
			// Store
			table[i] = remainder;
		}
		
		// Store
		this._table = table;
	}
	
	/**
	 * Obtains the CRC table.
	 *
	 * @parma __poly The polynomial.
	 * @return The CRC table.
	 * @since 2016/07/16
	 */
	static final __CRC32Table__ __table(int __poly)
	{
		// Lock
		Map<Integer, Reference<__CRC32Table__>> tables = _TABLES;
		synchronized (tables)
		{
			// Get
			Integer i = Integer.valueOf(__poly);
			Reference<__CRC32Table__> ref = tables.get(i);
			__CRC32Table__ rv;
		
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				tables.put(i,
					new WeakReference<>((rv = new __CRC32Table__(__poly))));
		
			// Return
			return rv;
		}
	}
}

