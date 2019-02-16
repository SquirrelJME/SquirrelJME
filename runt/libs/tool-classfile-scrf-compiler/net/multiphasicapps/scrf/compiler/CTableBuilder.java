// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.scrf.CTableEntryIndex;

/**
 * This contains the builder for the class description table.
 *
 * @since 2019/01/19
 */
@Deprecated
public final class CTableBuilder
{
	/** The string table. */
	protected final StringTableBuilder strings;
	
	/** Mapping of CTable keys to actual values. */
	private final Map<CTableEntryIndex, Object> _table =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the CTable builder
	 *
	 * @param __s The string table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public CTableBuilder(StringTableBuilder __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.strings = __s;
	}
	
	/**
	 * Sets the index in the ctable to the given value.
	 *
	 * @param __k The index to set.
	 * @param __v The value to use.
	 * @throws IllegalStateException If the entry has already been set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public final void set(CTableEntryIndex __k, Object __v)
		throws IllegalStateException, NullPointerException
	{
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock on this to add the item
		Map<CTableEntryIndex, Object> table = this._table;
		synchronized (this)
		{
			// {@squirreljme.error AT01 Entry in the CTable has already been
			// set. (The entry key)}
			if (table.containsKey(__k))
				throw new IllegalStateException("AT01 " + __k);
			
			table.put(__k, __v);
		}
	}
}

