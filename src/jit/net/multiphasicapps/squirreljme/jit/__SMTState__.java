// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This represents a verification state which on entry to an operation, the
 * specified state must be matched. If it is not matched then a verification
 * exception would be thrown to indicate badly verified code.
 *
 * @since 2016/03/31
 */
class __SMTState__
{
	/** The state of the stack. */
	final __SMTStack__ _stack;
	
	/** The state of the locals. */
	final __SMTLocals__ _locals;

	/**
	 * Initializes the stack storage states.
	 *
	 * @param __ms Maximum number of stack variables.
	 * @param __ml Maximum number of local variables.
	 * @since 2016/08/29
	 */
	__SMTState__(int __ms, int __ml)
	{
		this._stack = new __SMTStack__(__ms, 0);
		this._locals = new __SMTLocals__(__ml);
	}
	
	/**
	 * Creates a new state which is duplicated from the existing state.
	 *
	 * @param __s The other state to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	__SMTState__(__SMTState__ __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Duplicate
		this._stack = new __SMTStack__(__s._stack);
		this._locals = new __SMTLocals__(__s._locals);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/29
	 */
	@Override
	public String toString()
	{
		return "{locals=" + this._locals + ", stack=" + this._stack + "}";
	}
}

