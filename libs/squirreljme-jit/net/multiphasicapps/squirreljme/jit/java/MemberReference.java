// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is the base class for field and method references.
 *
 * @since 2017/06/12
 */
public abstract class MemberReference
{
	/** The class this refers to. */
	protected final ClassName classname;
	
	/** The name of the member this refers to. */
	protected final Identifier identifier;
	
	/**
	 * Initializes the base member reference.
	 *
	 * @param __c The class the member resides in.
	 * @param __i The member which is being referenced.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	MemberReference(ClassName __c, Identifier __i)
		throws NullPointerException
	{
		// Check
		if (__c == null || __i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __c;
		this.identifier = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Returns the class the member exists within.
	 *
	 * @return The class containing the member.
	 * @since 2017/06/12
	 */
	public final ClassName className()
	{
		return this.classname;
	}
	
	/**
	 * Returns the name of the member being referenced.
	 *
	 * @return The name of the referenced member.
	 * @since 2017/06/12
	 */
	public final Identifier memberName()
	{
		return this.identifier;
	}
}

