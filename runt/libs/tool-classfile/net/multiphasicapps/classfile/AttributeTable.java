// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the attribute table that exists within a class.
 *
 * @since 2018/05/14
 */
public final class AttributeTable
{
	/** The attribute table. */
	private final Map<String, Attribute> _attributes;
	
	/**
	 * Initializes the attribute table.
	 *
	 * @param __a The input attributes.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public AttributeTable(Attribute... __a)
		throws NullPointerException
	{
		this(Arrays.<Attribute>asList((__a != null ? __a :
			new Attribute[0])));
	}
	
	/**
	 * Initializes the attribute table.
	 *
	 * @param __a The input attributes.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public AttributeTable(Iterable<Attribute> __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		Map<String, Attribute> attributes = new LinkedHashMap<>();
		for (Attribute a : __a)
		{
			if (a == null)
				throw new NullPointerException("NARG");
			
			String n = a.name();
			if (!attributes.containsKey(n))
				attributes.put(n, a);
		}
		this._attributes = attributes;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the attribute that uses the specified name.
	 *
	 * @param __n The name of the attribute to get.
	 * @return The specified attribute or {@code null} if it does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public final Attribute get(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this._attributes.get(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the attribute table.
	 *
	 * @return The attribute table length.
	 * @since 2018/05/14
	 */
	public final int size()
	{
		return this._attributes.size();
	}
	
	/**
	 * Parses the attribute table.
	 *
	 * @param __in The input stream.
	 * @param __pool The constant pool.
	 * @return The attribute table.
	 * @throws InvalidClassFormatException If the table is not correct.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static AttributeTable parse(DataInputStream __in, Pool __pool)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__in == null || __pool == null)
			throw new NullPointerException("NARG");
		
		int n = __in.readUnsignedShort();
		Attribute[] rv = new Attribute[n];
		
		// Read each entry
		for (int i = 0; i < n; i++)
		{
			String name = __pool.<UTFConstantEntry>require(
				UTFConstantEntry.class, __in.readUnsignedShort()).toString();
			
			// {@squirreljme.error JC1z Attributes with a size larger than two
			// gigabytes are not supported.}
			int len = __in.readInt();
			if (len < 0)
				throw new InvalidClassFormatException("JC1z");
			
			byte[] data = new byte[len];
			__in.readFully(data);
			
			rv[i] = new Attribute(name, data, 0, len);
		}
		
		return new AttributeTable(rv);
	}
}

