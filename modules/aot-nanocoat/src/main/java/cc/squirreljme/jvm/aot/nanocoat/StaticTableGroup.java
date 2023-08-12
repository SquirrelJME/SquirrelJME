// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

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
public class StaticTableGroup
{
	/** Static table outputs. */
	protected final Map<StaticTableType, StaticTable<?>> tables;
	
	/**
	 * Initializes the table group.
	 *
	 * @since 2023/08/12
	 */
	public StaticTableGroup()
	{
		// Initialize tables
		Map<StaticTableType, StaticTable<?>> tables =
			new EnumTypeMap<StaticTableType, StaticTable<?>>(
				StaticTableType.class, StaticTableType.values());
		Reference<StaticTableGroup> self = new WeakReference<>(this);
		for (StaticTableType type : StaticTableType.TYPES)
			tables.put(type, new StaticTable<>(self, type));
		
		// Store all tables
		this.tables = UnmodifiableMap.of(tables);
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
