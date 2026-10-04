// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
public final class CRC32Table
{
	/** Static table reference. */
	private static final Map<Integer, Reference<CRC32Table>> _TABLES =
		new HashMap<>();
	
	/** CRC table data. */
	final int[] _table;
	
	/**
	 * Initializes the table data.
	 *
	 * @param __poly The polynomial.
	 * @since 2016/07/16
	 */
	@SuppressWarnings("MagicNumber")
	private CRC32Table(int __poly)
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
	 * Reads the given index from the CRC table.
	 * 
	 * @param __dx The index to get.
	 * @return The value at the given index.
	 * @throws IndexOutOfBoundsException If the index is not valid within the
	 * table.
	 * @since 2021/11/13
	 */
	public final int get(int __dx)
		throws IndexOutOfBoundsException
	{
		return this._table[__dx]; 
	}
	
	/**
	 * Obtains the CRC table.
	 *
	 * @param __poly The polynomial.
	 * @return The CRC table.
	 * @since 2016/07/16
	 */
	public static CRC32Table calculateTable(int __poly)
	{
		// Lock
		Map<Integer, Reference<CRC32Table>> tables =
			CRC32Table._TABLES;
		synchronized (CRC32Table.class)
		{
			// Get
			Integer i = __poly;
			Reference<CRC32Table> ref = tables.get(i);
			CRC32Table rv;
		
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				tables.put(i,
					new WeakReference<>((rv = new CRC32Table(__poly))));
		
			// Return
			return rv;
		}
	}
}

