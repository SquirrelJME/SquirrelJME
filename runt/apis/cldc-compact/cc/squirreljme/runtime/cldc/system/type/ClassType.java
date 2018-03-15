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
 * This represents the name of a class as seen by a remote virtual machine so
 * that the kernel and such may reference classes.
 *
 * @since 2018/03/14
 */
public final class ClassType
{
	/** The name of the class. */
	protected final String name;
	
	/**
	 * Initializes the class type.
	 *
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public ClassType(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
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
	 * Does this logically refer to the same class?
	 *
	 * @param __cl The class to check.
	 * @return If this refers to the same local class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public final boolean isClass(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this.name.equals(__cl.getName());
	}
	
	/**
	 * Returns the name of the class.
	 *
	 * @return The class name.
	 * @since 2018/03/14
	 */
	public final String name()
	{
		return this.name;
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

