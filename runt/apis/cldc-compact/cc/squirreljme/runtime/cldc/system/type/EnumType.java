// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

/**
 * This represents an enumerated type which is passed through system calls.
 * This class is intended to allow the enumerated type to be passed across
 * VMs 
 *
 * @since 2018/03/14
 */
public final class EnumType
{
	/** The class this enumeration is in. */
	protected final ClassType inclass;
	
	/** The ordinal of this enumeration. */
	protected final int ordinal;
	
	/** The name of the element. */
	protected final String name;
	
	/**
	 * Initializes the enumerated type.
	 *
	 * @param __c The class type of the enumeration.
	 * @param __dx The ordinal of the enumeration.
	 * @param __n The name of the value.
	 * @throws IllegalArgumentException If the index is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public EnumType(String __c, int __dx, String __n)
		throws IllegalArgumentException, NullPointerException
	{
		this(new ClassType(__c), __dx, __n);
	}
	
	/**
	 * Initializes the enumerated type.
	 *
	 * @param __c The class type of the enumeration.
	 * @param __dx The ordinal of the enumeration.
	 * @param __n The name of the value.
	 * @throws IllegalArgumentException If the index is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public EnumType(ClassType __c, int __dx, String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// {@squirreljme.error ZZ0l An enumeration cannot have a negative
		// ordinal.}
		if (__dx < 0)
			throw new IllegalArgumentException("ZZ0l");
		if (__c == null || __n == null)
			throw new NullPointerException("NARG");
		
		this.inclass = __c;
		this.ordinal = __dx;
		this.name = __n;
	}
	
	/**
	 * Returns the class type of the enumeration.
	 *
	 * @return The class type of the enumeration.
	 * @since 2018/03/14
	 */
	public final ClassType classType()
	{
		return this.inclass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of the enumeration.
	 *
	 * @return The enumeration name.
	 * @since 2018/03/14
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the ordinal of the enumeration.
	 *
	 * @return The enumeration ordinal.
	 * @since 2018/03/14
	 */
	public final int ordinal()
	{
		return this.ordinal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

