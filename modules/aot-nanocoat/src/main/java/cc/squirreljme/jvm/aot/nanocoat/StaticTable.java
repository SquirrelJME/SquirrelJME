// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.lang.ref.Reference;
import java.util.Map;

/**
 * This represents a static table which will output in a format similar to TSV
 * or CSV which is handled on the NanoCoat side of NanoCoat, it is intended
 * to deduplicate any entries in any given ROM so that every single library
 * accordingly does not need to duplicate the same information over and over.
 * These static tables are essentially used for everything and are ultimately
 * merged in the very end.
 *
 * @param <E> The type of entries to store.
 * @since 2023/08/10
 */
public class StaticTable<E>
{
	/** The length that prefixes must be. */
	private static final int _PREFIX_LENGTH = 4;
	
	/** Entries within the static table. */
	protected final Map<CIdentifier, E> entries = new SortedTreeMap<>();
	
	/** The type of table this is. */
	protected final StaticTableType type;
	
	/** The group which owns this table. */
	private final Reference<StaticTableGroup> _group;
	
	/**
	 * Initializes the static table.
	 *
	 * @param __type The type of table this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTable(Reference<StaticTableGroup> __group, StaticTableType __type)
		throws NullPointerException
	{
		if (__group == null || __type == null)
			throw new NullPointerException("NARG");
		
		this._group = __group;
		this.type = __type;
	}
}
