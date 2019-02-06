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

import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.scrf.VTableIndex;
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
	/** String table. */
	protected final StringTableBuilder strings;
	
	/** The table of entries which may accordingly be mapped. */
	private final Map<Object, VTableIndex> _table =
		new LinkedHashMap<>();
	
	/** The next ID to use for entries. */
	private int _nextid;
	
	/**
	 * Initializes the VTable builder.
	 *
	 * @param __s The string table to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public VTableBuilder(StringTableBuilder __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.strings = __s;
	}
	
	/**
	 * Adds a single entry to the vtable.
	 *
	 * @param __o The item to add.
	 * @return The position of the entry in the table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public final VTableIndex add(Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Prevent multiple threads from building the VTable
		Map<Object, VTableIndex> table = this._table;
		synchronized (this)
		{
			// If there is already a slot for this entry then use that
			VTableIndex vx = table.get(__o);
			if (vx != null)
				return vx;
			
			// The index to store this entry at
			int id = this._nextid++;
			table.put(__o, (vx = new VTableIndex(id)));
			
			// Debug
			todo.DEBUG.note("@%d = %s", id, __o);
			
			// Return the ID of this new entry
			return vx;
		}
	}
	
	/**
	 * Adds a field reference to the vtable.
	 *
	 * @param __static Is this field static?
	 * @param __f The field to add.
	 * @return The index of the added entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/03
	 */
	public final VTableIndex addFieldReference(boolean __static,
		FieldReference __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

