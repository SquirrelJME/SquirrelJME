// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.UnmodifiableIterator;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents a local variable table.
 *
 * @since 2022/09/21
 */
public final class LocalVariableTable
	implements Contexual, Iterable<LocalVariableInfo>
{
	/** The entries within the table. */
	private final LocalVariableInfo[] _entries;
	
	/**
	 * Initializes the local variable table.
	 * 
	 * @param __vars the variables to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/23
	 */
	public LocalVariableTable(LocalVariableInfo... __vars)
		throws NullPointerException
	{
		if (__vars == null)
			throw new NullPointerException("NARG");
		
		__vars = __vars.clone();
		for (LocalVariableInfo var : __vars)
			if (var == null)
				throw new NullPointerException("NARG");
		
		this._entries = __vars;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof LocalVariableTable))
			return false;
		
		LocalVariableTable that = (LocalVariableTable)__o;
		return Arrays.equals(this._entries, that._entries);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public int hashCode()
	{
		return Arrays.asList(this._entries).hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public Iterator<LocalVariableInfo> iterator()
	{
		return UnmodifiableIterator.of(this._entries);
	}
	
	/**
	 * Returns the number of entries within.
	 * 
	 * @return The size of the table.
	 * @since 2022/09/23
	 */
	public int size()
	{
		return this._entries.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public String toString()
	{
		return Arrays.asList(this._entries).toString();
	}
	
	/**
	 * Parses the local variable tables.
	 * 
	 * @param __pool The pool used.
	 * @param __attrs The attributes to read from.
	 * @return The resultant local variable table.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/21
	 */
	public static LocalVariableTable parse(Pool __pool,
		AttributeTable __attrs)
		throws IOException, NullPointerException
	{
		if (__pool == null || __attrs == null)
			throw new NullPointerException("NARG");
		
		// Get the table if it exists
		Attribute attr = __attrs.get("LocalVariableTable");
		if (attr == null)
			return new LocalVariableTable();
		
		// Parse data
		try (DataInputStream in = attr.open())
		{
			// How many?
			int count = in.readUnsignedShort();
			
			// Setup and load each one
			LocalVariableInfo[] result = new LocalVariableInfo[count];
			for (int i = 0; i < count; i++)
				result[i] = new LocalVariableInfo(
					in.readUnsignedShort(),
					in.readUnsignedShort(),
					new FieldName(__pool.require(UTFConstantEntry.class,
						in.readUnsignedShort()).toString()),
					new FieldDescriptor(__pool.require(UTFConstantEntry.class,
						in.readUnsignedShort()).toString()),
					in.readUnsignedShort());
			
			return new LocalVariableTable(result);
		}
	}
}
