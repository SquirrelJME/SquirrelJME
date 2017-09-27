// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

/**
 * This represents a binary name which consists of a class which is
 * separted internally by forwarded slashes.
 *
 * @since 2017/09/27
 */
public final class BinaryName
	implements Comparable<BinaryName>
{
	/**
	 * Initializes the binary name.
	 *
	 * @param __n The name to initialize.
	 * @throws InvalidClassFormatException If the binary name is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public BinaryName(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int compareTo(BinaryName __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

