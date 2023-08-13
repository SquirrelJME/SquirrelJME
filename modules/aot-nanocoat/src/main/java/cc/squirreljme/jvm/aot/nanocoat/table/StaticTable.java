// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ArchiveOutputQueue;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.Map;
import java.util.Set;

/**
 * This represents a static table which will output in a format similar to TSV
 * or CSV which is handled on the NanoCoat side of NanoCoat, it is intended
 * to deduplicate any entries in any given ROM so that every single library
 * accordingly does not need to duplicate the same information over and over.
 * These static tables are essentially used for everything and are ultimately
 * merged in the very end.
 *
 * @param <K> The key type for entries.
 * @param <V> The type of values to store, may be the same as keys. 
 * @since 2023/08/10
 */
public abstract class StaticTable<K, V>
{
	/** Entries within the static table. */
	protected final Map<K, CVariable> keys = new SortedTreeMap<>();
	
	/** Variable identifiers, used to check for collisions. */
	protected final Set<CIdentifier> identifiers =
		new SortedTreeSet<>();
	
	/** The type of table this is. */
	protected final StaticTableType type;
	
	/** The group which owns this table. */
	private final Reference<StaticTableManager> _group;
	
	/**
	 * Initializes the static table.
	 *
	 * @param __type The type of table this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTable(Reference<StaticTableManager> __group, StaticTableType __type)
		throws NullPointerException
	{
		if (__group == null || __type == null)
			throw new NullPointerException("NARG");
		
		this._group = __group;
		this.type = __type;
	}
	
	/**
	 * Returns the identifier to use for the entry.
	 *
	 * @param __entry The entry to identify.
	 * @return The identifier to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	protected abstract CIdentifier buildIdentity(K __entry)
		throws NullPointerException;
	
	/**
	 * Writes a table entry to the archive output.
	 *
	 * @param __archive The archive to write to.
	 * @param __fileName The suggested file name.
	 * @param __variable The variable being written.
	 * @param __entry The entry to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	protected abstract void writeEntry(ArchiveOutputQueue __archive,
		String __fileName, CVariable __variable, K __entry)
		throws IOException, NullPointerException;
	
	/**
	 * Returns the identifier to use for the entry.
	 *
	 * @param __entry The entry to identify.
	 * @return The identifier to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public final CIdentifier identify(K __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Puts an entry into the table if it does not exist, and returns the
	 * variable designated for it.
	 *
	 * @param __entry The entry to put in.
	 * @return The resultant variable.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public final CVariable put(K __entry)
		throws IOException, NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		throw new NullPointerException("NARG");
	}
}
