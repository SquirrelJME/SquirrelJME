// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a generic type which contains a basic type along with
 * generic information.
 *
 * @since 2018/04/09
 */
public final class GenericType
{
	/** The represented base generic type. */
	protected final GenericBinaryName name;
	
	/** The number of array dimensions. */
	protected final int dimensions;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the generic type.
	 *
	 * @param __name The name of the type.
	 * @param __dims The number of dimensions.
	 * @throws IllegalArgumentException If the dimensions is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/09
	 */
	public GenericType(GenericBinaryName __name, int __dims)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ2r The number of dimensions for a type cannot
		// be negative.}
		if (__dims < 0)
			throw new IllegalArgumentException("AQ2r");
		
		this.name = __name;
		this.dimensions = __dims;
	}
	
	/**
	 * Returns the base type.
	 *
	 * @return The base type.
	 * @since 2018/04/09
	 */
	public final GenericBinaryName baseType()
	{
		return this.name;
	}
	
	/**
	 * Returns the dimensions in the array.
	 *
	 * @return The dimension count.
	 * @since 2018/04/09
	 */
	public final int dimensions()
	{
		return this.dimensions;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/09
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/09
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/09
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

