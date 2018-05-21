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
 * This represents an inner class that is contained within an outer class, it
 * is used by the compiler to determine how classes are contained within each
 * other.
 *
 * @since 2018/05/15
 */
public final class InnerClass
{
	/**
	 * Initializes an anonymous inner class.
	 *
	 * @param __n The name of the class.
	 * @param __f The class flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public InnerClass(ClassName __n, InnerClassFlags __f)
		throws NullPointerException
	{
		if (__n == null || __f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a standard inner class.
	 *
	 * @param __n The name of the class.
	 * @param __o The class this is a member of.
	 * @param __i The identifier used to name the class.
	 * @param __f The class flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public InnerClass(ClassName __n, ClassName __o, ClassIdentifier __i,
		InnerClassFlags __f)
		throws NullPointerException
	{
		if (__n == null || __o == null || __i == null || __f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
}

