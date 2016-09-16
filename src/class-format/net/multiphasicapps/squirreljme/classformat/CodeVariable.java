// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single variable that refers a local variable or a stack
 * variable.
 *
 * This class is immutable.
 *
 * @since 2016/09/15
 */
public final class CodeVariable
	implements Comparable<CodeVariable>
{
	/** Is this a stack variable? */
	protected final boolean stack;
	
	/** The position of this variable. */
	protected final int id;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the variable.
	 *
	 * @param __s If {@code true} then the variable is a stack variable.
	 * @param __id The identifier of the variable.
	 * @throws IndexOutOfBoundsException If the identifier is negative.
	 * @since 2016/09/15
	 */
	private CodeVariable(boolean __s, int __id)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AY0j Code variable identifier cannot be
		// negative.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("AY0j");
		
		// Set
		this.stack = __s;
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public int compareTo(CodeVariable __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Locals before stack
		boolean as = this.stack, bs = __o.stack;
		if (!as && bs)
			return -1;
		else if (as && !bs)
			return 1;
		
		// Compare by ID
		int ai = this.id, bi = __o.id;
		if (ai < bi)
			return -1;
		else if (ai > bi)
			return 1;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof CodeVariable))
			return false;
		
		// Cast and compare
		CodeVariable o = (CodeVariable)__o;
		return this.stack == o.stack && this.id == o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final int hashCode()
	{
		int id = this.id;
		if (this.stack)
			return ~id;
		return id;
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
	 * Returns whether or not this is a local variable.
	 *
	 * @return {@code true} if a local variable.
	 * @since 2016/09/15
	 */
	public final boolean isLocal()
	{
		return !this.stack;
	}
	
	/**
	 * Returns whether or not this is a stack variable.
	 *
	 * @return {@code true} if a stack variable.
	 * @since 2016/09/15
	 */
	public final boolean isStack()
	{
		return this.stack;
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
			this._string = new WeakReference<>((rv = (this.stack ? "stack" :
				"local") + "#" + this.id));
		
		// Return
		return rv;
	}
	
	/**
	 * Initializes the variable of the given type and positioned identifier.
	 *
	 * @param __s If {@code true} then the variable is a stack variable.
	 * @param __id The identifier of the variable.
	 * @throws IndexOutOfBoundsException If the identifier is negative.
	 * @since 2016/09/15
	 */
	public static CodeVariable of(boolean __s, int __id)
		throws IndexOutOfBoundsException
	{
		return new CodeVariable(__s, __id);
	}
}

