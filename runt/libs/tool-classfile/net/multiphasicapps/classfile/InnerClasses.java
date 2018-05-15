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
 * This contains the table of inner class references.
 *
 * @since 2018/05/15
 */
public final class InnerClasses
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
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the information which pertains to the inner classes.
	 *
	 * @param __pool The constant pool.
	 * @param __attrs The input attributes.
	 * @return The parsed inner class information.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public static final InnerClasses parse(Pool __pool,
		AttributeTable __attrs)
		throws IOException, NullPointerException
	{
		if (__pool == null || __attrs == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

