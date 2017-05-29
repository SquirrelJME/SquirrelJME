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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single variable that refers to a slot on the stack, in
 * locla variables, or the working area.
 *
 * This class is immutable.
 *
 * @since 2016/09/15
 */
public final class Variable
	implements Comparable<Variable>
{
	/** Where is this variable stored? */
	protected final VariableLocation area;
	
	/** The position of this variable. */
	protected final int id;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the variable.
	 *
	 * @param __t The location where the variable is stored.
	 * @param __id The identifier of the variable.
	 * @throws IndexOutOfBoundsException If the identifier is negative.
	 * @since 2016/09/15
	 */
	public Variable(VariableLocation __t, int __id)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ1h Code variable identifier cannot be
		// negative.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("AQ1h");
		
		// Set
		this.area = __t;
		this.id = __id;
	}
	
	/**
	 * Returns the storage type area.
	 *
	 * @return The area storage type.
	 * @since 2017/03/31
	 */
	public final VariableLocation area()
	{
		return this.area;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public int compareTo(Variable __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Compare type first
		int rv = this.area.ordinal() - __o.area.ordinal();
		if (rv != 0)
			return rv;
		
		// Then by ID
		return this.id - __o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Variable))
			return false;
		
		// Cast and compare
		Variable o = (Variable)__o;
		return this.area == o.area && this.id == o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final int hashCode()
	{
		return id ^ this.area.hashCode();
	}
	
	/**
	 * Returns the variable ID.
	 *
	 * @return The variable ID.
	 * @since 2016/09/15
	 */
	public final int id()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.area + "#" +
				this.id));
		
		// Return
		return rv;
	}
}

