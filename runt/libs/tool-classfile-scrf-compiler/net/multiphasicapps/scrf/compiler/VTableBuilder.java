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

/**
 * This is the builder for vtables which are used to contain some run-time
 * determined information about a class, it effectively allows the VM to
 * access other classes and information without needing to build a combined
 * structure, just interlinked.
 *
 * @since 2019/01/12
 */
public final class VTableBuilder
{
	/** The table of entries which may accordingly be mapped. */
	private final Map<Object, Integer> _table =
		new LinkedHashMap<>();
	
	/** The next ID to use for entries. */
	private int _nextid;
	
	/**
	 * Adds a single entry to the vtable.
	 *
	 * @param __o The item to add.
	 * @return The position of the entry in the table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public final int add(Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Prevent multiple threads from building the VTable
		Map<Object, Integer> table = this._table;
		synchronized (this)
		{
			// If there is already a slot for this entry then use that
			Integer vx = table.get(__o);
			if (vx != null)
				return vx;
			
			// The index to store this entry at
			int id = this._nextid++;
			table.put(__o, id);
			
			// Debug
			todo.DEBUG.note("@%d = %s", id, __o);
			
			// Return the ID of this new entry
			return id;
		}
	}
}

