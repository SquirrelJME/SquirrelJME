// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.jvm.aot.nanocoat.ArchiveOutputQueue;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * A group of static tables.
 *
 * @see StaticTable
 * @since 2023/08/12
 */
public class StaticTableManager
{
	/** Static table outputs. */
	protected final Map<StaticTableType, StaticTable<?>> tables;
	
	/** The archive to write to. */
	protected final ArchiveOutputQueue archive;
	
	/**
	 * Initializes the table group.
	 *
	 * @param __archive The archive to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public StaticTableManager(ArchiveOutputQueue __archive)
		throws NullPointerException
	{
		if (__archive == null)
			throw new NullPointerException("NARG");
		
		// Initialize tables
		Map<StaticTableType, StaticTable<?>> tables =
			new EnumTypeMap<StaticTableType, StaticTable<?>>(
				StaticTableType.class, StaticTableType.values());
		Reference<StaticTableManager> self = new WeakReference<>(this);
		for (StaticTableType type : StaticTableType.TYPES)
			tables.put(type, type.__newTable(self));
		
		// Store all tables
		this.tables = UnmodifiableMap.of(tables);
		this.archive = __archive;
	}
	
	/**
	 * Returns the string table.
	 *
	 * @return The string table.
	 * @since 2023/08/12
	 */
	public StringStaticTable strings()
	{
		return (StringStaticTable)this.table(String.class,
			StaticTableType.STRINGS);
	}
	
	/**
	 * Returns the table for the given type. 
	 *
	 * @param <E> The element type to store.
	 * @param __elementType The element type to store.
	 * @param __type The type of table to get.
	 * @return The table for the given type.
	 * @throws ClassCastException If the element type is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	@SuppressWarnings("unchecked")
	public <E> StaticTable<E> table(Class<E> __elementType,
		StaticTableType __type)
		throws ClassCastException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Check type of requested table
		StaticTable<?> rv = this.tables.get(__type);
		if (!rv.type.elementType.isAssignableFrom(__elementType))
			throw new ClassCastException("CLCL");
		
		// Use it
		return (StaticTable<E>)rv;
	}
}
