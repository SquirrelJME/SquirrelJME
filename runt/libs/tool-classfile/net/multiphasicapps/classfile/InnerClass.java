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
	/** The name of the inner class. */
	protected final ClassName name;
	
	/** The outer class this is contained within. */
	protected final ClassName outerclass;
	
	/** The simple name of the class as defined in the class. */
	protected final ClassIdentifier simplename;
	
	/** The flags for the inner class. */
	protected final InnerClassFlags flags;
	
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
		
		this.name = __n;
		this.outerclass = null;
		this.simplename = null;
		this.flags = __f;
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
		
		this.name = __n;
		this.outerclass = __o;
		this.simplename = __i;
		this.flags = __f;
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
	 * Returns the flags for the inner class.
	 *
	 * @return The inner class flags.
	 * @since 2018/06/16
	 */
	public final InnerClassFlags flags()
	{
		return this.flags;
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
	 * Is this an anonymous class?
	 *
	 * @return Is this an anonymous class?
	 * @since 2018/06/16
	 */
	public final boolean isAnonymous()
	{
		return this.name != null &&
			this.outerclass == null &&
			this.simplename == null;
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The class name.
	 * @since 2018/06/16
	 */
	public final ClassName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the name of the outer class or {@code null} if it is anonymous.
	 *
	 * @return The name of the outer class or {@code null} if anonymous.
	 * @since 2018/06/16
	 */
	public final ClassName outerClass()
	{
		return this.outerclass;
	}
	
	/**
	 * Returns the simple name of the class.
	 *
	 * @return The simple name of the class.
	 * @since 2018/06/16
	 */
	public final ClassIdentifier simpleName()
	{
		return this.simplename;
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

