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

/**
 * This represents the attribute table that exists within a class.
 *
 * @since 2018/05/14
 */
public final class AttributeTable
{
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
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the attribute table.
	 *
	 * @return The attribute table length.
	 * @since 2018/05/14
	 */
	public final int length()
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
		
		throw new todo.TODO();
	}
}

