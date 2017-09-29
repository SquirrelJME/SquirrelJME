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

import java.util.Objects;

/**
 * This represents the name of a class or array within the virtual machine.
 *
 * This class is immutable.
 *
 * @since 2017/09/27
 */
public class ClassName
	implements Comparable<ClassName>
{
	/** The binary name of the class. */
	protected final BinaryName binary;
	
	/** The field type of the class (for arrays). */
	protected final FieldDescriptor field;
	
	/**
	 * Initializes the class name.
	 *
	 * @param __n The input string.
	 * @throws InvalidClassFormatException If it is not a valid class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public ClassName(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Is an array?
		if (__n.startsWith("["))
		{
			this.binary = null;
			this.field = new FieldDescriptor(__n);
		}
		
		// Not an array
		else
		{
			this.binary = new BinaryName(__n);
			this.field = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int compareTo(ClassName __o)
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
		if (!(__o instanceof ClassName))
			return false;
		
		ClassName o = (ClassName)__o;
		return Objects.equals(this.binary, o.binary) &&
			Objects.equals(this.field, o.field);
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

