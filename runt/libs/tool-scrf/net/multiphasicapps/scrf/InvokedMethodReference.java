// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import net.multiphasicapps.classfile.MethodReference;

/**
 * This represents an invoked method reference based on a reference to a
 * method.
 *
 * @since 2019/02/24
 */
public final class InvokedMethodReference
{
	/** The invocation type. */
	protected final InvokeType type;
	
	/** The method reference. */
	protected final MethodReference method;
	
	/**
	 * Initializes the reference.
	 *
	 * @param __t The invocation type.
	 * @param __m The method type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public InvokedMethodReference(InvokeType __t, MethodReference __m)
		throws NullPointerException
	{
		if (__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.method = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof InvokedMethodReference))
			return false;
		
		InvokedMethodReference o = (InvokedMethodReference)__o;
		return this.type == o.type &&
			this.method.equals(o.method);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final int hashCode()
	{
		return this.type.hashCode() ^
			this.method.hashCode();
	}
}

