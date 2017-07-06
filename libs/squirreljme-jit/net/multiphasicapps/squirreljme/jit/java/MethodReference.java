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

import net.multiphasicapps.squirreljme.jit.bin.ClassName;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This describes a reference to a method.
 *
 * @since 2017/06/12
 */
public final class MethodReference
	extends MemberReference
{
	/** The method descriptor. */
	protected final MethodDescriptor descriptor;
	
	/** Is this an interface? */
	protected final boolean isinterface;
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __c The class the member resides in.
	 * @param __i The name of the member.
	 * @param __t The descriptor of the member.
	 * @param __int Does this refer to an interface method?
	 * @since 2017/06/12
	 */
	public MethodReference(ClassName __c, Identifier __i, MethodDescriptor __t,
		boolean __int)
		throws NullPointerException
	{
		super(__c, __i);
		
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.descriptor = __t;
		this.isinterface = __int;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof MethodReference))
			return false;
		
		MethodReference o = (MethodReference)__o;
		return this.classname.equals(o.classname) &&
			this.identifier.equals(o.identifier) &&
			this.descriptor.equals(o.descriptor) &&
			this.isinterface == o.isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.classname.hashCode() ^ this.identifier.hashCode() ^
			this.descriptor.hashCode() ^ (this.isinterface ? 1 : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

