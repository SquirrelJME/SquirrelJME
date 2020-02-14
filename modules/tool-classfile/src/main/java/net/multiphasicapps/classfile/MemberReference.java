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

/**
 * This is the base class for field and method references.
 *
 * @since 2017/06/12
 */
public abstract class MemberReference
{
	/** The class this refers to. */
	protected final ClassName classname;
	
	/**
	 * Initializes the base member reference.
	 *
	 * @param __c The class the member resides in.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	MemberReference(ClassName __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __c;
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
	 * Returns the name of the member being referenced.
	 *
	 * @return The name of the referenced member.
	 * @since 2017/06/12
	 */
	public abstract Identifier memberName();
	
	/**
	 * Returns the name and type of the member.
	 *
	 * @return The member name and type.
	 * @since 2018/09/09
	 */
	public abstract MemberNameAndType memberNameAndType();
	
	/**
	 * Returns the type of the member.
	 *
	 * @return The member type.
	 * @since 2018/09/09
	 */
	public abstract MemberDescriptor memberType();
	
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
}

