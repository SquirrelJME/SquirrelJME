// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the location of a variable, where it is located and the
 * index of that variable.
 *
 * @since 2017/08/12
 */
public final class Variable
{
	/** Copied this variable, used for synchronization. */
	public static final Variable SYNCHRONIZED =
		new Variable(VariableLocation.SYNCHRONIZED, 0);
	
	/** The exception being thrown. */
	public static final Variable THROWING_EXCEPTION =
		new Variable(VariableLocation.THROWING_EXCEPTION, 0);
	
	/** The variable location. */
	protected final VariableLocation location;
	
	/** The variable index. */
	protected final int index;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the variable.
	 *
	 * @param __l The variable location.
	 * @param __i The index of the variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public Variable(VariableLocation __l, int __i)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.location = __l;
		this.index = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s#%d",
				this.location, this.index)));
		
		return rv;
	}
}

