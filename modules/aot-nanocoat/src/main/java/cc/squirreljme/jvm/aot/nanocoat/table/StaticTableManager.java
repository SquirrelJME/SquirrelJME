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
import cc.squirreljme.jvm.aot.nanocoat.CArchiveOutputQueue;
import cc.squirreljme.jvm.aot.nanocoat.CodeFingerprint;
import cc.squirreljme.jvm.aot.nanocoat.VariableLimits;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.collections.UnmodifiableSet;

/**
 * A group of static tables.
 *
 * @see StaticTable
 * @since 2023/08/12
 */
public class StaticTableManager
{
	/** The archive to write to. */
	protected final CArchiveOutputQueue archive;
	
	/** Static table outputs. */
	private final Map<StaticTableType, StaticTable<?, ?>> _tables;
	
	/** Self reference. */
	private final Reference<StaticTableManager> _self =
		new WeakReference<>(this);
	
	/**
	 * Initializes the table group.
	 *
	 * @param __archive The archive to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public StaticTableManager(CArchiveOutputQueue __archive)
		throws NullPointerException
	{
		if (__archive == null)
			throw new NullPointerException("NARG");
		
		// Initialize tables
		this._tables = new EnumTypeMap<StaticTableType, StaticTable<?, ?>>(
				StaticTableType.class, StaticTableType.values());
		
		// Store all tables
		this.archive = __archive;
	}
	
	/**
	 * Returns the code table.
	 *
	 * @return The code table.
	 * @since 2023/08/12
	 */
	public CodeStaticTable code()
	{
		return (CodeStaticTable)this.table(CodeFingerprint.class,
			ByteCode.class, StaticTableType.CODE);
	}
	
	/**
	 * Returns the field type table.
	 *
	 * @return The field type table.
	 * @since 2023/08/13
	 */
	public FieldTypeStaticTable fieldType()
	{
		return (FieldTypeStaticTable)this.table(FieldDescriptor.class,
			StaticTableType.FIELD_TYPE);
	}
	
	/**
	 * Does the static table manager have entries for this?
	 *
	 * @param __type The type of table to check.
	 * @return If there are entries for this.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public boolean hasEntries(StaticTableType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		StaticTable<?, ?> table = this._tables.get(__type);
		if (table == null || table.identifiers().isEmpty())
			return false;
		
		return true;
	}
	
	/**
	 * Returns the identifiers in every static table.
	 *
	 * @return The static table identifiers within every table.
	 * @since 2023/09/03
	 */
	public final Set<CIdentifier> identifiers()
	{
		SortedTreeSet<CIdentifier> result = new SortedTreeSet<>();
		for (StaticTable<?, ?> table : this._tables.values())
			if (table != null)
				result.addAll(table.identifiers());
		
		return UnmodifiableSet.of(result);
	}
	
	/**
	 * The method type table.
	 *
	 * @return The method type table.
	 * @since 2023/08/13
	 */
	public MethodTypeStaticTable methodType()
	{
		return (MethodTypeStaticTable)this.table(MethodDescriptor.class,
			StaticTableType.METHOD_TYPE);
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
			StaticTableType.STRING);
	}
	
	/**
	 * Returns the table for the given type.
	 *
	 * @param <K> The element type to store.
	 * @param __keyType The key type to store.
	 * @param __type The type of table to get.
	 * @return The table for the given type.
	 * @throws ClassCastException If the element type is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public <K> StaticTable<K, K> table(Class<K> __keyType,
		StaticTableType __type)
		throws ClassCastException, NullPointerException
	{
		return this.table(__keyType, __keyType, __type);
	}
	
	/**
	 * Returns the table for the given type.
	 *
	 * @param <K> The element type to store.
	 * @param <V> The value type.
	 * @param __keyType The key type to store.
	 * @param __valueType The value type used.
	 * @param __type The type of table to get.
	 * @return The table for the given type.
	 * @throws ClassCastException If the element type is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	@SuppressWarnings("unchecked")
	public <K, V> StaticTable<K, V> table(Class<K> __keyType,
		Class<V> __valueType, StaticTableType __type)
		throws ClassCastException, NullPointerException
	{
		if (__keyType == null || __valueType == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Get and create table if it is missing, lazy initialization since
		// for some libraries not all tables might be used
		Map<StaticTableType, StaticTable<?, ?>> tables = this._tables;
		StaticTable<?, ?> rv = tables.get(__type);
		if (rv == null)
		{
			rv = __type.__newTable(this._self);
			tables.put(__type, rv);
		}
		
		// Check type of requested table
		if (!rv.type.keyType.isAssignableFrom(__keyType))
			throw new ClassCastException("CLCL");
		if (!rv.type.valueType.isAssignableFrom(__valueType))
			throw new ClassCastException("CLCL");
		
		// Use it
		return (StaticTable<K, V>)rv;
	}
	
	/**
	 * Returns the variable limits table.
	 *
	 * @return The variable limits table.
	 * @since 2023/08/13
	 */
	public VariableLimitsStaticTable variableLimits()
	{
		return (VariableLimitsStaticTable)this.table(VariableLimits.class,
			StaticTableType.VARIABLE_LIMITS);
	}
}
